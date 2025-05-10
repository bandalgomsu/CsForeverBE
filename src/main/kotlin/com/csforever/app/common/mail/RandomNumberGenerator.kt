package com.csforever.app.common.mail

class RandomNumberGenerator {

    companion object {
        fun generateRandomNumber(length: Int): String {
            val randomNumber = StringBuilder()

            for (i in 0 until length) {
                val random = (0..9).random()
                randomNumber.append(random)
            }

            return randomNumber.toString()
        }
    }
}