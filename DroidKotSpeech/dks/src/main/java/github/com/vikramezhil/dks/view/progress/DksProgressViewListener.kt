package github.com.vikramezhil.dks.view.progress

interface DksProgressViewListener {
    /**
     * Sends an update when the positive button is clicked
     */
    fun onClickedPositive()

    /**
     * Sends an update when the neutral button is clicked
     */
    fun onClickedNeutral()

    /**
     * Sends an update when the negative button is clicked
     */
    fun onClickedNegative()
}