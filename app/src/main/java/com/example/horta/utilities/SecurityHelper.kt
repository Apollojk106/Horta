package com.example.horta.utilities

import at.favre.lib.crypto.bcrypt.BCrypt

object SecurityHelper {

    fun hashPassword(senha: String): String {
        return BCrypt.withDefaults().hashToString(12, senha.toCharArray())
    }

    fun verifyPassword(senha: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(senha.toCharArray(), hash).verified
    }
}