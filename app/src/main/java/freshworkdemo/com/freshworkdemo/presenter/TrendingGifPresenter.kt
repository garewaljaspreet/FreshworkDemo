package freshworkdemo.com.freshworkdemo.presenter

import freshworkdemo.com.freshworkdemo.model.BeanGifMain
import freshworkdemo.com.freshworkdemo.network.NetworkService
import freshworkdemo.com.freshworkdemo.views.fragments.TrendingGifFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Presenter to fetch trending gifs from server
 * Also used to search gifs
 * @author Jass
 * @version 1.0
 */
class TrendingGifPresenter(private val view: TrendingGifFragment, internal var service: NetworkService?) : TrendingGifpresenterInteractor {

    override fun getTrendingGifs(page: Int, size: Int): BeanGifMain? {

        val trendingGifData = service?.api?.getTrendingGifs(page, size)

        trendingGifData?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ trendingData -> view.onTrendingResult(trendingData.data) })
        return null
    }

    override fun searchGifs(q: String): BeanGifMain? {
        val trendingGifData = service?.api?.searchGifs(q)

        trendingGifData?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ trendingData -> view.onSearchResult(trendingData.data) })
        return null
    }

}
