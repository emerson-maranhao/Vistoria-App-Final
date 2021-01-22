package br.gov.am.detran.appvistoria.until

import android.os.CountDownTimer
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import br.gov.am.detran.appvistoria.presentation.ui.home.TAG
import br.gov.am.detran.appvistoria.session.UserPreferences
import java.util.concurrent.atomic.AtomicBoolean

fun FragmentActivity.addOnBackPressedCallbackWithInterval(
    millisToBack: Long,
    firstPressed: () -> Unit
) {
    onBackPressedDispatcher.addCallback(
        this, ActivityUtils.getOnBackPressedCallbackWithInterval(
            this,
            millisToBack,
            firstPressed
        )
    )
}

object ActivityUtils {
    fun getOnBackPressedCallbackWithInterval(
        fragmentActivity: FragmentActivity,
        millisToBack: Long,
        firstPressed: () -> Unit
    ) =
        object : OnBackPressedCallback(true) {
            var pressed = AtomicBoolean(false)
            val counter = object : CountDownTimer(millisToBack, millisToBack) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    if (pressed.get()) {
                        pressed.set(false)
                    }
                }
            }

            override fun handleOnBackPressed() {
                if (pressed.get()) disableAndPressOnBack()
                else {
                    pressed.set(true)
                    firstPressed()
                }
                counter.start()
            }

            private fun disableAndPressOnBack() {
                isEnabled = false
                fragmentActivity.onBackPressed()
                Log.i(TAG, "Finish")
                UserPreferences.clear(fragmentActivity)
                fragmentActivity.finishAffinity()

            }
        }
}