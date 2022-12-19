package com.example.wallebi_app.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallebi_app.R
import com.example.wallebi_app.adapters.WalletAdapter
import com.example.wallebi_app.api.HttpCallback
import com.example.wallebi_app.api.HttpUtil
import com.example.wallebi_app.api.data.CoinListModel
import com.example.wallebi_app.api.wallet.MarketsModel
import com.example.wallebi_app.api.wallet.models.WalletModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.json.JSONObject

class WalletFragment : Fragment() {

    val USDT_MODE = 0
    val IRT_MODE = 1

    var hide_small = false
    var hide_amount = false

    var coin_mode = true

    lateinit var imgHideBalance: MaterialCardView
    lateinit var btnHideSmallAmount: MaterialCardView
    lateinit var recyclerWallets: RecyclerView
    lateinit var layoutLoad: RelativeLayout
    var arrayWallets: ArrayList<WalletModel>? = null
    var arrayMarkets: ArrayList<MarketsModel>? = null
    var marketsUsdtHash: HashMap<String, Double> = HashMap()
    var marketsIrtHash: HashMap<String, Double> = HashMap()
    var walletAdapter: WalletAdapter? = null

    var mode = USDT_MODE // 0 for usdt 1 for irt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wallet, container, false)
        loadViews(view)


        view.findViewById<MaterialButton>(R.id.btn_hide_small)
            .setOnClickListener { changeHideAmount(view) }
        view.findViewById<MaterialCardView>(R.id.card_hide_balance)
            .setOnClickListener { changeShowAmount(view) }

        view.findViewById<MaterialButton>(R.id.btn_fiat)
            .setOnClickListener { if(coin_mode){
                changeCoinMode(view)
            } }

        view.findViewById<MaterialButton>(R.id.btn_coin)
            .setOnClickListener { if(!coin_mode){
                changeCoinMode(view)
            } }



