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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transfer, container, false)
        loadViews(view)
        view.findViewById<RadioButton>(R.id.radio_withdraw).setOnClickListener {setModeWithdraw(view)}
        view.findViewById<RadioButton>(R.id.radio_withdraw).setOnClickListener {setModeDeposit(view)}
        view.findViewById<ImageButton>(R.id.img_action_collapse).setOnClickListener{
            view.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.GONE
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        var arg = arguments
        if(arg == null){

        }else{

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

    private fun setModeDeposit(v:View){
        actionMode = 2
        setMode(v,requireContext().getString(R.string.deposit))
    }

    private fun setModeWithdraw(v:View){
        actionMode = 1
        setMode(v,requireContext().getString(R.string.withdraw))
    }

    private fun setMode(v:View,text:String){
        v.findViewById<LinearLayout>(R.id.layout_expand_action).visibility = View.GONE
        v.findViewById<LinearLayout>(R.id.layout_collapse_action).visibility = View.VISIBLE
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