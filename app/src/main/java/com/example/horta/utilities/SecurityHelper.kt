package com.example.horta.utilities

import at.favre.lib.crypto.bcrypt.BCrypt

object SecurityHelper {

    // Gerar hash da senha
    fun hashPassword(senha: String): String {
        return BCrypt.withDefaults().hashToString(12, senha.toCharArray())
    }

    // Verificar se a senha corresponde ao hash
    fun verifyPassword(senha: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(senha.toCharArray(), hash).verified
    }
}