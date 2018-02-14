package freshworkdemo.com.freshworkdemo.presenter

import freshworkdemo.com.freshworkdemo.model.BeanGifMain

/**
 * @author Jass
 * @version 1.0
 */

interface TrendingGifpresenterInteractor {
    fun getTrendingGifs(page: Int, size: Int): BeanGifMain?
    fun searchGifs(q: String): BeanGifMain?
}
