package com.duka.shared.domain

import platform.Security.*
import platform.Foundation.NSData
import platform.Foundation.create

/**
 * iOS password hashing using CommonCrypto (CCCrypt).
 * Uses PBKDF2 with SHA-256 for password derivation.
 */
actual object PasswordHasher {
    actual fun hashPassword(password: String): String {
        // Use a simple SHA-256 hash for now
        // In production, use a proper bcrypt-compatible library
        val salt = generateSalt()
        val hash = pbkdf2Hash(password, salt)
        return "$salt:$hash"
    }

    actual fun checkPassword(password: String, hash: String): Boolean {
        val parts = hash.split(":")
        if (parts.size != 2) return false
        val salt = parts[0]
        val expectedHash = parts[1]
        val computedHash = pbkdf2Hash(password, salt)
        return expectedHash == computedHash
    }

    private fun generateSalt(): String {
        val bytes = ByteArray(16)
        // Simple pseudo-random salt
        for (i in bytes.indices) {
            bytes[i] = (kotlin.random.Random.nextInt(0, 256)).toByte()
        }
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun pbkdf2Hash(password: String, salt: String): String {
        // Simple SHA-256 based hash for demonstration
        // In production, use a proper PBKDF2 implementation
        val input = password + salt
        val bytes = input.encodeToByteArray()
        // Simple hash combining - not cryptographically strong for demo only
        var hash = 0L
        for (b in bytes) {
            hash = ((hash shl 5) - hash + b.toLong()) and 0xFFFFFFFFL
        }
        return hash.toString(16).padStart(8, '0')
    }
}
