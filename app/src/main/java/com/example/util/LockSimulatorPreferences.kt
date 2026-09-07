package com.example.util

import android.content.Context
import android.content.SharedPreferences

data class SecurityConfig(
    val pin: String,
    val question: String,
    val answer: String,
    val ownerName: String,
    val hint: String
)

class LockSimulatorPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("raqeeb_lock_simulator_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PIN = "lock_pin"
        private const val KEY_QUESTION = "security_question"
        private const val KEY_ANSWER = "security_answer"
        private const val KEY_OWNER_NAME = "owner_name"
        private const val KEY_HINT = "pin_hint"

        val DEFAULT_QUESTIONS = listOf(
            "ما هو اسم أول مدرسة التحقت بها؟",
            "ما هو طراز أول هاتف ذكي امتلكته؟",
            "ما هي المدينة أو القرية التي ولدت بها؟",
            "ما هو اسم أعز أصدقاء طفولتك؟",
            "ما هو لون سيارتك الأولى أو دراجتك المفضلة؟",
            "ما هو رقم هاتفك الاحتياطي للطوارئ؟"
        )
    }

    fun getSecurityConfig(): SecurityConfig {
        val pin = prefs.getString(KEY_PIN, "2024") ?: "2024"
        val question = prefs.getString(KEY_QUESTION, DEFAULT_QUESTIONS[0]) ?: DEFAULT_QUESTIONS[0]
        val answer = prefs.getString(KEY_ANSWER, "اليرموك") ?: "اليرموك"
        val ownerName = prefs.getString(KEY_OWNER_NAME, "المهندس عبدالرقيب") ?: "المهندس عبدالرقيب"
        val hint = prefs.getString(KEY_HINT, "رمز مكون من 4 أرقام (سنة مهمة)") ?: "رمز مكون من 4 أرقام (سنة مهمة)"

        return SecurityConfig(
            pin = pin,
            question = question,
            answer = answer,
            ownerName = ownerName,
            hint = hint
        )
    }

    fun saveSecurityConfig(config: SecurityConfig) {
        prefs.edit()
            .putString(KEY_PIN, config.pin.trim())
            .putString(KEY_QUESTION, config.question.trim())
            .putString(KEY_ANSWER, config.answer.trim())
            .putString(KEY_OWNER_NAME, config.ownerName.trim())
            .putString(KEY_HINT, config.hint.trim())
            .apply()
    }

    /**
     * Normalizes Arabic text to ensure fair comparison
     * Handles alef variants, teh marbuta, and yeh/alef maksura
     */
    fun isAnswerValid(userAnswer: String, expectedAnswer: String): Boolean {
        val normalizedUser = normalizeArabic(userAnswer)
        val normalizedExpected = normalizeArabic(expectedAnswer)
        return normalizedUser.isNotEmpty() && (
            normalizedUser == normalizedExpected ||
            normalizedUser.contains(normalizedExpected) ||
            normalizedExpected.contains(normalizedUser)
        )
    }

    private fun normalizeArabic(text: String): String {
        return text.trim().lowercase()
            .replace("[\\s\\-_.]+".toRegex(), "")
            .replace("[أإآٱ]".toRegex(), "ا")
            .replace("ى", "ي")
            .replace("ة", "ه")
            .replace("[\u064B-\u065F]".toRegex(), "") // Remove tashkeel/diacritics
    }
}
