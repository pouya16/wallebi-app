package com.example.wallebi_app.fragments

import android.R.attr.bitmap
import android.app.Activity
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import com.example.wallebi_app.R
import com.example.wallebi_app.api.*
import com.example.wallebi_app.api.data.CoinListModel
import com.example.wallebi_app.api.data.DepositAddressModel
import com.example.wallebi_app.api.data.GetCoinsApi
import com.example.wallebi_app.api.data.NetworkModel
import com.example.wallebi_app.database.DataAccess
import com.example.wallebi_app.database.LoginData
import com.example.wallebi_app.helpers.StringHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.WriterException
import okhttp3.Response
import org.json.JSONObject


class TransferFragment : Fragment() {


    lateinit var cardAction: MaterialCardView
    lateinit var cardType: MaterialCardView
    lateinit var cardCoin: MaterialCardView
    lateinit var cardAddress: MaterialCardView
    lateinit var cardAmount: MaterialCardView
    lateinit var cardSkelet: MaterialCardView
    lateinit var spinnerCoin: Spinner
    lateinit var spinnerAddress: Spinner
    lateinit var progressBar_main: ProgressBar

    //deposit layout items:
    lateinit var btnShare:MaterialButton
    lateinit var btnTransaction:MaterialButton
    lateinit var btnCpyAddress: ImageButton
    lateinit var btnCpyTag: ImageButton
    lateinit var txtDepositAddress: TextView
    lateinit var txtTagAddress: TextView
    var depositAddressModel: DepositAddressModel? = null

    // DEFINE COINS SPINNDER PART
    var data_fiat: ArrayList<CoinListModel> = ArrayList()
    var data_crypto: ArrayList<CoinListModel> = ArrayList()
    lateinit var adapterCoins: ArrayAdapter<CoinListModel>
    var ticker = ""

    //network addresses model
    var networksArrayList: ArrayList<NetworkModel> = ArrayList()
    lateinit var adapterNetworks: ArrayAdapter<NetworkModel>

    var mode = 0
    var actionMode = 0 //withdraw 1 deposit 2
    var typeMode = 0 //fiat 2 crypto is 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transfer, container, false)
        loadViews(view)
        //check login
        //todo uncomment this
        /*if(LoginData.access_token.length < 3){
            StringHelper.showSnackBar(context as Activity?,"need login first","Login",2);
            findNavController().navigate(R.id.action_transferFragment_to_homeFragment)
        }*/
        //LOAD DATA
        var data = DataAccess()
        if (data.cryptoListModels == null) {
            GetCoinsApi(context, GetCoinsApi.MODE_CRYPTO)
        } else
            Log.i("Log1", "data exist")
        if (data.fiatListModels == null) {
            GetCoinsApi(context, GetCoinsApi.MODE_FIAT)
        } else
            Log.i("Log1", "data exist")
        if (LoginData.meClass == null) {
            var getMeApi = GetMeApi(context)
        }

        progressBar_main.visibility = View.GONE

