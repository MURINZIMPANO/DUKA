package com.duka.shared.viewmodel

import com.duka.shared.domain.currentTimeMillis
import com.duka.shared.data.repository.BusinessRepository
import com.duka.shared.data.repository.EmployeeRepository
import com.duka.shared.data.repository.UserRepository
import com.duka.shared.data.repository.normalizePhoneOrEmail
import com.duka.shared.data.session.SessionManager
import com.duka.shared.domain.PasswordHasher
import com.duka.shared.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * SessionViewModel — Shared ViewModel using plain coroutines.
 * Platform apps can wrap this as needed.
 */
class SessionViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository,
    private val employeeRepository: EmployeeRepository
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    sealed class NavDestination {
        data object Splash : NavDestination()
        data object Login : NavDestination()
        data object CompanySignup : NavDestination()
        data object ClientSignup : NavDestination()
        data object EmployeeLogin : NavDestination()
        data object Dashboard : NavDestination()
        data object EmployeeSell : NavDestination()
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
        scope.launch {
            try {
                val role = sessionManager.currentRole.first()
                if (role != null) {
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
                    _uiState.update { it.copy(isLoading = false, destination = NavDestination.Login) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to check session: ${e.message}", destination = NavDestination.Login) }
            }
        }
    }

    fun login(phoneOrEmail: String, password: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getByPhoneOrEmail(phoneOrEmail)
                if (user == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Account doesn't exist.") }
                    return@launch
                }
                if (!PasswordHasher.checkPassword(password, user.passwordHash)) {
                    _uiState.update { it.copy(isLoading = false, error = "Incorrect password") }
                    return@launch
                }
                val businessId = user.businessId ?: 0L
                sessionManager.saveSession(user.id, businessId, user.role, user.fullName)
                _uiState.update {
                    it.copy(
                        isLoading = false, currentUser = user, currentUserName = user.fullName,
                        currentRole = user.role, currentBusinessId = businessId,
                        destination = when (user.role) {
                            "owner" -> NavDestination.Dashboard
                            "employee" -> NavDestination.EmployeeSell
                            "client" -> NavDestination.ClientDiscover
                            else -> NavDestination.Dashboard
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Login failed: ${e.message}") }
            }
        }
    }

    fun employeeLogin(businessCode: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val code = businessCode.trim()
                if (code.isEmpty()) { _uiState.update { it.copy(isLoading = false, error = "Enter your code") }; return@launch }
                val emp = employeeRepository.getByCodeAny(code)
                if (emp == null) { _uiState.update { it.copy(isLoading = false, error = "No employee found") }; return@launch }
                if (!emp.isActive) { _uiState.update { it.copy(isLoading = false, error = "Account deactivated") }; return@launch }
                val user = userRepository.getById(emp.userId) ?: run { _uiState.update { it.copy(isLoading = false, error = "Account not found") }; return@launch }
                employeeRepository.updateLastSeenAt(emp.id, currentTimeMillis())
                sessionManager.saveSession(user.id, emp.businessId, "employee", user.fullName)
                _uiState.update { it.copy(isLoading = false, currentUser = user, currentUserName = user.fullName, currentRole = "employee", currentBusinessId = emp.businessId, destination = NavDestination.EmployeeSell) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Login failed: ${e.message}") }
            }
        }
    }

    fun createBusinessAccount(ownerName: String, phoneOrEmail: String, password: String, businessName: String, businessType: String, employeeCount: Int, language: String, district: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val normalizedPhone = normalizePhoneOrEmail(phoneOrEmail)
                val existing = userRepository.getByPhoneOrEmail(normalizedPhone)
                if (existing != null) { _uiState.update { it.copy(isLoading = false, error = "Account already exists") }; return@launch }
                val passwordHash = PasswordHasher.hashPassword(password)
                val bizId = businessRepository.createBusiness(com.duka.shared.domain.Business(name = businessName.trim(), type = businessType, employeeCount = employeeCount, language = language, district = district))
                val userId = userRepository.createUser(User(businessId = bizId, fullName = ownerName.trim(), phoneOrEmail = normalizedPhone, passwordHash = passwordHash, role = "owner"))
                val user = userRepository.getById(userId)!!
                sessionManager.saveSession(userId, bizId, "owner", ownerName.trim())
                _uiState.update { it.copy(isLoading = false, currentUser = user, currentUserName = ownerName.trim(), currentRole = "owner", currentBusinessId = bizId, destination = NavDestination.Dashboard, error = null) }
            } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = "Failed: ${e.message}") } }
        }
    }

    fun createClientAccount(fullName: String, phoneOrEmail: String, password: String, language: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val normalizedPhone = normalizePhoneOrEmail(phoneOrEmail)
                val existing = userRepository.getByPhoneOrEmail(normalizedPhone)
                if (existing != null) { _uiState.update { it.copy(isLoading = false, error = "Account already exists") }; return@launch }
                val passwordHash = PasswordHasher.hashPassword(password)
                val userId = userRepository.createUser(User(businessId = null, fullName = fullName, phoneOrEmail = normalizedPhone, passwordHash = passwordHash, role = "client"))
                val user = userRepository.getById(userId)!!
                sessionManager.saveSession(userId, 0L, "client", fullName)
                _uiState.update { it.copy(isLoading = false, currentUser = user, currentUserName = fullName, currentRole = "client", currentBusinessId = 0L, destination = NavDestination.ClientDiscover, error = null) }
            } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = "Failed: ${e.message}") } }
        }
    }

    fun logout() {
        scope.launch {
            sessionManager.clearSession()
            _uiState.value = SessionUiState(isLoading = false, destination = NavDestination.Login)
        }
    }

    fun goToSignup() { _uiState.update { it.copy(destination = NavDestination.CompanySignup) } }
    fun goToClientSignup() { _uiState.update { it.copy(destination = NavDestination.ClientSignup) } }
    fun goToEmployeeLogin() { _uiState.update { it.copy(destination = NavDestination.EmployeeLogin) } }
    fun goToLogin() { _uiState.update { it.copy(destination = NavDestination.Login) } }
    fun clearError() { _uiState.update { it.copy(error = null) } }
}
