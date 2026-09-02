import Speech
import AVFoundation

/**
 * iOS speech recognition wrapper — thin platform-specific implementation.
 * Calls shared KMM ViewModel methods on results, no business logic here.
 */
class SpeechRecognitionService: ObservableObject {
    private var recognizer: SFSpeechRecognizer?
    private var audioEngine = AVAudioEngine()
    private var request: SFSpeechAudioBufferRecognitionRequest?
    private var task: SFSpeechRecognitionTask?

    @Published var isAuthorized = false
    @Published var isListening = false

    init(locale: Locale = Locale(identifier: "rw-RW")) {
        recognizer = SFSpeechRecognizer(locale: locale)
    }

    func requestPermissions() async -> Bool {
        let speechAuth = await withCheckedContinuation { continuation in
            SFSpeechRecognizer.requestAuthorization { status in
                continuation.resume(returning: status == .authorized)
            }
        }

        let audioAuth = await withCheckedContinuation { continuation in
            AVAudioSession.sharedInstance().requestRecordPermission { granted in
                continuation.resume(returning: granted)
            }
        }

        await MainActor.run {
            isAuthorized = speechAuth && audioAuth
        }
        return speechAuth && audioAuth
    }

    func startListening(
        onResult: @escaping (String) -> Void,
        onError: @escaping (String) -> Void
    ) {
        guard let recognizer, recognizer.isAvailable else {
            onError("Speech recognizer not available")
            return
        }

        let inputNode = audioEngine.inputNode

        request = SFSpeechAudioBufferRecognitionRequest()
        guard let request else { return }
        request.shouldReportPartialResults = true

        task = recognizer.recognitionTask(with: request) { [weak self] result, error in
            guard let self else { return }

            if let error {
                DispatchQueue.main.async {
                    onError(error.localizedDescription)
                    self.isListening = false
                }
                return
            }

            if let result {
                let text = result.bestTranscription.formattedString
                DispatchQueue.main.async {
                    onResult(text)
                }

                if result.isFinal {
                    self.stopListening()
                }
            }
        }

        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { [weak self] buffer, _ in
            self?.request?.append(buffer)
        }

        audioEngine.prepare()
        try? audioEngine.start()

        DispatchQueue.main.async {
            self.isListening = true
        }
    }

    func stopListening() {
        audioEngine.stop()
        audioEngine.inputNode.removeTap(onBus: 0)
        request?.endAudio()
        task?.cancel()
        task = nil
        request = nil

        DispatchQueue.main.async {
            self.isListening = false
        }
    }
}
