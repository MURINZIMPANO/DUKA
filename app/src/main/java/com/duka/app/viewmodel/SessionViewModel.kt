package com.duka.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duka.app.data.local.entity.User
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.EmployeeRepository
import com.duka.app.data.repository.UserRepository
import com.duka.app.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SessionViewModel — Single source of truth for all navigation and session state.
 *
 * DESIGN RULE (from spec):
 * - Button taps update state immediately (optimistic), then persist in the background.
 * - If a persistence call fails, catch it and show a real error state on screen — never fail silently.
 * - Every screen reads navigation state from this ViewModel via collectAsStateWithLifecycle().
 * - A screen must NEVER gate its own "Continue" button on a condition that nothing else can satisfy.
 *   This is the exact bug that broke the previous build, and this ViewModel exists to prevent it.
 *
 * Pattern:
 * 1. Screen calls a ViewModel action (e.g., login, createBusiness).
 * 2. ViewModel sets _uiState to Loading immediately.
 * 3. ViewModel performs the DB/DataStore work.
 * 4. On success: updates session state → Compose NavHost observes and navigates.
 * 5. On failure: updates _uiState to Error(message) → screen shows the error.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    /** The current navigation destination. */
    sealed class NavDestination {
        data object Splash : NavDestination()
        data object Login : NavDestination()
        data object CompanySignup : NavDestination()
        data object ClientSignup : NavDestination()
        data object EmployeeLogin : NavDestination()
        data object Dashboard : NavDestination()
        data object EmployeeSell : NavDestination()
        // V3/V4 destinations
        data object ClientDiscover : NavDestination()
        data object GovernmentAdmin : NavDestination()
    }

    data class SessionUiState(
        val isLoading: Boolean = true,
        val destination: NavDestination = NavDestination.Splash,
        val error: String? = null,
        val currentUser: User? = null,
        val currentUserName: String = "",
        val currentRole: String? = null,
        val currentBusinessId: Long = 0L
    )

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        // Check session on launch
        viewModelScope.launch {
            try {
                val hasSession = sessionManager.hasSession.first()
                if (hasSession) {
                    val role = sessionManager.currentRole.first()
                    val businessId = sessionManager.currentBusinessId.first()
                    val userName = sessionManager.currentUserName.first()
                    val userId = sessionManager.currentUserId.first()
                    val user = userRepository.getById(userId)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = user,
                            currentUserName = userName,
                            currentRole = role,
                            currentBusinessId = businessId,
                            destination = when (role) {
                                "owner" -> NavDestination.Dashboard
                                "employee" -> NavDestination.EmployeeSell
                                "client" -> NavDestination.ClientDiscover
                                "government_admin" -> NavDestination.GovernmentAdmin
                                else -> NavDestination.Login
                            }
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, destination = NavDestination.Login)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to check session: ${e.message}",
                        destination = NavDestination.Login
                    )
                }
            }
        }
    }

    /**
     * Login with phone/email and password.
     * Optimistic: sets loading state immediately, then persists.
     */
    fun login(phoneOrEmail: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getByPhoneOrEmail(phoneOrEmail)
                if (user == null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Account doesn't exist. Please sign up first.")
                    }
                    return@launch
                }

                // Verify password using BCrypt
                val passwordMatches = try {
                    org.mindrot.jbcrypt.BCrypt.checkpw(password, user.passwordHash)
                } catch (e: Exception) {
                    false
                }

                if (!passwordMatches) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Incorrect password")
                    }
                    return@launch
                }

                // Password correct — save session
                val businessId = user.businessId ?: 0L
                sessionManager.saveSession(
                    userId = user.id,
                    businessId = businessId,
                    role = user.role,
                    userName = user.fullName
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = user,
                        currentUserName = user.fullName,
                        currentRole = user.role,
                        currentBusinessId = businessId,
                        destination = when (user.role) {
                            "owner" -> NavDestination.Dashboard
                            "employee" -> NavDestination.EmployeeSell
                            "client" -> NavDestination.ClientDiscover
                            "government_admin" -> NavDestination.GovernmentAdmin
                            else -> NavDestination.Dashboard
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Login failed: ${e.message}")
                }
            }
        }
    }

    /**
     * Employee login with business code (no name needed — the code resolves everything).
     * The employee never has to type or know their businessId — the code alone resolves it.
     */
    fun employeeLogin(businessCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val code = businessCode.trim()
                if (code.isEmpty()) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Please enter your employee code")
                    }
                    return@launch
                }

                // Query Employee table by code — one row expected
                // First check if any employee with this code exists (active or not)
                val anyEmployee = employeeRepository.getByCodeAny(code)
                if (anyEmployee == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No active employee with that code — check with your shop owner"
                        )
                    }
                    return@launch
                }

                // If found but deactivated, show distinct message
                if (!anyEmployee.isActive) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "This account has been deactivated — contact your shop owner"
                        )
                    }
                    return@launch
                }

                // Active employee found — resolve the linked User row
                val user = userRepository.getById(anyEmployee.userId)
                if (user == null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Employee account not found. Contact your shop owner.")
                    }
                    return@launch
                }

                // Update lastSeenAt
                employeeRepository.updateLastSeenAt(anyEmployee.id, System.currentTimeMillis())

                // Save session — businessId comes from the Employee row, not from user input
                sessionManager.saveSession(
                    userId = user.id,
                    businessId = anyEmployee.businessId,
                    role = "employee",
                    userName = user.fullName
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = user,
                        currentUserName = user.fullName,
                        currentRole = "employee",
                        currentBusinessId = anyEmployee.businessId,
                        destination = NavDestination.EmployeeSell
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Employee login failed: ${e.message}")
                }
            }
        }
    }

    /**
     * Role-aware routing after login — single function, not scattered if-branches.
     */
    fun routeAfterLogin(role: String, navController: androidx.navigation.NavController) {
        val destination = when (role) {
            "owner" -> NavDestination.Dashboard
            "employee" -> NavDestination.EmployeeSell
            "client" -> NavDestination.ClientDiscover
            "government_admin" -> NavDestination.GovernmentAdmin
            else -> NavDestination.Login
        }
        _uiState.update { it.copy(destination = destination) }
    }

    /**
     * Create a new Shopper/Client account.
     * Creates User (role client), saves session, routes to Client Discover.
     */
    fun createClientAccount(
        fullName: String,
        phoneOrEmail: String,
        password: String,
        language: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Check if account already exists
                val normalizedPhone = com.duka.app.data.repository.UserRepository.normalizePhoneOrEmail(phoneOrEmail)
                val existing = userRepository.getByPhoneOrEmail(normalizedPhone)
                if (existing != null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "An account with that number already exists")
                    }
                    return@launch
                }

                // Hash password with BCrypt
                val passwordHash = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt())

                // Create client user (no businessId — client is not tied to a specific business)
                val userId = userRepository.createUser(
                    User(
                        businessId = null,
                        fullName = fullName,
                        phoneOrEmail = normalizedPhone,
                        passwordHash = passwordHash,
                        role = "client"
                    )
                )

                val user = userRepository.getById(userId)!!

                // Save session
                sessionManager.saveSession(
                    userId = userId,
                    businessId = 0L,
                    role = "client",
                    userName = fullName
                )

                // Persist and apply the selected language
                sessionManager.saveLanguage(
                    com.duka.app.DukaApplication.languageTagFromName(language)
                )
                com.duka.app.DukaApplication.applyLocale(
                    com.duka.app.DukaApplication.languageTagFromName(language)
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = user,
                        currentUserName = fullName,
                        currentRole = "client",
                        currentBusinessId = 0L,
                        destination = NavDestination.ClientDiscover,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to create account: ${e.message}")
                }
            }
        }
    }

    /**
     * Create a new business owner account.
     * Creates User (role owner), Business, and saves session.
     */
    fun createBusinessAccount(
        ownerName: String,
        phoneOrEmail: String,
        password: String,
        businessName: String,
        businessType: String,
        employeeCount: Int,
        language: String,
        district: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Check if account already exists
                val existing = userRepository.getByPhoneOrEmail(phoneOrEmail.trim())
                if (existing != null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "An account with that number already exists")
                    }
                    return@launch
                }

                // Hash password with BCrypt
                val passwordHash = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt())

                // Normalize phoneOrEmail (trim + lowercase) — shared normalization
                // ensures signup and login use the same canonical form.
                // BUG FIX #1: Root cause was case-sensitivity mismatch between
                // signup insert and login query.
                val normalizedPhone = com.duka.app.data.repository.UserRepository.normalizePhoneOrEmail(phoneOrEmail)

                // Create business
                val businessId = businessRepository.createBusiness(
                    com.duka.app.data.local.entity.Business(
                        name = businessName.trim(),
                        type = businessType,
                        employeeCount = employeeCount,
                        language = language,
                        district = district
                    )
                )

                // Create owner user
                val userId = userRepository.createUser(
                    User(
                        businessId = businessId,
                        fullName = ownerName.trim(),
                        phoneOrEmail = normalizedPhone,
                        passwordHash = passwordHash,
                        role = "owner"
                    )
                )

                val user = userRepository.getById(userId)!!

                // Save session
                sessionManager.saveSession(
                    userId = userId,
                    businessId = businessId,
                    role = "owner",
                    userName = ownerName.trim()
                )

                // Persist and apply the selected language
                sessionManager.saveLanguage(
                    com.duka.app.DukaApplication.languageTagFromName(language)
                )
                com.duka.app.DukaApplication.applyLocale(
                    com.duka.app.DukaApplication.languageTagFromName(language)
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = user,
                        currentUserName = ownerName.trim(),
                        currentRole = "owner",
                        currentBusinessId = businessId,
                        destination = NavDestination.Dashboard,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to create account: ${e.message}")
                }
            }
        }
    }

    /**
     * Change the app language at runtime.
     * Persists the choice in DataStore and applies via AppCompatDelegate.
     */
    fun changeLanguage(displayName: String) {
        viewModelScope.launch {
            val tag = com.duka.app.DukaApplication.languageTagFromName(displayName)
            sessionManager.saveLanguage(tag)
            com.duka.app.DukaApplication.applyLocale(tag)
        }
    }

    /**
     * Logout — clears session and routes back to Login.
     */
    fun logout() {
        viewModelScope.launch {
            try {
                sessionManager.clearSession()
                _uiState.update {
                    SessionUiState(
                        isLoading = false,
                        destination = NavDestination.Login
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Logout failed: ${e.message}")
                }
            }
        }
    }

    /** Navigate to Company Signup. */
    fun goToSignup() {
        _uiState.update { it.copy(destination = NavDestination.CompanySignup) }
    }

    /** Navigate to Client Signup. */
    fun goToClientSignup() {
        _uiState.update { it.copy(destination = NavDestination.ClientSignup) }
    }

    /** Navigate to Employee Login. */
    fun goToEmployeeLogin() {
        _uiState.update { it.copy(destination = NavDestination.EmployeeLogin) }
    }

    /** Navigate to Login. */
    fun goToLogin() {
        _uiState.update { it.copy(destination = NavDestination.Login) }
    }

    /** Navigate to Dashboard (from signup/login). */
    fun goToDashboard() {
        _uiState.update { it.copy(destination = NavDestination.Dashboard) }
    }

    /** Navigate to Employee Sell. */
    fun goToEmployeeSell() {
        _uiState.update { it.copy(destination = NavDestination.EmployeeSell) }
    }

    /** Clear error message. */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
