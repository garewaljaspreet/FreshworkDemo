package freshworkdemo.com.freshworkdemo.views.adapter


import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import freshworkdemo.com.freshworkdemo.R
import freshworkdemo.com.freshworkdemo.views.fragments.FavoriteGifFragment
import freshworkdemo.com.freshworkdemo.views.fragments.TrendingGifFragment

/**
 * Adapter to show fragment horizontal scrolling
 * @author Jass
 * @version 1.0
 */

class ViewPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    // This determines the fragment for each tab
    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            TrendingGifFragment()
        } else if (position == 1) {
            FavoriteGifFragment()
        } else {
            TrendingGifFragment()
        }
    }

    // This determines the number of tabs
    override fun getCount(): Int {
        return 2
    }

    // This determines the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        when (position) {
            0 -> return mContext.getString(R.string.trending)
            1 -> return mContext.getString(R.string.favorite)

            else -> return null
        }
    }

}
