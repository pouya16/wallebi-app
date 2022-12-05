package com.example.wallebi_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.wallebi_app.R
import com.google.android.material.card.MaterialCardView

class TransferFragment : Fragment() {


    lateinit var cardAction:MaterialCardView
    lateinit var cardType:MaterialCardView
    lateinit var cardCoin:MaterialCardView
    lateinit var cardAddress:MaterialCardView
    lateinit var cardAmount:MaterialCardView
    lateinit var cardSkelet:MaterialCardView


    var mode = 0
    var actionMode = 0
    var typeMode = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transfer, container, false)
        loadViews(view)

        // HANDLE ACTION PART :
        view.findViewById<RadioButton>(R.id.radio_withdraw).setOnClickListener {setModeWithdraw(view)}
        view.findViewById<RadioButton>(R.id.radio_deposit).setOnClickListener {setModeDeposit(view)}
        view.findViewById<ImageButton>(R.id.img_action_collapse).setOnClickListener{
            view.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.GONE
        }

        // HANDLE CHOOSE TYPE PART :
        view.findViewById<RadioButton>(R.id.radio_coin).setOnClickListener {setModeCoin(view)}
        view.findViewById<RadioButton>(R.id.radio_fiat).setOnClickListener {setModeFiat(view)}
        view.findViewById<ImageButton>(R.id.img_type_collapse).setOnClickListener{
            view.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        var arg = arguments
        if(arg == null){
            mode = 0
        }else{
            mode = arg.getInt("mode",0)
        }
        loadMode(view)

    }


    private fun loadMode(v: View?){
        when(mode){
            0->{
                loadZero(v!!)
            }
        }

    }

    private fun setModeFiat(v:View){
        typeMode = 2
        setType(v,requireContext().getString(R.string.fiat))
    }

    private fun setModeCoin(v:View){
        typeMode = 1
        setType(v,requireContext().getString(R.string.coin))
    }

    private fun setType(v:View,text:String){
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinfiat).visibility = View.VISIBLE
        v.findViewById<LinearLayout>(R.id.layout_expand_fiatcoin).visibility = View.GONE
        v.findViewById<TextView>(R.id.txt_coinfiat_collapse).text = text
        v.findViewById<LinearLayout>(R.id.layout_collapse_coinname).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_expand_coin).visibility = View.VISIBLE
        cardCoin.visibility = View.VISIBLE
        cardSkelet.visibility = View.VISIBLE
        cardAddress.visibility = View.GONE
        cardAmount.visibility = View.GONE
    }

    private fun setModeDeposit(v:View){
        actionMode = 2
        setMode(v,requireContext().getString(R.string.deposit))
    }

    private fun setModeWithdraw(v:View){
        actionMode = 1
        setMode(v,requireContext().getString(R.string.withdraw))
    }

    private fun setMode(v:View,text:String){
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

    private fun loadZero(v:View){
        cardAction.visibility = View.VISIBLE
        cardSkelet.visibility = View.VISIBLE
        cardType.visibility  = View.GONE
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

    private fun loadViews(v:View){
        cardAction = v.findViewById(R.id.card_action)
        cardType = v.findViewById(R.id.card_fiat_coin)
        cardCoin = v.findViewById(R.id.card_coin_name)
        cardAddress = v.findViewById(R.id.card_address)
        cardAmount = v.findViewById(R.id.card_amount)
        cardSkelet = v.findViewById(R.id.card_end)
    }

}