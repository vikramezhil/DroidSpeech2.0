package github.com.vikramezhil.dks.view.alert

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import github.com.vikramezhil.dks.R
import github.com.vikramezhil.dks.view.progress.DksProgressView
import github.com.vikramezhil.dks.view.progress.DksProgressViewListener
import kotlinx.android.synthetic.main.layout_progress_view.view.*

/**
 * Dks Full Screen Dialog
 * @author vikramezhil
 */

data class DksLiveObservers(val speechResult: MutableLiveData<String>, val speechFrequency: MutableLiveData<Float>,
                            val oneStepVerify: MutableLiveData<Boolean>, val resetButtons : MutableLiveData<Boolean>,
                            val ejectView: MutableLiveData<Boolean>)

class DksFullScreenDialog(private val dksProgressViewLayout: Int, private val manager: FragmentManager, private val observers: DksLiveObservers, private val listener: DksFullScreenDialogListener): DialogFragment() {
    companion object {
        const val TAG = "FullScreenDialog"
        const val PERMISSION_REQUEST_CODE = 1588
    }

    private var permissionWindowOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DksFullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogView = inflater.inflate(dksProgressViewLayout, container, false)
        if (dialogView is DksProgressView) {
            val defaultProgressTxt = dialogView.tv_progress_message.text.toString()
            dialogView.setProgressViewListener(object: DksProgressViewListener {
                override fun onClickedPositive() {
                    listener.onSuccess(dialogView.tv_progress_message.text.toString())
                }

                override fun onClickedNeutral() {
                    listener.onRetry()
                }

                override fun onClickedNegative() {
                    listener.onClose()
                    removeFragmentFromCurrentStack(true)
                }
            })

            observers.speechResult.observe(this, Observer { result ->
                if (result.isEmpty()) {
                    dialogView.setProgressMessage(defaultProgressTxt)
                } else {
                    dialogView.setProgressMessage(result)
                }
            })

            observers.speechFrequency.observe(this, Observer { frequency ->
                dialogView.setProgressBallFrequency(frequency)
            })

            observers.oneStepVerify.observe(this, Observer { trigger ->
                if (trigger) {
                    dialogView.setPositiveButtonVisibility(View.VISIBLE)
                    dialogView.setNeutralButtonVisibility(View.VISIBLE)
                }
            })

            observers.resetButtons.observe(this, Observer { reset ->
                if (reset) {
                    dialogView.setPositiveButtonVisibility(View.GONE)
                    dialogView.setNeutralButtonVisibility(View.GONE)
                    dialogView.setNegativeButtonVisibility(View.GONE)
                }
            })

            observers.ejectView.observe(this, Observer { eject ->
                if (eject) {
                    listener.onClose()
                    removeFragmentFromCurrentStack(true)
                }
            })
        }

        return dialogView
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Requesting for audio permissions
        requestForAudioPermission()
    }

    override fun onPause() {
        super.onPause()

        if (permissionWindowOpen) return
        listener.onPause()
    }

    override fun onResume() {
        super.onResume()

        if (permissionWindowOpen) return
        listener.onResume()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        listener.onClose()
        removeFragmentFromCurrentStack(false)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        listener.onClose()
        removeFragmentFromCurrentStack(false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionWindowOpen = false
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        // Permission not granted
                        listener.onMicrophonePermissionStatus(statusGiven = false, restartSpeechOps = false)
                        return
                    }
                }
            }
        }

        // Permission granted
        listener.onMicrophonePermissionStatus(statusGiven = true, restartSpeechOps = true)
    }

    /**
     * Requests for audio permission
     */
    private fun requestForAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionWindowOpen = true
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST_CODE)
        } else {
            listener.onMicrophonePermissionStatus(statusGiven = true, restartSpeechOps = false)
        }
    }

    /**
     * Removes the fragment from the current stack
     * @param manualDismiss Boolean The manual dismiss status
     */
    private fun removeFragmentFromCurrentStack(manualDismiss: Boolean) {
        val pendingFragment = manager.findFragmentByTag(TAG)
        if (pendingFragment != null) {
            val transaction = manager.beginTransaction()
            transaction.remove(pendingFragment).commit()
        }

        if (manualDismiss) {
            dismiss()
        }
    }
}