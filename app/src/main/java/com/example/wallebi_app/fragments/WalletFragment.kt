package com.example.wallebi_app.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallebi_app.R
import com.example.wallebi_app.adapters.WalletAdapter
import com.example.wallebi_app.api.HttpCallback
import com.example.wallebi_app.api.HttpUtil
import com.example.wallebi_app.api.data.CoinListModel
import com.example.wallebi_app.api.wallet.models.WalletModel
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.json.JSONObject

class WalletFragment : Fragment() {

    val USDT_MODE = 0
    val IRT_MODE = 1

    lateinit var imgHideBalance:MaterialCardView
    lateinit var btnHideSmallAmount: MaterialCardView
    lateinit var recyclerWallets:RecyclerView
    var arrayWallets:ArrayList<WalletModel>? = null
    var walletAdapter:WalletAdapter? = null

    var mode = USDT_MODE // 0 for usdt 1 for irt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wallet, container, false)
        loadViews(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        setWallets(mode)

    }

    private fun loadViews(v:View){
        imgHideBalance = v.findViewById(R.id.card_hide_balance)
        btnHideSmallAmount = v.findViewById(R.id.card_hide_balance)
        recyclerWallets = v.findViewById<RecyclerView>(R.id.recycler_wallet)
    }

    private fun setWallets(mode:Int){
        if(arrayWallets!=null){
            arrayWallets

        }else{
            getWallets()
        }
    }

    private fun getWallets(){
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
                        if (jsonObject.getBoolean("success")){

                            val userListType =
                                object : TypeToken<java.util.ArrayList<WalletModel?>?>() {}.type

                            val gson = Gson()
                            var walletsJson = jsonObject.getJSONObject("msg")
                                .getJSONArray("WALLETS")

                            arrayWallets = gson.fromJson<ArrayList<WalletModel>>(walletsJson!!.toString(),userListType)
                        }
                        mainHandler.post {
                            arrayWallets.let {

                                walletAdapter = WalletAdapter(arrayWallets,requireContext(),1.0,WalletAdapter.MODE_USDT)
                                Log.i("Log1","wallets adapter ${walletAdapter!!.itemCount}" )
                                recyclerWallets.layoutManager = LinearLayoutManager(requireContext())
                                recyclerWallets.adapter = walletAdapter
                            }

                        }
                    }else{
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
            }
        }

        var httpUtil = HttpUtil(requireContext())
        httpUtil.get(netAddress,null,callback,HttpUtil.MODE_AUTH)


    }


}