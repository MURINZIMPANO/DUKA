package com.duka.employee.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duka.app.data.local.entity.Employee
import com.duka.app.data.local.entity.User
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.EmployeeRepository
import com.duka.app.data.repository.UserRepository
import com.duka.app.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Employee Management feature (Owner portal).
 * Manages the list of employees, adding new ones, editing roles, and deactivation.
 */
@HiltViewModel
class EmployeeManagementViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class EmployeeListState(
        val employees: List<Employee> = emptyList(),
        val activeCount: Int = 0,
        val isLoading: Boolean = true
    )

    data class AddEmployeeState(
        val name: String = "",
        val role: String = "cashier",
        val generatedCode: String = "",
        val isSubmitting: Boolean = false,
        val showSuccess: Boolean = false,
        val successCode: String = "",
        val error: String? = null
    )

    private val _listState = MutableStateFlow(EmployeeListState())
    val listState: StateFlow<EmployeeListState> = _listState.asStateFlow()

    private val _addState = MutableStateFlow(AddEmployeeState())
    val addState: StateFlow<AddEmployeeState> = _addState.asStateFlow()

    init {
        loadEmployees()
        // Initialize the generated code
        _addState.update { it.copy(generatedCode = generateCode()) }
    }

    private fun loadEmployees() {
        viewModelScope.launch {
            val businessId = sessionManager.getBusinessId()
            if (businessId <= 0) {
                _listState.update { it.copy(isLoading = false) }
                return@launch
            }
            employeeRepository.getEmployeesByBusiness(businessId).collect { employees ->
                _listState.update {
                    it.copy(
                        employees = employees,
                        activeCount = employees.count { e -> e.isActive },
                        isLoading = false
                    )
                }
            }
        }
    }

    /** Generate a random 6-character alphanumeric code. */
    private fun generateCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // no ambiguous chars (0/O/1/I)
        return (1..6).map { chars.random() }.joinToString("")
    }

    /** Regenerate the employee code (EM2). */
    fun regenerateCode() {
        _addState.update { it.copy(generatedCode = generateCode()) }
    }

    /** Update the name field (EM2). */
    fun updateName(name: String) {
        _addState.update { it.copy(name = name) }
    }

    /** Update the role field (EM2). */
    fun updateRole(role: String) {
        _addState.update { it.copy(role = role) }
    }

    /**
     * Add a new employee (EM2).
     * 1. Create Employee row
     * 2. Create User row (role = EMPLOYEE, no email/password — employees log in by code)
     * 3. Link Employee.userId to the created User.id
     * 4. Show success bottom sheet
     */
    fun addEmployee() {
        viewModelScope.launch {
            val state = _addState.value
            if (state.name.isBlank()) {
                _addState.update { it.copy(error = "Please enter a name") }
                return@launch
            }

            _addState.update { it.copy(isSubmitting = true, error = null) }

            try {
                val businessId = sessionManager.getBusinessId()

                // Check code uniqueness within this business
                val existing = employeeRepository.getByCode(businessId, state.generatedCode)
                if (existing != null) {
                    // Regenerate and retry (extremely unlikely with 6 chars but safe)
                    val newCode = generateCode()
                    _addState.update { it.copy(generatedCode = newCode) }
                    return@launch
                }

                // Create User row for this employee
                // Employees log in by code, not email/password — this is intentional
                val userId = userRepository.createUser(
                    User(
                        businessId = businessId,
                        fullName = state.name.trim(),
                        phoneOrEmail = "", // employees don't have email/phone login
                        passwordHash = "",
                        role = "employee"
                    )
                )

                // Create Employee row linked to the User
                val employeeId = employeeRepository.addEmployee(
                    Employee(
                        businessId = businessId,
                        userId = userId,
                        code = state.generatedCode,
                        name = state.name.trim(),
                        role = state.role,
                        isActive = true,
                        joinedAt = System.currentTimeMillis(),
                        lastSeenAt = null
                    )
                )

                _addState.update {
                    it.copy(
                        isSubmitting = false,
                        showSuccess = true,
                        successCode = state.generatedCode
                    )
                }
            } catch (e: Exception) {
                _addState.update {
                    it.copy(isSubmitting = false, error = "Failed to add employee: ${e.message}")
                }
            }
        }
    }

    /** Dismiss the success sheet and reset the add form (EM2). */
    fun dismissAddSuccess() {
        _addState.update {
            AddEmployeeState(generatedCode = generateCode())
        }
    }

    /** Edit employee role (EM3). */
    fun editEmployeeRole(employeeId: Long, newRole: String) {
        viewModelScope.launch {
            employeeRepository.updateRole(employeeId, newRole)
        }
    }

    /** Toggle active status (deactivate/reactivate). */
    fun toggleActive(employeeId: Long, isActive: Boolean) {
        viewModelScope.launch {
            employeeRepository.updateActive(employeeId, isActive)
        }
    }

    /** Permanently remove an employee. */
    fun removeEmployee(employeeId: Long) {
        viewModelScope.launch {
            employeeRepository.deleteById(employeeId)
        }
    }
}