        //HANDLE DEPOSIT PART :
        spinnerAddress.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                v: View, position: Int, id: Long
            ) {
                if (position != 0) {
                    showDepositAddress(view,ticker,networksArrayList[position].ticker)
                } else {
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        //HANDLE CHOOSE COIN PART
        spinnerCoin.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                v: View, position: Int, id: Long
            ) {
                if (position != 0) {
                    if (typeMode == 1) {
                        ticker = data_crypto[position].name
                        setCoin(view, data_crypto[position].fullname)
                    } else {
                        setCoin(view, data_fiat[position].fullname)
                    }
                } else {
                    ticker = ""
                    cardAddress.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        view.findViewById<ImageButton>(R.id.img_expand_coinname).setOnClickListener {
            view.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.GONE
        }


        // HANDLE ACTION PART :
        view.findViewById<RadioButton>(R.id.radio_withdraw)
            .setOnClickListener { setModeWithdraw(view) }
        view.findViewById<RadioButton>(R.id.radio_deposit)
            .setOnClickListener { setModeDeposit(view) }
        view.findViewById<ImageButton>(R.id.img_action_collapse).setOnClickListener {
            view.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.GONE
        }

        // HANDLE CHOOSE TYPE PART :
        view.findViewById<RadioButton>(R.id.radio_coin).setOnClickListener { setModeCoin(view) }
        view.findViewById<RadioButton>(R.id.radio_fiat).setOnClickListener { setModeFiat(view) }
        view.findViewById<ImageButton>(R.id.img_type_collapse).setOnClickListener {
            view.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        var arg = arguments
        if (arg == null) {
            mode = 0
        } else {
            mode = arg.getInt("mode", 0)
        }
        loadMode(view)


    }


    private fun loadMode(v: View?) {
        when (mode) {
            0 -> {
                loadZero(v!!)
            }
        }

    }


    private fun showDepositAddress(v:View,ticker:String,network:String){
        cardAmount.visibility = View.VISIBLE
        Log.i("Log1","ticker is: $ticker  + network is : $network")
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_address_header_label).text = "Network"
        v.findViewById<TextView>(R.id.txt_address_label).text = network
        v.findViewById<MaterialCardView>(R.id.card_address).visibility = View.VISIBLE
        v.findViewById<MaterialCardView>(R.id.card_end).visibility = View.GONE
        v.findViewById<ProgressBar>(R.id.progress_deposit).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_deposit_inside).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_amount).visibility = View.GONE
        v.findViewById<View>(R.id.layout_deposit).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_amount).visibility = View.GONE
        var address = "v0/CryptoService/wallet_address/"
        val callback:HttpCallback = object :HttpCallback{
            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                    v.findViewById<ProgressBar>(R.id.progress_deposit).visibility = View.GONE
                    StringHelper.showSnackBar(
                        context as Activity,
                        "failed to get data",
                        "Network",
                        2
                    )
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
                            var gson = Gson()
                            depositAddressModel = gson.fromJson(jsonObject.getJSONObject("msg").toString(),DepositAddressModel::class.java)
                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
                mainHandler.post {
                    v.findViewById<ProgressBar>(R.id.progress_deposit).visibility = View.GONE
                    if(depositAddressModel!=null){
                        Log.i("Log1","deposit model is not null")
                        v.findViewById<LinearLayout>(R.id.layout_deposit_inside).visibility = View.VISIBLE
                        v.findViewById<TextView>(R.id.txt_deposit_address).text = depositAddressModel!!.address
                        var imageQr = v.findViewById<ImageView>(R.id.img_qr)
                        var qrgEncoder =
                            QRGEncoder(depositAddressModel!!.address, QRGContents.Type.TEXT,148)
                        qrgEncoder.colorBlack = Color.WHITE
                        qrgEncoder.colorWhite = Color.BLACK
                        try {
                            // Getting QR-Code as Bitmap
                            var bitmap = qrgEncoder.bitmap
                            // Setting Bitmap to ImageView
                            imageQr.setImageBitmap(bitmap)
                        } catch (e: WriterException) {
                            Log.v("Log1", e.toString())
                        }
                        v.findViewById<TextView>(R.id.txt_deposit_alert_1).text = context!!.getString(R.string.alert_1_prefix) +
                                " " + ticker + " - " + network + " " + context!!.getString(R.string.alert_1_suffix)

                        v.findViewById<TextView>(R.id.txt_deposit_alert_2).text = context!!.getString(R.string.alert_2_prefix) +
                                " " + depositAddressModel!!.fee + context!!.getString(R.string.alert_2_suffix)
                        if(depositAddressModel!!.memo !=null){
                            Log.i("Log1","memo is: ${depositAddressModel!!.memo}")
                            if(depositAddressModel!!.memo.toString().length>1){
                                xrpVisibilityDeposit(v,View.VISIBLE)
                                v.findViewById<TextView>(R.id.txt_tag_address).text = depositAddressModel!!.memo.toString()
                                v.findViewById<TextView>(R.id.txt_deposit_alert_3).text = context!!.getString(R.string.alert_xrp_3)
                            }else{
                                xrpVisibilityDeposit(v,View.GONE)
                            }
                        }else{
                            Log.i("Log1","memo is null")
                            xrpVisibilityDeposit(v,View.GONE)
                        }
                    }
                }
            }
        }

        var httpUtil = HttpUtil(context)
        var bodyModel = BodyHandlingModel("ticker", ticker, "string")
        var bodyModel2 = BodyHandlingModel("network", network, "string")
        var bodyList = ArrayList<BodyHandlingModel>()
        bodyList.add(bodyModel)
        bodyList.add(bodyModel2)
        var body = BodyMaker.getBody(bodyList)
        httpUtil.post(address, body, null, callback, HttpUtil.MODE_AUTH)

    }
    private fun xrpVisibilityDeposit(v:View,visibility:Int){
        v.findViewById<TextView>(R.id.txt_tag_header).visibility = visibility
        v.findViewById<LinearLayout>(R.id.layout_tag).visibility = visibility
        v.findViewById<TextView>(R.id.txt_deposit_alert_3).visibility = visibility
    }

    private fun getWhitelist(v:View){

    }

    private fun setCoin(v: View, coin: String) {
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_coinname_collapse).text = coin
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.VISIBLE

        cardAddress.visibility = View.VISIBLE
        Log.i("Log1", " action mode is : $actionMode ")
        if (actionMode == 2) {
            chooseNetwork(v)
            v.findViewById<LinearLayout>(R.id.layout_address_whitelist_off).visibility = View.GONE
            v.findViewById<MaterialButton>(R.id.btn_accept_address).visibility = View.GONE
            v.findViewById<MaterialCardView>(R.id.card_tag).visibility = View.GONE
            cardSkelet.visibility = View.VISIBLE
        } else {
            v.findViewById<MaterialButton>(R.id.btn_accept_address).visibility = View.VISIBLE
            if(ticker.compareTo("XRP") == 0){
                v.findViewById<MaterialCardView>(R.id.card_tag).visibility = View.VISIBLE
            }else{
                v.findViewById<MaterialCardView>(R.id.card_tag).visibility = View.GONE
            }
            if (LoginData.meClass.is_withdraw_whitelist){
                progressBar_main.visibility = View.VISIBLE
                getWhitelist(v)
            }else{
                v.findViewById<LinearLayout>(R.id.layout_address_whitelist_off).visibility = View.VISIBLE
                v.findViewById<RelativeLayout>(R.id.layout_address_whitelist_on).visibility = View.GONE
            }
            v.findViewById<TextView>(R.id.choose_address_expanded_title).text = requireContext().getString(R.string.choose_address)
            cardSkelet.visibility = View.GONE
        }
        cardAmount.visibility = View.GONE
    }

    private fun chooseNetwork(v: View) {
        v.findViewById<TextView>(R.id.choose_address_expanded_title).text = "Choose Network"
        progressBar_main.visibility = View.VISIBLE
        getNetworks(v)
    }

    private fun getNetworks(v: View) {

        Log.i("Log1", "ticker is: $ticker");
        val address_network = "v0/CryptoService/network_list_2/"
        val callback: HttpCallback = object : HttpCallback {

            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                    progressBar_main.visibility = View.GONE
                    StringHelper.showSnackBar(
                        context as Activity,
                        "failed to get data",
                        "Network",
                        2
                    )
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
                            val gson = Gson()
                            val networkListType =
                                object : TypeToken<java.util.ArrayList<NetworkModel?>?>() {}.type
                            networksArrayList.clear()
                            networksArrayList = gson.fromJson(
                                jsonObject.getJSONArray("msg").toString(),
                                networkListType
                            )
                            networksArrayList.add(
                                0,
                                NetworkModel(
                                    "Choose Network",
                                    "Choose Network",
                                    0.0,
                                    "Choose Network"
                                )
                            )

                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
                mainHandler.post {
                    progressBar_main.visibility = View.GONE
                    adapterNetworks =
                        context?.let {
                            ArrayAdapter(
                                it,
                                android.R.layout.simple_spinner_dropdown_item,
                                networksArrayList
                            )
                        }!!
                    spinnerAddress.adapter = adapterNetworks
                }
            }
        }
        var httpUtil = HttpUtil(context)
        var bodyModel = BodyHandlingModel("ticker", ticker, "string")
        var bodyList = ArrayList<BodyHandlingModel>()
        bodyList.add(bodyModel)
        var body = BodyMaker.getBody(bodyList)
        httpUtil.post(address_network, body, null, callback, HttpUtil.MODE_NO_AUTH)

    }


    private fun setModeFiat(v: View) {
        typeMode = 2
        data_fiat = DataAccess().fiatListModels
        data_fiat.add(0, CoinListModel(0, "Select Fiat", "", "", true));
        adapterCoins =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    data_fiat
                )
            }!!
        setType(v, requireContext().getString(R.string.fiat), adapterCoins)
        //todo uncomment this
        /*if(LoginData.meClass.kyc_level.level == 1){
        }else if (LoginData.meClass.kyc_level.state == 1){
            StringHelper.showSnackBar(context as Activity?,"your kyc process is pending", "Kyc", 1)
        }else{
            StringHelper.showSnackBar(context as Activity?,"you need to pass the kyc", "Kyc", 2)
        }*/
    }

    private fun setModeCoin(v: View) {
        typeMode = 1
        data_crypto = DataAccess().cryptoListModels
        data_fiat.add(0, CoinListModel(0, "Select Coin", "", "", true));
        adapterCoins =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    data_crypto
                )
            }!!
        setType(v, requireContext().getString(R.string.coin), adapterCoins)
    }

    private fun setType(v: View, text: String, adapter: ArrayAdapter<CoinListModel>) {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCoin.adapter = null
        spinnerCoin.adapter = adapter

        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_coinfiat_collapse).text = text
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_choose).text = getString(R.string.choose) + " " + text
        cardCoin.visibility = View.VISIBLE
        cardSkelet.visibility = View.VISIBLE
        cardAddress.visibility = View.GONE
        cardAmount.visibility = View.GONE
    }

    private fun setModeDeposit(v: View) {
        actionMode = 2
        setMode(v, requireContext().getString(R.string.deposit))
    }

    private fun setModeWithdraw(v: View) {
        actionMode = 1
        setMode(v, requireContext().getString(R.string.withdraw))
        //todo uncomment this
        /*if(LoginData.meClass.kyc_level.level == 1){
        }else if (LoginData.meClass.kyc_level.state == 1){
            StringHelper.showSnackBar(context as Activity?,"your kyc process is pending", "Kyc", 1)
        }else{
            StringHelper.showSnackBar(context as Activity?,"you need to pass the kyc", "Kyc", 2)
        }*/
    }

    private fun setMode(v: View, text: String) {
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_action_collapse).text = text
        cardType.visibility = View.VISIBLE
        cardSkelet.visibility = View.VISIBLE
        cardCoin.visibility = View.GONE
        cardAddress.visibility = View.GONE
        cardAmount.visibility = View.GONE
    }

    private fun loadZero(v: View) {
        cardAction.visibility = View.VISIBLE
        cardSkelet.visibility = View.VISIBLE
        cardType.visibility = View.GONE
        cardCoin.visibility = View.GONE
        cardAddress.visibility = View.GONE
        cardAmount.visibility = View.GONE
        var layoutActionExpand = v.findViewById<LinearLayout>(R.id.layout_expand_action)
        var layoutActoinCollapse = v.findViewById<LinearLayout>(R.id.layout_collapse_action)
        var radioAction = v.findViewById<RadioGroup>(R.id.radio_group_action)
        layoutActionExpand.visibility = View.VISIBLE
        layoutActoinCollapse.visibility = View.GONE
        radioAction.clearCheck()
    }

    private fun loadViews(v: View) {
        cardAction = v.findViewById(R.id.card_action)
        cardType = v.findViewById(R.id.card_fiat_coin)
        cardCoin = v.findViewById(R.id.card_coin_name)
        cardAddress = v.findViewById(R.id.card_address)
        cardAmount = v.findViewById(R.id.card_amount)
        cardSkelet = v.findViewById(R.id.card_end)
        spinnerCoin = v.findViewById(R.id.spinner_coin)
        progressBar_main = v.findViewById(R.id.progressbar)
        spinnerAddress = v.findViewById(R.id.spinner_address)
        btnShare = v.findViewById(R.id.btn_share)
        btnTransaction = v.findViewById(R.id.btn_transaction)
        btnCpyAddress = v.findViewById(R.id.img_cpy_address)
        btnCpyTag = v.findViewById(R.id.img_cpy_tag)
        txtDepositAddress = v.findViewById(R.id.txt_deposit_address)
        txtTagAddress = v.findViewById(R.id.txt_tag_address)
    }

}