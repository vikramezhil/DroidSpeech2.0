package github.com.vikramezhil.dks.view.progress

interface ProgressViewListener {

    fun onClickedPositive(message: String)

    fun onClickedNeutral()

    fun onClickedNegative()
}