package freshworkdemo.com.freshworkdemo.views.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.support.v7.widget.SearchView
import freshworkdemo.com.freshworkdemo.ApplicationData
import freshworkdemo.com.freshworkdemo.R
import freshworkdemo.com.freshworkdemo.views.adapter.MainRvAdapter
import freshworkdemo.com.freshworkdemo.interfaces.DialogVisibilityListerner
import freshworkdemo.com.freshworkdemo.model.BeansGifDataChild
import freshworkdemo.com.freshworkdemo.network.NetworkService
import freshworkdemo.com.freshworkdemo.presenter.TrendingGifPresenter
import freshworkdemo.com.freshworkdemo.presenter.TrendingGifpresenterInteractor
import kotlinx.android.synthetic.main.fragment_trending_gif.*
import android.support.v7.widget.RecyclerView
import android.view.View.OnAttachStateChangeListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import freshworkdemo.com.freshworkdemo.model.BeansChildImageType
import freshworkdemo.com.freshworkdemo.model.BeansChildImages
import freshworkdemo.com.freshworkdemo.utilities.Utilities


/**
 * This fragment is used to show Trending Gifs
 * Also used to search Gifs
 * @author Jass
 * @version 1.0
 *
 */
class TrendingGifFragment : Fragment(), DialogVisibilityListerner {
    private var service: NetworkService? = null
    private var presenter: TrendingGifpresenterInteractor? = null
    private var gifData: ArrayList<BeansGifDataChild>? = null       //List to store trending gifs
    private var searchData: ArrayList<BeansGifDataChild>? = null    //List to store searched data
    private var adapter: MainRvAdapter? = null
    private var limit: Int = 25                                     //Number of items to be fetched during pagination
    private var offset: Int = 0
    private var lastVisibleItemPosition: Int? = 0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val RECORD_REQUEST_CODE = 101
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_trending_gif,
                container, false)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        rvMainGifList.layoutManager = linearLayoutManager

        if (Utilities.checkWIFI(activity)) {
            presenter?.getTrendingGifs(offset, limit)
            showProgressBar(true)
            progressBar?.visibility = View.VISIBLE
        } else {
            Utilities.showMessage(getString(R.string.no_network), coordinatorLayout!!)
            showProgressBar(false)
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = ApplicationData.instance.networkService
        presenter = TrendingGifPresenter(this, service)
        setHasOptionsMenu(true)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity.menuInflater.inflate(R.menu.menu_search, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searching(searchView)
        super.onPrepareOptionsMenu(menu)
    }

    private fun setRecyclerViewScrollListener(isRemoveProgress: Boolean) {
        if (isRemoveProgress) {
            gifData?.removeAt(gifData?.size!! - 1)
            adapter?.notifyItemRemoved(gifData?.size!! - 1)
        }

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                if (totalItemCount == lastVisibleItemPosition!! + 1) {
                    offset = offset + limit
                    if (Utilities.checkWIFI(activity)) {
                        presenter?.getTrendingGifs(offset, limit)
                        rvMainGifList.removeOnScrollListener(scrollListener)


                    } else {
                        Utilities.showMessage(getString(R.string.no_network), coordinatorLayout!!)
                    }
                }
            }
        }
        rvMainGifList.addOnScrollListener(scrollListener)
    }


    private fun searching(search: SearchView) {

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                rvMainGifList.removeOnScrollListener(scrollListener)
                if (Utilities.checkWIFI(activity))
                {
                    showProgressBar(true)
                    presenter?.searchGifs(query)
                }
                else
                    Utilities.showMessage(getString(R.string.no_network), coordinatorLayout!!)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

        })


        search.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {

            override fun onViewDetachedFromWindow(arg0: View) {
                adapter?.updateData(gifData!!)
                if(gifData!=null)
                    rvMainGifList.addOnScrollListener(scrollListener)
            }

            override fun onViewAttachedToWindow(arg0: View) {

            }
        })

    }

    /**
     * Sets adapter with trending gifs retrieved from server
     * @param gifDataList List of trending gifs
     */
    fun onTrendingResult(gifDataList: ArrayList<BeansGifDataChild>) {


        if ((gifData != null) && (gifData?.size!! > 0)) {
            setRecyclerViewScrollListener(true)
            this.gifData?.addAll(gifDataList)
            var mListK: BeansChildImageType = BeansChildImageType("")
            var mListN: BeansChildImages = BeansChildImages(mListK)
            var mList: BeansGifDataChild = BeansGifDataChild("", "", mListN, 0)
            gifData?.add(mList)
            adapter?.updateData(gifData!!)


        } else {
            showProgressBar(false)
            setRecyclerViewScrollListener(false)
            gifData = ArrayList()
            this.gifData?.addAll(gifDataList)
            var mListK: BeansChildImageType = BeansChildImageType("")
            var mListN: BeansChildImages = BeansChildImages(mListK)
            var mList: BeansGifDataChild = BeansGifDataChild("", "", mListN, 0)
            gifData?.add(mList)
            adapter = MainRvAdapter(gifData!!, context, this)
            runLayoutAnimation()
            rvMainGifList.adapter = adapter

        }

    }

    /**
     * Method to show or hide progress bar
     * @param isShowing boolean to show or hide progress bar
     */
    override fun showProgressBar(isShowing: Boolean) {
        if (isShowing)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE
    }

    /**
     * Sets adapter with searched gifs
     * @param searchDataRes List of searched gifs
     */
    fun onSearchResult(searchDataRes: ArrayList<BeansGifDataChild>) {
        showProgressBar(false)
        this.searchData = searchDataRes
        if(searchData?.size==0)
        {
            Toast.makeText(activity, activity.getString(R.string.no_result), Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter?.updateData(searchData!!)
            rvMainGifList.adapter = adapter
        }

    }

    /**
     * Method to check storage permissions at runtime
     */
    override fun checkPermissionStatus(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)

            return permission == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    /**
     * Shows dialog to setup storage permissions
     */
    override fun setUpPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Permission to access the storage is required for this app to save Gif's.")
                    .setTitle("Permission required")

            builder.setPositiveButton("OK"
            ) { dialog, id ->
                makeRequest()
            }

            val dialog = builder.create()
            dialog.show()
        } else {
            makeRequest()
        }
    }


    private fun makeRequest() {
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE)
    }

    //Callback to receive permissions result
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.e("deny", "Permission has been denied by user")
                } else {
                    Log.e("granted", "Permission has been granted by user")
                }
            }
        }
    }

    //Handles recyclerview item animations
    private fun runLayoutAnimation() {
        rvMainGifList.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(context, R.anim.layout_list_animation));
        rvMainGifList.scheduleLayoutAnimation();
    }

}