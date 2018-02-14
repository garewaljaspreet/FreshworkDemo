package freshworkdemo.com.freshworkdemo.utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

/**
 * Used to check internet connection and show snackbar messages
 * @author Jass
 * @version 1.0
 */

object Utilities {

    /**
     * Checks wifi/Data connection
     * @param context Reference of view
     * @return boolean if internet is available or not
     */
    fun checkWIFI(context: Context): Boolean {

        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            true
        } else if (netInfo != null && (netInfo.state == NetworkInfo.State.DISCONNECTED
                || netInfo.state == NetworkInfo.State.DISCONNECTING
                || netInfo.state == NetworkInfo.State.SUSPENDED || netInfo
                .state == NetworkInfo.State.UNKNOWN)) {
            false
        } else {
            false
        }
    }

    /**
     * Display Toast Message
     */
    fun showMessage(message: String, view: CoordinatorLayout) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}


