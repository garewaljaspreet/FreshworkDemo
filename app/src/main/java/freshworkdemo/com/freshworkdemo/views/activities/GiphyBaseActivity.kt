package freshworkdemo.com.freshworkdemo.views.activities

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import freshworkdemo.com.freshworkdemo.R
import freshworkdemo.com.freshworkdemo.views.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_giphy_base.*
import java.io.File

/**
 * This is base class to show Trending Fragment and Favorite Fragment
 * @author Jass
 * @version 1.0
 *
 */
class GiphyBaseActivity : AppCompatActivity() {
    private val favGifFolder:String?="/FreshworkGifs/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giphy_base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar))
        }

        deleteExistingFiles()
        val viewPagerAdapter=ViewPagerAdapter(this,supportFragmentManager)
        viewPager.adapter=viewPagerAdapter
        slidingTabs.setupWithViewPager(viewPager);

    }

    /**
     * Method to delete existing favorite gifs at start of application
     */
    fun deleteExistingFiles(){
        val dir = File(Environment.getExternalStorageDirectory().toString() + favGifFolder)
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children!!.indices) {
                File(dir, children[i]).delete()
            }
        }
    }
}


