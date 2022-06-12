package com.github.duninvit.twittertokafkaservice.jasypt

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.iv.RandomIvGenerator
import org.junit.jupiter.api.Test

class TestJasypt {

    @Test
    fun generatePassword() {
        val target = ""
        val encryptor = StandardPBEStringEncryptor().apply {
            setPassword(System.getenv("JASYPT_PASSWORD"))
            setAlgorithm("PBEWITHHMACSHA512ANDAES_256")
            setIvGenerator(RandomIvGenerator())
        }
        val result = encryptor.encrypt(target)
        println(result)
        println(encryptor.decrypt(result))
    }
}