        return view
    }


    private fun changeCoinMode(v: View) {
        if (coin_mode) {
            coin_mode = false
            v.findViewById<MaterialButton>(R.id.btn_fiat)
                .setTextColor(requireContext().getColor(R.color.mvp_gray4))
            v.findViewById<MaterialButton>(R.id.btn_coin)
                .setTextColor(requireContext().getColor(R.color.mvp_gray2))
            v.findViewById<View>(R.id.view_iban)
                .setBackgroundColor(requireContext().getColor(R.color.mvp_yellow))
            v.findViewById<View>(R.id.view_card)
                .setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
            loadRecycler()
        } else {
            coin_mode = true
            v.findViewById<MaterialButton>(R.id.btn_fiat)
                .setTextColor(requireContext().getColor(R.color.mvp_gray2))
            v.findViewById<MaterialButton>(R.id.btn_coin)
                .setTextColor(requireContext().getColor(R.color.mvp_gray4))
            v.findViewById<View>(R.id.view_iban)
                .setBackgroundColor(requireContext().getColor(R.color.mvp_gray2))
            v.findViewById<View>(R.id.view_card)
                .setBackgroundColor(requireContext().getColor(R.color.mvp_yellow))
            loadRecycler()
        }
    }


    private fun changeShowAmount(v: View) {
        if (hide_amount) {
            hide_amount = false
            v.findViewById<TextView>(R.id.txt_total_balance).text = "0.00"
            loadRecycler()
        } else {
            hide_amount = true
            v.findViewById<TextView>(R.id.txt_total_balance).text = "*"
            loadRecycler()
        }
    }

    private fun changeHideAmount(v: View) {
        if (hide_small) {
            hide_small = false
            v.findViewById<MaterialButton>(R.id.btn_hide_small).text =
                requireContext().getString(R.string.hide_small_ammounts)
            loadRecycler()
        } else {
            hide_small = true
            v.findViewById<MaterialButton>(R.id.btn_hide_small).text =
                requireContext().getString(R.string.show_small_amounts)
            loadRecycler()
        }
    }


    override fun onResume() {
        super.onResume()
        setWallets(mode)
        if (arrayWallets != null) {
            if (arrayMarkets != null) {
                loadRecycler()
            }
            getPriceInfo()
        }
    }

    private fun loadViews(v: View) {
        imgHideBalance = v.findViewById(R.id.card_hide_balance)
        btnHideSmallAmount = v.findViewById(R.id.card_hide_balance)
        recyclerWallets = v.findViewById<RecyclerView>(R.id.recycler_wallet)
        layoutLoad = v.findViewById(R.id.layout_load)
    }

    private fun setWallets(mode: Int) {
        if (arrayWallets != null) {
            arrayWallets
        } else {
            getWallets()
        }
    }

    private fun loadRecycler() {
        arrayWallets.let {
            walletAdapter = WalletAdapter(
                arrayWallets,
                requireContext(),
                marketsUsdtHash,
                mode,
                hide_small,
                hide_amount,
                coin_mode
            )

            Log.i("Log1", "wallets adapter ${walletAdapter!!.itemCount}")
            recyclerWallets.layoutManager = LinearLayoutManager(requireContext())
            recyclerWallets.adapter = walletAdapter
        }

    }

    private fun getWallets() {
        var netAddress = "v0/CryptoService/wallets_info/"

        Log.i("Log1", "get wallets")
        val callback: HttpCallback = object : HttpCallback {
            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                }
            }

            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", "response: $res")
                    val jsonObject = JSONObject(res)
                    if (response.code == 200) {
                        if (jsonObject.getBoolean("success")) {

                            val userListType =
                                object : TypeToken<java.util.ArrayList<WalletModel?>?>() {}.type

                            val gson = Gson()
                            var walletsJson = jsonObject.getJSONObject("msg")
                                .getJSONArray("WALLETS")

                            arrayWallets = gson.fromJson<ArrayList<WalletModel>>(
                                walletsJson!!.toString(),
                                userListType
                            )
                            getPriceInfo()
                        }
                        mainHandler.post {
                            arrayWallets.let {

                                //walletAdapter = WalletAdapter(arrayWallets,requireContext(),1.0,WalletAdapter.MODE_USDT)
                                //Log.i("Log1","wallets adapter ${walletAdapter!!.itemCount}" )
                                //recyclerWallets.layoutManager = LinearLayoutManager(requireContext())
                                //recyclerWallets.adapter = walletAdapter
                            }//

                        }
                    } else {
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress, null, callback, HttpUtil.MODE_AUTH)
    }


    private fun getPriceInfo() {
        var netAddress = "v0/CryptoService/price_info/"

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

            override fun onSuccess(response: Response) {
                Log.i("Log1", "" + response.code)
                try {
                    val res = response.body!!.string()
                    Log.i("Log1: ", " price response: $res")
                    val jsonObject = JSONObject(res)
                    if (response.code == 200) {
                        if (jsonObject.getBoolean("success")) {
                            layoutLoad.visibility = View.GONE
                            val userListType =
                                object : TypeToken<java.util.ArrayList<MarketsModel?>?>() {}.type

                            val gson = Gson()
                            var walletsJson = jsonObject.getJSONObject("msg")
                                .getJSONArray("markets")
                            arrayMarkets = gson.fromJson<ArrayList<MarketsModel>>(
                                walletsJson!!.toString(),
                                userListType
                            )
                            for (markets: MarketsModel in arrayMarkets!!) {
                                if (markets.ticker_to.lowercase().compareTo("usdt") == 0) {
                                    if (marketsUsdtHash.contains(markets.ticker_from)) {
                                        marketsUsdtHash.replace(
                                            markets.ticker_from,
                                            markets.line_price
                                        )
                                    } else {
                                        marketsUsdtHash.put(markets.ticker_from, markets.line_price)
                                    }
                                } else {
                                    if (marketsIrtHash.contains(markets.ticker_from)) {
                                        marketsIrtHash.replace(
                                            markets.ticker_from,
                                            markets.line_price
                                        )
                                    } else {
                                        marketsIrtHash.put(markets.ticker_from, markets.line_price)
                                    }
                                }
                            }
                        }
                        mainHandler.post {
                            loadRecycler()
                        }
                    } else {
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "price : failed to convert to json: $e")
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress, null, callback, HttpUtil.MODE_AUTH)
    }


}