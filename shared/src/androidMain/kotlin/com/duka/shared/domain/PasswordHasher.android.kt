package com.duka.shared.domain

import org.mindrot.jbcrypt.BCrypt

actual object PasswordHasher {
    actual fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    actual fun checkPassword(password: String, hash: String): Boolean {
        return try {
            BCrypt.checkpw(password, hash)
        } catch (e: Exception) {
            false
        }
    }
}
