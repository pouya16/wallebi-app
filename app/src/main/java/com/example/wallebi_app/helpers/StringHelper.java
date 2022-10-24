package com.example.wallebi_app.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;

public class StringHelper {
    public static String putDelimeters(int numb){
        return numberDelimeter(numb);
    }

    public static String putDelimeters(String numb){
        int i = 0;
        try {
            i = Integer.parseInt(numb);
        }catch (Exception e){
            Log.i("Log3","number is: " +  numb);
        }
        return numberDelimeter(i);
    }


    public static String getMinimumLengthPrice(float price){
        return fmt(price);
    }

    public static String getMinimumLengthPrice(Double price){


        return fmt(price);
    }
    public static String getMinimumLengthPrice(String price){
        String sPrice = String.valueOf(price);
        int index = sPrice.indexOf(".");
        Double priced = 0.;
        try {
            priced = Double.parseDouble(price);
        }catch (Exception e){

        }
        return fmt(priced);
    }

    private static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else{
            String x = "0";
            try {
                x = Double.toString(d);
            }catch (Exception e){

            }
            return new BigDecimal(x).toPlainString();//String.format("%s",d);
        }
    }

    public static String getToman(double d){
        long x = (long) d;
        if(x == 0){
            return "0";
        }else{
            return StringHelper.putDelimeters(String.valueOf(x));
        }
    }



    public static String getStringwithDelimeters(String s){
        return s.replace(",","");
    }


    /*public static void showSnackBar(Context context, String message,AlertOkInterface alertOkInterface){

        try{
            Dialog alertDialog = new Dialog(context);
            alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.alert_caution);

            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setCancelable(true);
            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);
            alertDialog.show();
            TextView txt = alertDialog.findViewById(R.id.txt_message);
            txt.setText(message);
            MaterialButton btnDismiss = alertDialog.findViewById(R.id.alert_dismiss);
            btnDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }catch (Exception e){

        }

    }*/


   /* public static void showSnackBar(Context context, String message){

        try{
            Dialog alertDialog = new Dialog(context);
            alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.alert_caution);

            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setCancelable(true);
            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);
            alertDialog.show();
            TextView txt = alertDialog.findViewById(R.id.txt_message);
            txt.setText(message);
            MaterialButton btnDismiss = alertDialog.findViewById(R.id.alert_dismiss);
            btnDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }catch (Exception e){

        }

    }*/

    private static String numberDelimeter(int numb){
        String string = "";
        int length = 0;
        while(numb>0){
            length++;
            string = numb%10 +string;
            if(length%3==0&&numb/10!=0){
                string = "," + string;
            }
            numb /= 10;
        }
        return string;
    }


    public static String getDateFromText(String txt){
        String date = "";String time = "";

        int date_index = txt.indexOf("T");
        int time_index = txt.indexOf(".");



        date = txt.substring(0,date_index);
        date = date.replace("-","");

        time = txt.substring(date_index+1,time_index);

        int hour = 0,minute = 0,second = 0;
        try {
            hour = Integer.parseInt(time.substring(0,2));
            minute = Integer.parseInt(time.substring(3,5));
            second = Integer.parseInt(time.substring(6,8));
        }catch (Exception e){

        }
        int extra_hour = 0;
        if(minute + 30 > 60)
            extra_hour = 1;
        minute = (minute + 30)%60;
        int extra_day = 0;
        hour = hour+ extra_hour;
        if(hour+3>24)
            extra_day = 1;
        hour = (hour + 3)%24;




        int year=0,month=0,day =0;
        try {
            year = Integer.parseInt(date.substring(0,4));
            month = Integer.parseInt(date.substring(4,6));
            day = Integer.parseInt(date.substring(6,8));
        }catch (Exception e){

        }
        day = day + extra_day;
        JalaliCalendar.YearMonthDate jalali_date = JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(year,month,day));
        date = jalali_date.toString();
        return date;
    }

    public static String getTimeFromText(String txt){
        String time = "";
        Log.i("Log29",txt);

        int date_index = txt.indexOf("T");
        int time_index = txt.indexOf(".");

        time = txt.substring(date_index+1,time_index);

        int hour = 0,minute = 0,second = 0;
        try {
            hour = Integer.parseInt(time.substring(0,2));
            minute = Integer.parseInt(time.substring(3,5));
            second = Integer.parseInt(time.substring(6,8));
        }catch (Exception e){

        }
        int extra_hour = 0;
        if(minute + 30 > 60)
            extra_hour = 1;
        minute = (minute + 30)%60;
        hour = hour+ extra_hour;
        hour = (hour + 3)%24;

        String min = minute>=10? minute + "": "0"+minute;
        String hou = hour>=10? hour + "": "0"+hour;
        String sec = second>=10? second + "": "0"+second;
        time = hou+":"+min+":"+sec;
        return time;
    }



    //change model from what is given from server model to whats suitable for database
    /*public static MarketsDE createMarketFromModel(MarketListModel model){
        MarketsDE marketsDE = new MarketsDE();
        marketsDE.pk = model.getPk();
        String marketFullName = model.getMarket();
        int mid = marketFullName.indexOf("/");
        String firstHalf = marketFullName.substring(0,mid);
        String secondHalf = marketFullName.substring(mid+1);

        mid = firstHalf.indexOf("|");
        marketsDE.main_market = firstHalf.substring(0,mid);
        marketsDE.main_market_en = firstHalf.substring(mid+1);

        mid = secondHalf.indexOf("|");
        marketsDE.second_market = secondHalf.substring(0,mid);
        marketsDE.second_market_en = secondHalf.substring(mid+1);
        return marketsDE;
    }
*/


}
