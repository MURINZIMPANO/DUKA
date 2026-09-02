package com.duka.shared.domain

/**
 * Expect/actual for password hashing — platform-specific implementations.
 * Android uses BCrypt; iOS uses CommonCrypto.
 */
expect object PasswordHasher {
    fun hashPassword(password: String): String
    fun checkPassword(password: String, hash: String): Boolean
}
