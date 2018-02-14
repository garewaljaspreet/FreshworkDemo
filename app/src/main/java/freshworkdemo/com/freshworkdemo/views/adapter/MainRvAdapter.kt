package freshworkdemo.com.freshworkdemo.views.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import freshworkdemo.com.freshworkdemo.R
import freshworkdemo.com.freshworkdemo.model.BeansGifDataChild
import freshworkdemo.com.freshworkdemo.interfaces.DialogVisibilityListerner
import freshworkdemo.com.freshworkdemo.utilities.Utilities
import java.io.File
import java.io.FileOutputStream


/**
 * Adapter to show list of trending gifs
 * @author Jass
 * @version 1.0
 */
class MainRvAdapter(var userList: ArrayList<BeansGifDataChild>, val activity: Context, val dialogVisibilityListener: DialogVisibilityListerner) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 0
    private val VIEW_PROG = 1               //View type progress bar
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is GifViewHolder)
            holder?.bindItems(userList[position])
        else if (holder is ProgressViewHolder)
            holder.bindItems()

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.trending_item_layout, parent, false)
            vh = GifViewHolder(v, MyCustomClickListener(), activity)
            return vh
        } else if (viewType == VIEW_PROG) {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.footerview, parent, false)
            vh = ProgressViewHolder(v)
            return vh
        }
        return vh!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList.get(position).id.equals("")) VIEW_PROG else VIEW_ITEM
    }

    override fun getItemCount(): Int {
        return if (userList == null) 0 else userList.size
    }


    fun updateData(updatedData: ArrayList<BeansGifDataChild>) {
        userList = ArrayList()
        userList?.addAll(updatedData)
        notifyDataSetChanged()

    }

    inner class GifViewHolder(itemView: View, val myCustomClickListener: MyCustomClickListener, val activity: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: BeansGifDataChild) {

            val txtGifName: TextView = itemView.findViewById(R.id.txtGifName)
            val imgGif: ImageView = itemView.findViewById(R.id.imgGif)
            val imgFav: ImageView = itemView.findViewById(R.id.imgFav)
            imgFav.setOnClickListener(myCustomClickListener)
            myCustomClickListener.updatePosition(adapterPosition, imgFav)
            if (data.username.equals(""))
                txtGifName?.text = activity.getString(R.string.no_username)
            else
                txtGifName?.text = data.username
            Glide.with(activity)
                    .load(data.images.fixed_width_downsampled.url)
                    .asGif()
                    .crossFade()
                    .override(250, 250)
                    .into(imgGif)

            if (data.isFavourite == 0)
                imgFav.setBackgroundResource(R.drawable.ic_favorite_border_black_24px)
            else
                imgFav.setBackgroundResource(R.drawable.ic_favorite_black_24px)
        }

    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {

        }
    }

    /**
     * Custom class to handle favorite button click
     */
    inner class MyCustomClickListener : View.OnClickListener {
        private var position: Int = 0
        private var favImg: ImageView? = null
        fun updatePosition(position: Int, favImg: ImageView) {
            this.position = position
            this.favImg = favImg
        }

        override fun onClick(v: View?) {
            if (v?.getId() == favImg?.getId()) {
                if (dialogVisibilityListener.checkPermissionStatus()) {
                    if (userList.get(position).isFavourite == 0) {
                        if (Utilities.checkWIFI(activity)) {
                            AsyncTaskExample(userList.get(position).id, userList.get(position).images.fixed_width_downsampled.url).execute()
                            userList.get(position).isFavourite = 1
                            favImg?.setBackgroundResource(R.drawable.ic_favorite_black_24px)
                        } else
                            Toast.makeText(activity, activity.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                    } else {
                        showAlertWithTwoButton(position, favImg!!)
                    }
                } else {
                    dialogVisibilityListener.setUpPermission()
                }


            }
        }

    }

    /**
     * Shows delete confirmation dialog
     */
    private fun showAlertWithTwoButton(position: Int, favImg: ImageView) {
        val alertDilog = AlertDialog.Builder(activity).create()
        alertDilog.setTitle(activity.getString(R.string.confirm_delete))
        alertDilog.setMessage(activity.getString(R.string.dialog_messgae))

        alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", { dialogInterface, i ->
            userList.get(position).isFavourite = 0
            favImg?.setBackgroundResource(R.drawable.ic_favorite_border_black_24px)
            deleteFile(userList.get(position).id)
        })
        alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", { dialogInterface, i ->
        })

        alertDilog.show()
    }

    /**
     * Deletes specified file from device storage
     * @param name Name of file to be deleted
     */
    private fun deleteFile(name: String) {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/FreshworkGifs/" + name + ".gif")
        if (file.exists()) {
            file.delete()
       }
    }

    /**
     * Async task to download and save favorite gif in local storage
     */
    inner class AsyncTaskExample(name: String, url: String) : AsyncTask<String, String, String>() {
        val fileName: String? = name
        var urlInfo: String? = url
        var newFavGif: File? = null
        override fun onPreExecute() {
            super.onPreExecute()
            dialogVisibilityListener.showProgressBar(true)
        }

        override fun doInBackground(vararg p0: String?): String {
            val dir = File(Environment.getExternalStorageDirectory().toString() + "/FreshworkGifs")
            val bytes = Glide.with(activity)
                    .load(urlInfo)
                    .asGif()
                    .toBytes()
                    .into(250, 250)
                    .get()
            if (!dir.exists())
                dir.mkdir()
            newFavGif = File(dir, "$fileName.gif")
            if (!newFavGif!!.exists()) {
                val fileWriter = FileOutputStream(newFavGif)
                fileWriter.write(bytes)
                fileWriter.flush()
                fileWriter.close()
            }

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            dialogVisibilityListener.showProgressBar(false)
            Toast.makeText(activity, activity.getString(R.string.success_fav_download), Toast.LENGTH_SHORT).show();


        }
    }


}
