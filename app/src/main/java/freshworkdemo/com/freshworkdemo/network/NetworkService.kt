package freshworkdemo.com.freshworkdemo.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import java.io.IOException
import java.util.Observable

import freshworkdemo.com.freshworkdemo.model.BeanGifMain
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author  Jass
 * This class contains retrofit implementation to make network calls
 */

class NetworkService @JvmOverloads constructor(baseUrlTemp: String = baseUrl) {
    /**
     * Method to return the API interface.
     * @return
     */
    val api: NetworkAPI
    private val okHttpClient: OkHttpClient
    var SERVER_RESPONSE_OK = 200

    init {

        okHttpClient = buildClient()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        api = retrofit.create(NetworkAPI::class.java)
    }


    /**
     * Method to build and return an OkHttpClient so we can set/get
     * headers quickly and efficiently.
     * @return
     */
    fun buildClient(): OkHttpClient {

        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        builder.addInterceptor { chain ->
            // Do anything with response here
            //if we ant to grab a specific cookie or something..
            chain.proceed(chain.request())
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        builder.addInterceptor { chain ->
            //this is where we will add whatever we want to our request headers.
            val request = chain.request().newBuilder().addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("api_key", "52jYtGFhwxSH2S4RNqg1KognA7ouibaq")
                    .build()
            chain.proceed(request)
        }

        return builder.build()
    }


    /**
     * all the Service calls to use for the retrofit requests.
     */
    interface NetworkAPI {


        @GET("trending")//get list of trending gifs
        fun getTrendingGifs(@Query("offset") offset: Int, @Query("limit") limit: Int): io.reactivex.Observable<BeanGifMain>

        @GET("search")//search gifs
        fun searchGifs(@Query("q") q: String): io.reactivex.Observable<BeanGifMain>


    }

    companion object {
        var baseUrl = "https://api.giphy.com/v1/gifs/"
    }

}
