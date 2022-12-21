package com.example.wallebi_app.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallebi_app.R
import com.example.wallebi_app.adapters.MarketsAdapter
import com.example.wallebi_app.api.HttpCallback
import com.example.wallebi_app.api.HttpUtil
import com.example.wallebi_app.api.markets.MarketApiModel
import com.example.wallebi_app.api.wallet.MarketsModel
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.json.JSONObject

class MarketFragment : Fragment() {

    var market_mode = 0
    val MODE_FAVORITE = 0
    val MODE_SPOT = 1
    val MODE_NEW = 2
    var spot_mode = 0
    val MODE_USDT = 0
    val MODE_IRT = 1
    var is_loaded = 0
    lateinit var layoutLoad:RelativeLayout
    lateinit var recyclerView: RecyclerView
    var marketsHash:HashMap<String,MarketApiModel>? = null
    var marketsArray:ArrayList<MarketApiModel>? = null
    var marketsArrayIrt:ArrayList<MarketApiModel>? = null
    var newListingHash:HashMap<String,MarketApiModel>? = null
    var newListingArray:ArrayList<MarketApiModel>? = null
    var favorites:HashMap<String,MarketApiModel>? = null
    var favoritesArrayList:ArrayList<MarketApiModel>? = null
    var arrayShow:ArrayList<MarketApiModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_market, container, false)
        layoutLoad = view.findViewById(R.id.layout_load)
        recyclerView = view.findViewById(R.id.recycler_markets)
        favorites = HashMap()

        setMarketMode(MODE_SPOT,view)
        getMarkets()

        view.findViewById<MaterialButton>(R.id.btn_spot).setOnClickListener { if(market_mode!=MODE_SPOT) setMarketMode(MODE_SPOT,view) }
        view.findViewById<MaterialButton>(R.id.btn_favorites).setOnClickListener { if(market_mode!=MODE_FAVORITE) setMarketMode(MODE_FAVORITE,view) }
        view.findViewById<MaterialButton>(R.id.btn_new_listing).setOnClickListener { if(market_mode!=MODE_NEW) setMarketMode(MODE_NEW,view) }
        view.findViewById<MaterialButton>(R.id.btn_usdt).setOnClickListener { if(spot_mode!=MODE_USDT) setSpotMode(MODE_USDT,view) }
        view.findViewById<MaterialButton>(R.id.btn_irt).setOnClickListener { if(spot_mode!=MODE_IRT) setSpotMode(MODE_IRT,view) }

        return view
    }

    private fun getMarkets(){
        getSpotMarkets()
        getNewMarkets()
        getSpotMarketsIrt()
        getFavoritesMarkets()
    }


    private fun showRecycler(arrayList: ArrayList<MarketApiModel>){
        var marketAdapter:MarketsAdapter? = null
        arrayList.let {
            favorites.let {
                marketAdapter = MarketsAdapter(arrayList,requireContext(),favorites)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = marketAdapter
            }
        }
    }


    private fun loadMarkets(){
        Log.i("Log1","spot market mode" + spot_mode)
        if(checkData()){
            if(market_mode == MODE_FAVORITE)
                showRecycler(favoritesArrayList!!)
            else if(market_mode == MODE_NEW)
                showRecycler(newListingArray!!)
            else if(market_mode == MODE_SPOT) {
                if (spot_mode == MODE_USDT)
                    showRecycler(marketsArray!!)
                else if(spot_mode == MODE_IRT)
                    showRecycler(marketsArrayIrt!!)
            }
        }
    }




    private fun getNewMarkets() {
        var netAddress = "v0/MarketService/new_markets/"

        Log.i("Log1", "get price info")
        val callback: HttpCallback = object : HttpCallback {
            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                is_loaded++
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", " new markets response: $res")
                    val jsonObject = JSONObject(res)
                    if(response.code == 200){
                        val userListType =
                            object : TypeToken<java.util.ArrayList<MarketApiModel?>?>() {}.type
                        val gson = Gson()
                        var walletsJson = jsonObject.getJSONArray("msg")
                        newListingArray = gson.fromJson<ArrayList<MarketApiModel>>(
                            walletsJson!!.toString(),
                            userListType
                        )
                    }
                } catch (e: Exception) {
                    Log.i("Log1", "new markets : failed to convert to json: $e")
                }
                mainHandler.post{
                    isLoaded()
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress, null, callback, HttpUtil.MODE_AUTH)
    }

    private fun getFavoritesMarkets() {
        var netAddress = "v0/MarketService/favorite_markets/"

        Log.i("Log1", "get price info")
        val callback: HttpCallback = object : HttpCallback {
            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                is_loaded++
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", " favorites response: $res")
                    val jsonObject = JSONObject(res)
                    if(response.code == 200){
                        val userListType =
                            object : TypeToken<java.util.ArrayList<MarketApiModel?>?>() {}.type
                        val gson = Gson()
                        var walletsJson = jsonObject.getJSONArray("msg")
                        favoritesArrayList = gson.fromJson<ArrayList<MarketApiModel>>(
                            walletsJson!!.toString(),
                            userListType
                        )
                        favorites!!.clear()
                        for(item in favoritesArrayList!!){
                            favorites!!.put(item.ticker_from + item.ticker_to,item)
                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "favorites : failed to convert to json: $e")
                }
                mainHandler.post{
                    isLoaded()
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress, null, callback, HttpUtil.MODE_AUTH)
    }

    private fun getSpotMarkets() {
        var netAddress = "v0/MarketService/USDT/spot_markets/"

        Log.i("Log1", "get price info")
        val callback: HttpCallback = object : HttpCallback {
            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                is_loaded++
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", " spot response: $res")
                    val jsonObject = JSONObject(res)
                    if(response.code == 200){
                        val userListType =
                            object : TypeToken<java.util.ArrayList<MarketApiModel?>?>() {}.type
                        val gson = Gson()
                        var walletsJson = jsonObject.getJSONArray("msg")
                        marketsArray = gson.fromJson<ArrayList<MarketApiModel>>(
                            walletsJson!!.toString(),
                            userListType
                        )
                    }


                } catch (e: Exception) {
                    Log.i("Log1", "price : failed to convert to json: $e")
                }
                mainHandler.post{
                    isLoaded()
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress, null, callback, HttpUtil.MODE_AUTH)
    }
    private fun getSpotMarketsIrt() {
        var netAddress = "v0/MarketService/IRT/spot_markets/"

        Log.i("Log1", "get price info")
        val callback: HttpCallback = object : HttpCallback {
            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                is_loaded++
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", " spot response: $res")
                    val jsonObject = JSONObject(res)
                    if(response.code == 200){
                        val userListType =
                            object : TypeToken<java.util.ArrayList<MarketApiModel?>?>() {}.type
                        val gson = Gson()
                        var walletsJson = jsonObject.getJSONArray("msg")
                        marketsArrayIrt = gson.fromJson<ArrayList<MarketApiModel>>(
                            walletsJson!!.toString(),
                            userListType
                        )
                    }


                } catch (e: Exception) {
                    Log.i("Log1", "price : failed to convert to json: $e")
                }
                mainHandler.post{
                    isLoaded()
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress, null, callback, HttpUtil.MODE_AUTH)
    }



    private fun isLoaded(){
        if(is_loaded>3){
            layoutLoad.visibility = View.GONE
            loadMarkets()
        }
    }


    //LAYOUT UI CHANGING
    private fun checkData():Boolean{
        if(marketsArray!=null&&favoritesArrayList!=null&&newListingArray!=null)
            return true
        return false
    }

    private fun setMarketMode(mode:Int,v:View){
        when(mode){
            MODE_FAVORITE -> setModeFavorite(v)
            MODE_SPOT -> setModeSpot(v)
            MODE_NEW -> setModeNew(v)
        }
        if(checkData()){
            loadMarkets()
        }
    }
    private fun setSpotMode(mode: Int,v:View){
        when(mode){
            MODE_USDT -> setSpotUSDT(v)
            MODE_IRT -> setSpotIRT(v)
        }
    }

    private fun setSpotUSDT(v: View){
        spot_mode = MODE_USDT
        v.findViewById<MaterialButton>(R.id.btn_usdt).setBackgroundColor(requireContext().getColor(R.color.mvp_bg_dark4))
        v.findViewById<MaterialButton>(R.id.btn_irt).setBackgroundColor(Color.TRANSPARENT)
        loadMarkets()
    }

    private fun setSpotIRT(v:View){
        spot_mode = MODE_IRT
        v.findViewById<MaterialButton>(R.id.btn_usdt).setBackgroundColor(Color.TRANSPARENT)
        v.findViewById<MaterialButton>(R.id.btn_irt).setBackgroundColor(requireContext().getColor(R.color.mvp_bg_dark4))
        loadMarkets()

    }



    private fun setModeFavorite(v:View){
        market_mode = MODE_FAVORITE
        v.findViewById<LinearLayout>(R.id.layout_spot_markets).visibility = View.GONE
        v.findViewById<MaterialButton>(R.id.btn_favorites).setTextColor(requireContext().getColor(R.color.mvp_gray4))
        v.findViewById<MaterialButton>(R.id.btn_spot).setTextColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<MaterialButton>(R.id.btn_new_listing).setTextColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<View>(R.id.view_favorites).setBackgroundColor(requireContext().getColor(R.color.mvp_yellow))
        v.findViewById<View>(R.id.view_spot).setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<View>(R.id.view_new_listing).setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))

    }
    private fun setModeSpot(v:View){
        market_mode = MODE_SPOT
        v.findViewById<LinearLayout>(R.id.layout_spot_markets).visibility = View.VISIBLE
        v.findViewById<MaterialButton>(R.id.btn_favorites).setTextColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<MaterialButton>(R.id.btn_spot).setTextColor(requireContext().getColor(R.color.mvp_gray4))
        v.findViewById<MaterialButton>(R.id.btn_new_listing).setTextColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<View>(R.id.view_favorites).setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<View>(R.id.view_spot).setBackgroundColor(requireContext().getColor(R.color.mvp_yellow))
        v.findViewById<View>(R.id.view_new_listing).setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
        setSpotMode(MODE_USDT,v)
    }

    private fun setModeNew(v:View){
        market_mode = MODE_NEW
        v.findViewById<LinearLayout>(R.id.layout_spot_markets).visibility = View.GONE
        v.findViewById<MaterialButton>(R.id.btn_favorites).setTextColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<MaterialButton>(R.id.btn_spot).setTextColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<MaterialButton>(R.id.btn_new_listing).setTextColor(requireContext().getColor(R.color.mvp_gray4))
        v.findViewById<View>(R.id.view_favorites).setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<View>(R.id.view_spot).setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
        v.findViewById<View>(R.id.view_new_listing).setBackgroundColor(requireContext().getColor(R.color.mvp_yellow))

    }


}