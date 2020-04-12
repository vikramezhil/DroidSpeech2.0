package github.com.vikramezhil.dks.view.alert

interface DksFullScreenDialogListener {
    /**
     * Sends an update when positive button is clicked
     * @param result String The result
     */
    fun onSuccess(result: String)

    /**
     * Sends an update when neutral button is clicked
     */
    fun onRetry()

    /**
     * Sends an update when negative button is clicked
     */
    fun onClose()

    /**
     * Sends an update when the progress view is paused
     */
    fun onPause()

    /**
     * Sends an update when the progress view is resumed
     */
    fun onResume()

    /**
     * Sends an update on the microphone permission status
     * @param statusGiven Boolean The microphone permission given status
     * @param restartSpeechOps Boolean The restart speech operations status
     */
    fun onMicrophonePermissionStatus(statusGiven: Boolean, restartSpeechOps: Boolean)
}