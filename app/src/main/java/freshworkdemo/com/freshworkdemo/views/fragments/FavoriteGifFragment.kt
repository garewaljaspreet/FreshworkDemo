package freshworkdemo.com.freshworkdemo.views.fragments

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.view.animation.AnimationUtils
import freshworkdemo.com.freshworkdemo.R
import freshworkdemo.com.freshworkdemo.views.adapter.FavoriteRvAdapter
import kotlinx.android.synthetic.main.fragment_favorite_gif.*
import java.io.File


/**
 * This fragment is used to show Favorite Gifs
 * @author Jass
 * @version 1.0
 *
 */
class FavoriteGifFragment : Fragment() {
    private val favGifFolder:String?="/FreshworkGifs"
    private var isViewInit:Boolean? = false
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_favorite_gif,
                container, false)


    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isViewInit!!)
        {
            fetchFavoriteGifs()
        }

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInit=true

    }

    /**
     * Fetches gifs stored in device locally
     */
    fun fetchFavoriteGifs()
    {
        val directory = File(Environment.getExternalStorageDirectory().toString() + favGifFolder)
        val files = directory.listFiles()
        val users = ArrayList<String>()
        if(files!=null && files.size>0) {
            txtNoData?.visibility=View.GONE
            for (i in files!!.indices) {
                users.add(files[i].absolutePath)
            }
            rvFavGifList.layoutManager = GridLayoutManager(activity, 3)
            var adapter = FavoriteRvAdapter(users, context)
            rvFavGifList.adapter = adapter
        }else{
            txtNoData?.visibility=View.VISIBLE
        }

    }

}