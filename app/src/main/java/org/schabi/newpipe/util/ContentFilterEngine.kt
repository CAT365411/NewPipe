package org.schabi.newpipe.util

import android.content.Context

class ContentFilterEngine private constructor(context: Context) {

    fun isRussianContent(title: String, description: String = ""): Boolean {
        val fullText = "$title $description".trim()
        if (fullText.isEmpty()) return false

        // Détection des lettres spécifiques au russe (absentes de l'ukrainien)
        val russianRegex = Regex("[ЁёЪъЫыЭэ]")
        if (russianRegex.containsMatchIn(fullText)) {
            return true
        }

        // Mots-clés de sécurité
        val russianKeywords = listOf("русский", "россия", "новости рф", "путин", "кремль")
        val lowerText = fullText.lowercase()
        for (keyword in russianKeywords) {
            if (lowerText.contains(keyword)) {
                return true
            }
        }

        return false
    }

    companion object {
        @Volatile
        private var INSTANCE: ContentFilterEngine? = null

        fun getInstance(context: Context): ContentFilterEngine {
            return INSTANCE ?: synchronized(this) {
                val instance = ContentFilterEngine(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
