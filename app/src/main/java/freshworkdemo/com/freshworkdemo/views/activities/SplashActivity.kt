package freshworkdemo.com.freshworkdemo.views.activities


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import freshworkdemo.com.freshworkdemo.R


/**
 * This is base class to show splash screen
 * @author Jass
 * @version 1.0
 */

class SplashActivity : AppCompatActivity() {
    private var counter: CountDownTimer? = null
    private val DEFAULT_TICK_TIME_MILISECONDS = 1000
    private val DEFAULT_FINISH_TIME_MILISECONDS = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideTitle()
        counter = object : CountDownTimer(DEFAULT_FINISH_TIME_MILISECONDS.toLong(), DEFAULT_TICK_TIME_MILISECONDS.toLong()) {
            override fun onTick(l: Long) {}

            override fun onFinish() {
                val intent= Intent(applicationContext, GiphyBaseActivity::class.java);
                applicationContext.startActivity(intent)
                finish()
            }
        }
        counter!!.start()
    }

    //Hides toolbar
    private fun hideTitle() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }
}
