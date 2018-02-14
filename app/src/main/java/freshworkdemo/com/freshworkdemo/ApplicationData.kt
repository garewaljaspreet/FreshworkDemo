package freshworkdemo.com.freshworkdemo

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.preference.PreferenceManager

import freshworkdemo.com.freshworkdemo.network.NetworkService

/**
 * Used to create sigleton for network calls
 * @author Jass
 * @version 1.0
 */
class ApplicationData : Application() {
    var networkService:NetworkService?=null
    override fun onCreate() {
        super.onCreate()
        instance = this
        networkService = NetworkService("")
    }

    companion object {
        lateinit var instance: ApplicationData
            private set
    }


}
