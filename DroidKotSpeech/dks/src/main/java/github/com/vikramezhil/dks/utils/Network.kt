package github.com.vikramezhil.dks.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Network Extensions
 * @author vikramezhil
 */

/**
 * Checks if the network is available
 * @receiver Context The application context
 * @return Boolean The network available status
 */
@Suppress("DEPRECATION")
fun Context.isNetworkAvailable(): Boolean {
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        } else {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}

