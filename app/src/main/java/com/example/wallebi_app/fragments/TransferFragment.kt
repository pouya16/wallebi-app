package com.example.wallebi_app.fragments

import android.R.attr.bitmap
import android.app.Activity
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.cardview.widget.CardView
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wallebi_app.R
import com.example.wallebi_app.api.*
import com.example.wallebi_app.api.bank.BankAccountsModel
import com.example.wallebi_app.api.bank.FiatInfoModel
import com.example.wallebi_app.api.bank.IbanAccountsModel
import com.example.wallebi_app.api.data.*
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
import org.w3c.dom.Text


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
    var coinListModel: CoinListModel? = null

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
    lateinit var adapterBankAccounts: ArrayAdapter<BankAccountsModel>
    lateinit var adapterBankIbans: ArrayAdapter<IbanAccountsModel>

    //FIAT DEPOSIT MODELS
    var fiatInfoModel:FiatInfoModel? = null
    var bankAccountsModel:BankAccountsModel? = null

    //FIAT WITHDRAW
    var ibanAccountsModel:IbanAccountsModel? = null

    //WITHDRAW :
    var addressValidateResponse:AddressValidateResponse? = null
    var walletWithdrawModel:WalletWithdrawModel? = null

    //DEFINE FIAT CARDS SPINNER PART:
    var dataBankCards: ArrayList<BankAccountsModel> = ArrayList()
    var dataBankIbans: ArrayList<IbanAccountsModel> = ArrayList()

    var mode = 0
    var actionMode = 0 //withdraw 1 deposit 2
    val ACTION_WITHDRAW = 1
    val ACTION_DEPOSIT = 2
    var typeMode = 0 //fiat 2 crypto is 1
    val TYPE_FIAT = 2
    val TYPE_CRYPTO = 1

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
            GetMeApi(context)
        }
        if(DataAccess.getBankAccountsModels() == null){
            GetBankCardsApi(requireContext())
        }

        //SEEKBAR SECTION
        view.findViewById<SeekBar>(R.id.amount_seekbar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                //todo create seekbar : 
                // write custom code for progress is changed
               // view.findViewById<EditText>(R.id.txt_amount).text = ((view.findViewById<TextView>(R.id.txt_available_balance).text.toString().toDouble()
               //         * progress)/100)).toString()
               // 0
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        )


        //WITHDRAW PRE INVOICE
        view.findViewById<MaterialButton>(R.id.btn_accept_amount).setOnClickListener{
            if(typeMode == TYPE_CRYPTO){
                if(validateBalance(view)){
                    showCryptoPre(view,view.findViewById<TextView>(R.id.txt_amount).text.toString())
                }
            }else{
                if(actionMode == ACTION_DEPOSIT){
                    if(validateBalanceFiat(view)){
                        showCryptoPre(view,view.findViewById<TextView>(R.id.txt_amount).text.toString())
                    }
                }else{
                    if(validateFiatWithdraw(view)){
                        showCryptoPre(view,view.findViewById<TextView>(R.id.txt_amount).text.toString())
                    }
                }
            }
        }

        //VALIDATE ADDRESS :
        view.findViewById<MaterialButton>(R.id.btn_accept_address).setOnClickListener{
            val address = view.findViewById<EditText>(R.id.txt_address).text.toString()
            var tag:String = ""
            if(address.length > 5){
                if(view.findViewById<MaterialCardView>(R.id.card_tag).visibility == View.VISIBLE){
                    tag = view.findViewById<EditText>(R.id.txt_tag).text.toString()
                }
                validateAddress(view,view.findViewById<MaterialButton>(R.id.btn_accept_address),address,tag)

            }else{
                StringHelper.showSnackBar(requireActivity(),"Please enter a valid address","failed",2)
            }
        }

        progressBar_main.visibility = View.GONE

        //HANDLE DEPOSIT PART :
        spinnerAddress.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                v: View, position: Int, id: Long
            ) {
                if(typeMode == TYPE_CRYPTO){
                    if (position != 0) {
                        showDepositAddress(view,ticker,networksArrayList[position].ticker)
                    } else {
                    }
                }else{
                    Log.i("Log1","action mode: $actionMode, pos: $position , parent size= ${parent.size}")
                    if(position != 0 && position != (dataBankCards.size - 1)){
                        if(actionMode == ACTION_DEPOSIT){
                            bankAccountsModel = dataBankCards.get(position)
                            getCardInfo(view,dataBankCards.get(position))
                        }else{
                            ibanAccountsModel = dataBankIbans.get(position)
                            getIbanInfo(view,ibanAccountsModel!!)
                        }
                    }else if(position == dataBankCards.size -1){
                        findNavController().navigate(R.id.action_transferFragment_to_bankFragment)
                    }
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
                    if (typeMode == TYPE_CRYPTO) {
                        ticker = data_crypto[position].name
                        setCoin(view, data_crypto[position].fullname)
                        coinListModel = data_crypto[position]
                    } else {
                        setCoin(view, data_fiat[position].fullname)
                    }
                } else {
                    coinListModel = null
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

        view.findViewById<ImageButton>(R.id.img_expand_address).setOnClickListener{
            view.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.VISIBLE
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


    private fun validateFiatWithdraw(v:View):Boolean{
        val editText = v.findViewById<EditText>(R.id.txt_amount)
        if(editText.text!=null&&editText.text.length>0){
            if(editText.text.toString().toDouble().compareTo(v.findViewById<TextView>(R.id.txt_available_balance).text.toString().toDouble())<=0) {
                return true
            }else{
                StringHelper.showSnackBar(requireActivity(),"ask too much ammount","Amount",2)
            }

        }else{
            StringHelper.showSnackBar(requireActivity(),"enter amount","Amount",2)
        }
        return false
    }

    //CHECK BALANCE FOR FIAT DEPOSIT
    private fun validateBalanceFiat(v:View):Boolean{
        val editText = v.findViewById<EditText>(R.id.txt_amount)
        if(editText.text!=null&&editText.text.length>0){
                if(editText.text.toString().toDouble().compareTo(fiatInfoModel!!.total_transfer.total_daily_iban)>=0) {
                    return true
                }else{
                    StringHelper.showSnackBar(requireActivity(),"ask too much ammount","Amount",2)
                }

        }else{
            StringHelper.showSnackBar(requireActivity(),"enter amount","Amount",2)
        }
        return false
    }


    //CHECK BALANCE FOR CRYPTO WITHDRAW
    private fun validateBalance(v:View):Boolean{
        val editText = v.findViewById<EditText>(R.id.txt_amount)
        if(editText.text!=null&&editText.text.length>0){
            if(editText.text.toString().toDouble().compareTo(walletWithdrawModel!!.balance)<=0){
                if(editText.text.toString().toDouble().compareTo(walletWithdrawModel!!.min_withdraw)>=0){
                    if(editText.text.toString().toDouble().compareTo(walletWithdrawModel!!.max_withdraw)<=0
                        && editText.text.toString().toDouble().compareTo(walletWithdrawModel!!.max_daily_withdraw)<=0){
                        return true
                    }else{
                        StringHelper.showSnackBar(requireActivity(),"max withdrawal problem","Amount",2)
                    }
                }else{
                    StringHelper.showSnackBar(requireActivity(),"enter number greater than min withdrawal","Amount",2)
                }
            }else{
                StringHelper.showSnackBar(requireActivity(),"not enough balance","Amount",2)
            }
        }else{
            StringHelper.showSnackBar(requireActivity(),"enter amount","Amount",2)
        }
        return false
    }

    private fun showCryptoPre(v:View,amount:String){

        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_amount).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_amount).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_amount_label).text = amount
        cardSkelet.visibility = View.GONE
        v.findViewById<MaterialCardView>(R.id.card_pre_invoice).visibility = View.VISIBLE
        if(typeMode == TYPE_CRYPTO){

            walletWithdrawModel
            v.findViewById<TextView>(R.id.txt_deposit_amount).text = amount
            v.findViewById<TextView>(R.id.txt_pre_amount).text = amount
            v.findViewById<TextView>(R.id.txt_pre_fee_show).text = "0"
            v.findViewById<TextView>(R.id.txt_amount_coin_symbol).text = "ticker"
            v.findViewById<TextView>(R.id.txt_pre_card_header).text = "Addressr"
            v.findViewById<TextView>(R.id.withdraw_pre1).visibility = View.VISIBLE
            v.findViewById<TextView>(R.id.withdraw_pre2).visibility = View.VISIBLE
            v.findViewById<TextView>(R.id.withdraw_pre3).visibility = View.VISIBLE
            v.findViewById<TextView>(R.id.txt_amount_coin_symbol).text = ticker
        }else{

            v.findViewById<TextView>(R.id.withdraw_pre1).visibility = View.GONE
            v.findViewById<TextView>(R.id.withdraw_pre2).visibility = View.GONE
            v.findViewById<TextView>(R.id.withdraw_pre3).visibility = View.GONE
            v.findViewById<TextView>(R.id.txt_amount_coin_symbol).text = "IRT"
            v.findViewById<TextView>(R.id.txt_pre_card_header).text = "Card Number"
            if(actionMode == ACTION_DEPOSIT){
                v.findViewById<TextView>(R.id.txt_pre_card).text = bankAccountsModel!!.card_number
            }else{

            }
            v.findViewById<TextView>(R.id.txt_deposit_amount).text = amount
            v.findViewById<TextView>(R.id.txt_pre_amount).text = amount
            v.findViewById<TextView>(R.id.txt_pre_fee_show).text = "0"
            v.findViewById<TextView>(R.id.txt_pre_fiat).text = "IRT"
            v.findViewById<TextView>(R.id.txt_pre_fiat2).text = "IRT"
            v.findViewById<TextView>(R.id.txt_pre_fiat3).text = "IRT"

        }

    }



    private fun validateAddress(v:View,btnNext:MaterialButton,address:String,tag:String){
        btnNext.isActivated = false
        progressBar_main.visibility = View.VISIBLE
        val netAddress = "v0/CryptoService/validate_address/"

        val callback: HttpCallback = object : HttpCallback {

            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                    btnNext.isActivated = true
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
                        if (jsonObject.getBoolean("success")){
                            val gson = Gson()
                             addressValidateResponse = gson.fromJson(
                                jsonObject.getJSONObject("msg").toString(),
                                AddressValidateResponse::class.java
                            )
                            getWalletInfo(v,btnNext,address,tag,addressValidateResponse!!)
                        }
                    }else{
                        mainHandler.post {
                            btnNext.isActivated = true
                            progressBar_main.visibility = View.GONE
                            StringHelper.showSnackBar(
                                context as Activity,
                                "failed to get data",
                                "Network",
                                2
                            )
                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
            }
        }

        var httpUtil = HttpUtil(context)
        Log.i("Log1","address is : $address , ticker is: $ticker")
        var bodyModel = BodyHandlingModel("address", address, "string")
        var bodyModel2 = BodyHandlingModel("coin", ticker, "string")
        var bodyList = ArrayList<BodyHandlingModel>()
        bodyList.add(bodyModel)
        bodyList.add(bodyModel2)
        var body = BodyMaker.getBody(bodyList)
        httpUtil.post(netAddress, body, null, callback, HttpUtil.MODE_AUTH)
    }

    private fun getWalletInfo(v:View,btnNext: MaterialButton,address:String,tag:String,addressValidateResponse:AddressValidateResponse){
        val netAddress = "v0/CryptoService/withdraw_info/"

        val callback: HttpCallback = object : HttpCallback {

            var mainHandler: Handler = Handler(context!!.getMainLooper())
            override fun onFialure(response: Response, throwable: Throwable) {
                try {
                } catch (e: Exception) {
                }
                mainHandler.post {
                    btnNext.isActivated = true
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
                    Log.i("Log1: ", "response wallet info: $res")
                    val jsonObject = JSONObject(res)
                    if (response.code == 200) {
                        if (jsonObject.getBoolean("success")){
                            val gson = Gson()
                            walletWithdrawModel = gson.fromJson(
                                jsonObject.getJSONObject("msg").toString(),
                                WalletWithdrawModel::class.java
                            )
                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
                mainHandler.post {
                    btnNext.isActivated = true
                    progressBar_main.visibility = View.GONE
                    showTransferAmount(v,address)
                }
            }
        }
        var httpUtil = HttpUtil(context)
        Log.i("Log1","ticker is : $address , network is: ${addressValidateResponse.network}")
        var bodyModel = BodyHandlingModel("network", addressValidateResponse.network, "string")
        var bodyModel2 = BodyHandlingModel("ticker_name", ticker, "string")
        var bodyList = ArrayList<BodyHandlingModel>()
        bodyList.add(bodyModel)
        bodyList.add(bodyModel2)
        var body = BodyMaker.getBody(bodyList)
        httpUtil.post(netAddress, body, null, callback, HttpUtil.MODE_AUTH)

    }

    private fun showTransferAmount(v:View,address:String){


        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<ImageButton>(R.id.img_expand_address).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.GONE
        v.findViewById<MaterialCardView>(R.id.card_remain_fiat).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_crypto_remain).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_address_label).text = address
        v.findViewById<TextView>(R.id.txt_coin_symbol_label).text = "BTC"
        v.findViewById<TextView>(R.id.txt_amount_input_coin_symbol).text = "BTC"
        cardAmount.visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_collapse_amount).visibility = View.GONE
        v.findViewById<AppCompatSeekBar>(R.id.amount_seekbar).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_amount).visibility = View.VISIBLE
        try{
            v.findViewById<TextView>(R.id.txt_available_balance).text = walletWithdrawModel!!.balance.toString()
            v.findViewById<TextView>(R.id.txt_amount_input_coin_symbol).text = ticker
            v.findViewById<TextView>(R.id.txt_remained_value).text = walletWithdrawModel!!.available_daily_withdraw.toString()
        }catch (e:Exception){

            Log.i("Log1", "failed to convert to json: $e")
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
        v.findViewById<MaterialCardView>(R.id.card_pre_invoice).visibility = View.GONE
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

    private fun getIbanInfo(v:View,bankCard:IbanAccountsModel){
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_address_header_label).text = "Card Number"
        v.findViewById<TextView>(R.id.txt_address_label).text = bankCard.card_number
        progressBar_main.visibility = View.VISIBLE

        v.findViewById<LinearLayout>(R.id.layout_withdraw_iban).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_remain_monthly_amount).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_remain_daily_amount).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.txt_max_daily_user).text = "Remaining Daily Withdrawal"
        v.findViewById<TextView>(R.id.txt_max_daily_card).text = "Remaining Monthly Withdrawal"

        val netAddress = "v0/CryptoService/fiat_info/"

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
                            fiatInfoModel = gson.fromJson(
                                jsonObject.getJSONObject("data").toString(),
                                FiatInfoModel::class.java)

                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
                mainHandler.post {
                    progressBar_main.visibility = View.GONE
                    cardAmount.visibility = View.VISIBLE
                    v.findViewById<LinearLayout>(R.id.layout_expand_amount).visibility = View.VISIBLE
                    v.findViewById<LinearLayout>(R.id.layout_crypto_remain).visibility = View.GONE
                    v.findViewById<CardView>(R.id.card_remain_fiat).visibility = View.GONE
                    v.findViewById<View>(R.id.layout_deposit).visibility = View.GONE
                    v.findViewById<LinearLayout>(R.id.layout_collapse_amount).visibility = View.GONE
                    v.findViewById<TextView>(R.id.txt_coin_symbol_label).text = "IRT"
                    v.findViewById<TextView>(R.id.txt_max_daily_amount).text = (3000000000.0-fiatInfoModel!!.total_transfer.total_daily).toString()
                    v.findViewById<TextView>(R.id.txt_max_card_amount).text = (15000000000.0-fiatInfoModel!!.total_transfer.total_monthly).toString()
                    v.findViewById<TextView>(R.id.txt_amount_input_coin_symbol).text = "IRT"
                    v.findViewById<TextView>(R.id.txt_available_balance).text = fiatInfoModel?.balance.toString() ?: "0"

                }
            }
        }



        var httpUtil = HttpUtil(context)
        //todo make here right bank card id problem
        Log.i("Log1","id is : ${bankCard.id}")
        var bodyModel = BodyHandlingModel("iban", "11", "int")
        var bodyModel2 = BodyHandlingModel("ticker", "6", "string")
        var bodyList = ArrayList<BodyHandlingModel>()
        bodyList.add(bodyModel)
        bodyList.add(bodyModel2)
        var body = BodyMaker.getBody(bodyList)
        Log.i("Log1","ibody is : $body")
        httpUtil.post(netAddress, body, null, callback, HttpUtil.MODE_AUTH)


    }

    //FIAT SHOW AMOUNT :
    private fun getCardInfo(v:View,bankCard:BankAccountsModel){
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_address_header_label).text = "Card Number"
        v.findViewById<TextView>(R.id.txt_address_label).text = bankCard.card_number

        v.findViewById<LinearLayout>(R.id.layout_withdraw_iban).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_remain_monthly_amount).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_remain_daily_amount).visibility = View.GONE

        v.findViewById<TextView>(R.id.txt_max_daily_user).text = "Max. Daily Deposit per User"
        v.findViewById<TextView>(R.id.txt_max_daily_card).text = "ax. Daily Deposit per Card"

        progressBar_main.visibility = View.VISIBLE

        val netAddress = "v0/CryptoService/fiat_info/"

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
                            fiatInfoModel = gson.fromJson(
                                jsonObject.getJSONObject("data").toString(),
                                FiatInfoModel::class.java)

                        }
                    }

                } catch (e: Exception) {
                    Log.i("Log1", "failed to convert to json: $e")
                }
                mainHandler.post {
                    progressBar_main.visibility = View.GONE
                    cardAmount.visibility = View.VISIBLE
                    v.findViewById<LinearLayout>(R.id.layout_expand_amount).visibility = View.VISIBLE
                    v.findViewById<LinearLayout>(R.id.layout_crypto_remain).visibility = View.GONE
                    v.findViewById<CardView>(R.id.card_remain_fiat).visibility = View.GONE
                    v.findViewById<View>(R.id.layout_deposit).visibility = View.GONE
                    v.findViewById<LinearLayout>(R.id.layout_collapse_amount).visibility = View.GONE
                    v.findViewById<SeekBar>(R.id.amount_seekbar).visibility = View.GONE
                    v.findViewById<TextView>(R.id.txt_coin_symbol_label).text = "IRT"
                    v.findViewById<TextView>(R.id.txt_max_daily_amount).text = "100,000,000"
                    v.findViewById<TextView>(R.id.txt_max_card_amount).text = "50,000,000"
                    v.findViewById<TextView>(R.id.txt_amount_input_coin_symbol).text = "IRT"
                    v.findViewById<TextView>(R.id.txt_available_balance).text = fiatInfoModel?.balance.toString() ?: "0"

                }
            }
        }



        var httpUtil = HttpUtil(context)
        //todo make here right bank card id problem
        Log.i("Log1","id is : ${bankCard.id}")
        var bodyModel = BodyHandlingModel("iban", "11", "int")
        var bodyModel2 = BodyHandlingModel("ticker", "6", "string")
        var bodyList = ArrayList<BodyHandlingModel>()
        bodyList.add(bodyModel)
        bodyList.add(bodyModel2)
        var body = BodyMaker.getBody(bodyList)
        Log.i("Log1","ibody is : $body")
        httpUtil.post(netAddress, body, null, callback, HttpUtil.MODE_AUTH)


    }

    private fun setFiat(v:View,coin:String){
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_coinname_collapse).text = coin
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.GONE
        v.findViewById<MaterialButton>(R.id.btn_accept_address).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_address_whitelist_off).visibility = View.GONE
        v.findViewById<MaterialCardView>(R.id.card_tag).visibility = View.GONE
        v.findViewById<RelativeLayout>(R.id.layout_address_whitelist_on).visibility = View.VISIBLE
        if (actionMode == ACTION_DEPOSIT){
            v.findViewById<TextView>(R.id.choose_address_expanded_title).text = requireContext().getString(R.string.choose_card)
            loadBankCards(v)
        }else{
            v.findViewById<TextView>(R.id.choose_address_expanded_title).text = requireContext().getString(R.string.choose_iban)
            loadBankIbans(v)
        }
    }

    private fun loadBankCards(v:View){
        dataBankCards = DataAccess.getBankAccountsModels()
        dataBankCards.add(0, BankAccountsModel(0,"Choose Card","",""))
        dataBankCards.add(BankAccountsModel(1,"6104338911471601","","melat"))//test
        dataBankCards.add(BankAccountsModel(0,"Add New Card","",""))
        adapterBankAccounts =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    dataBankCards
                )
            }!!
        spinnerAddress.adapter = adapterBankAccounts
    }

    private fun loadBankIbans(v:View){
        dataBankIbans = DataAccess.getIbanAccountsModels()
        dataBankIbans.add(0,IbanAccountsModel(0,"","Choose IBAN",""))
        dataBankIbans.add(IbanAccountsModel(1,"","IR4800001056748156489445616","melat"))
        dataBankIbans.add(IbanAccountsModel(0,"","Add new IBAN",""))
        adapterBankIbans =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    dataBankIbans
                )
            }!!
        spinnerAddress.adapter = adapterBankIbans

    }



    private fun setCoin(v: View, coin: String) {
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<MaterialCardView>(R.id.card_pre_invoice).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_coinname_collapse).text = coin
        v.findViewById<LinearLayout>(R.id.layout_collapse_address).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_expand_address).visibility = View.VISIBLE

        cardAddress.visibility = View.VISIBLE
        Log.i("Log1", " action mode is : $actionMode ")

        if(typeMode == TYPE_CRYPTO){
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
                try{
                    if (LoginData.meClass.is_withdraw_whitelist){
                        progressBar_main.visibility = View.VISIBLE
                        getWhitelist(v)
                    }else{
                        v.findViewById<LinearLayout>(R.id.layout_address_whitelist_off).visibility = View.VISIBLE
                        v.findViewById<RelativeLayout>(R.id.layout_address_whitelist_on).visibility = View.GONE
                    }
                }catch (e:Exception){
                    v.findViewById<LinearLayout>(R.id.layout_address_whitelist_off).visibility = View.VISIBLE
                    v.findViewById<RelativeLayout>(R.id.layout_address_whitelist_on).visibility = View.GONE
                }
                v.findViewById<TextView>(R.id.choose_address_expanded_title).text = requireContext().getString(R.string.choose_address)
                cardSkelet.visibility = View.GONE
            }
        }else{
            setFiat(v,coin)
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

        v.findViewById<TextView>(R.id.txt_choose).text = requireContext().getString(R.string.choose_currency)
        v.findViewById<TextView>(R.id.txt_choose_header).text = requireContext().getString(R.string.currency)
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
        v.findViewById<TextView>(R.id.txt_choose).text = requireContext().getString(R.string.choose_coin)
        v.findViewById<TextView>(R.id.txt_choose_header).text = requireContext().getString(R.string.coin)
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
        v.findViewById<MaterialCardView>(R.id.card_pre_invoice).visibility = View.GONE
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
        v.findViewById<MaterialCardView>(R.id.card_pre_invoice).visibility = View.GONE
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