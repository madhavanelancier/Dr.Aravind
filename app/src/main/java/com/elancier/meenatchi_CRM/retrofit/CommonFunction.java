package com.elancier.meenatchi_CRM.retrofit;


import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CommonFunction {

    public static boolean checkLogin(String resp) {
        try{
            JSONObject json = new JSONObject(resp);
            Log.i("resp", resp+"");
            if(json.getBoolean("success") == true){
                return true;
            }else if(json.getBoolean("success") == false){
                if(json.has("data")){
                    JSONTokener jsonTok = new JSONTokener(json.getString("data"));
                    JSONObject jObj = new JSONObject(jsonTok);
                    if(jObj.has("redirectToLogin") && jObj.getBoolean("redirectToLogin") == true){
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    return true;
                }
            }else{
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
    }

    public static InputStream createfalseJson(){
        try{
            JSONObject json = new JSONObject();
            json.put("success", false);
            JSONObject lo = new JSONObject();
            lo.put("redirectToLogin", true);
            json.put("data", lo);
            return new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



}

