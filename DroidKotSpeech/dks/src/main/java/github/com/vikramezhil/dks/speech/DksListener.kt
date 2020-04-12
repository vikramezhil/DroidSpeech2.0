package github.com.vikramezhil.dks.speech

interface DksListener {
    /**
     * Sends an update on the live speech result
     * @param liveSpeechResult String The live speech result
     */
    fun onDksLiveSpeechResult(liveSpeechResult: String)

    /**
     * Sends an update on the final speech result
     * @param speechResult String The final speech result
     */
    fun onDksFinalSpeechResult(speechResult: String)

    /**
     * Sends an update on the speech frequency
     * @param frequency String The speech frequency
     */
    fun onDksLiveSpeechFrequency(frequency: Float)

    /**
     * Sends an update when the languages are available
     * @param defaultLanguage String? The default language
     * @param supportedLanguages ArrayList<String>? The supported languages
     */
    fun onDksLanguagesAvailable(defaultLanguage: String?, supportedLanguages: ArrayList<String>?)

    /**
     * Sends an update if there is an error
     * @param errMsg String The error message
     */
    fun onDksSpeechError(errMsg: String)
}