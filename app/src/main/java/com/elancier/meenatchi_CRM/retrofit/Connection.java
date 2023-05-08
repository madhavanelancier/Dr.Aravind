package com.elancier.meenatchi_CRM.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class Connection {

    public InputStream getConnection(String stringUrl, String sess){
        InputStream respStream = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 15000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 15000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            //DefaultHttpClient client = new DefaultHttpClient();
            HttpClient client = new DefaultHttpClient(httpParameters);
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            HttpGet request = new HttpGet(stringUrl);
            request.setHeader("X-Requested-With", "XMLHttpRequest");
            request.setHeader("SWF-AuthToken", sess);
            request.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
            HttpResponse con = client.execute(request);
            con.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
            //logger2.info(stringUrl+" status code "+ con.getStatusLine().getStatusCode());
            if(con.getStatusLine().getStatusCode() == 401){
                respStream = CommonFunction.createfalseJson();
            }else{
                respStream = con.getEntity().getContent();
            }
            //logger2.info(stringUrl+" conn resp "+ respStream);
        } catch (Exception e) {
            e.printStackTrace();
            //logger2.info(stringUrl+"errorgetconnection "+ e.getMessage());
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            //logger2.error("connec Exception:"+stackTrace);
            Log.i("Connection", "error :"+e.getMessage());
        }
        return respStream;

    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        InputStreamReader is = new InputStreamReader(stream);
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();
        while(read != null) {
            sb.append(read);
            read =br.readLine();

        }
        return sb.toString();
    }

    public String connStringResponse(String stringUrl, String session, Context context) {
        //AndroidLogger logger = AndroidLogger.getLogger(context, "08760429-7068-49f9-b15a-2cdce9e6b977", false);
        //logger.info("connStringResponse entered "+stringUrl);
        InputStream stream;
        String respString = null;
        try {
            stream = getConnection(stringUrl, session);
            respString = readIt(stream);
            //logger.info("connStringResponse "+respString);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("connStringResponse error"+e.getMessage());

            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            //logger.error("connStringResponse Exception:"+stackTrace);
            Log.i("Result", "error :" + e.getMessage());
        }
        //logger.info("connStringResponse exited "+stringUrl);
        return respString;
    }
    public String readIt1(InputStream stream) throws IOException, UnsupportedEncodingException {
        InputStreamReader is = new InputStreamReader(stream);
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();
        while(read != null) {
            sb.append(read);
            read =br.readLine();

        }
        return sb.toString();
    }

    public String connStringResponse1(String stringurl)throws RemoteException {
        InputStream stream;
        String respString = null;
        try {
            stream = getConnection(stringurl);
            respString = readIt(stream);
        } catch (Exception e) {
            Log.i("Result", "error :" + e.getMessage());
            throw new RemoteException();
        }
        return respString;
    }

    @SuppressLint("NewApi")
    public InputStream getConnection(String stringUrl) throws RemoteException {
        InputStream respStream = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 15000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 15000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            //DefaultHttpClient client = new DefaultHttpClient();
            HttpClient client = new DefaultHttpClient(httpParameters);
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            HttpGet request = new HttpGet(stringUrl);
            request.setHeader("X-Requested-With", "XMLHttpRequest");/*
	        request.setHeader("X-AuthToken", AppConstants.sessionData);*/
            HttpResponse con = client.execute(request);
            if(con.getStatusLine().getStatusCode() == 401){
                respStream = CommonFunction.createfalseJson();
            }else{
                respStream = con.getEntity().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Connection", "error :"+e.getMessage());
            throw new RemoteException();
        }
        return respStream;

    }

    public String sendHttpPostjson(String url, String nameValuePairs, String session) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 15000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 150000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpPost post = new HttpPost(url);
        post.setHeader("SWF-AuthToken", session);
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        String result = null;
        try {
            post.setEntity(new StringEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 401)
                result = readIt(CommonFunction.createfalseJson());
            else
                result = readIt(response.getEntity().getContent());
        } catch (IOException e) {
            throw e;
        }
        return result;
    }


    public String sendHttpPostjson(String url, JSONObject jsonvalue) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 15000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 15000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpPost post = new HttpPost(url);/*
	    post.setHeader("SWF-AuthToken", session);*/
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");

        post.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
        String result = null;
        try {
            post.setEntity(new StringEntity(jsonvalue.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            result = readIt(response.getEntity().getContent());
        } catch (IOException e) {
            throw e;
        }
        return result;
    }

    public String sendHttpPostjson2(String url, JSONObject jsonvalue, String session) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 60000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 60000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        //DefaultHttpClient client = new DefaultHttpClient();
        HttpClient client = new DefaultHttpClient(httpParameters);
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
        // Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        HttpPost post = new HttpPost(url);
        //post.setHeader("SWF-AuthToken", session);
        //post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");

        //  post.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
        String result = null;
        try {
            post.setEntity(new StringEntity(jsonvalue.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 401){
                result =readIt(CommonFunction.createfalseJson());
            }else{
                result = readIt(response.getEntity().getContent());
            }
        } catch (IOException e) {
            throw e;
        }
        return result;
    }

    public static StringBuffer sendJsonHttpPost(String url, JSONObject nameValuePairs) throws IOException {
        StringBuffer result = null;
        try {
            HttpURLConnection conn = null;
            DataInputStream inStream = null;
            try{
                URL servletURL = new URL(url);
                conn = (HttpURLConnection) servletURL.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");//application/json
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(nameValuePairs.toString());
                osw.flush();
                osw.close();
                inStream = new DataInputStream( conn.getInputStream() );
                result = new StringBuffer();
                int chr;
                while ((chr=inStream.read())!=-1) {
                    result.append((char) chr);
                }
            }catch (IOException ioex){
                throw ioex;
            }
            catch(Exception e){
                throw e;
            }finally{
                try{
                    if(inStream!=null)inStream.close();
                }catch(Exception e){
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }



    public static StringBuffer sendJsonHttpPut(String url, JSONObject nameValuePairs) throws IOException {
        StringBuffer result = null;
        try {
            HttpURLConnection conn = null;
            DataInputStream inStream = null;
            try{
                URL servletURL = new URL(url);
                conn = (HttpURLConnection) servletURL.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(nameValuePairs.toString());
                osw.flush();
                osw.close();
                inStream = new DataInputStream( conn.getInputStream() );
                result = new StringBuffer();
                int chr;
                while ((chr=inStream.read())!=-1) {
                    result.append((char) chr);
                }
            }catch (IOException ioex){
                throw ioex;
            }
            catch(Exception e){
                throw e;
            }finally{
                try{
                    if(inStream!=null)inStream.close();
                }catch(Exception e){
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }


    public static int sendJsonHttpDel(String url) throws IOException {
        int code =0;
        try {
            HttpURLConnection conn = null;
            DataInputStream inStream = null;
            try{
                URL servletURL = new URL(url);
                conn = (HttpURLConnection) servletURL.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
               /* conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");*/
                conn.setRequestProperty("X-HTTP-Method-Override", "DELETE");
                conn.setRequestMethod("POST");
                code = conn.getResponseCode();
            }catch (IOException ioex){
                ioex.printStackTrace();
                throw ioex;
            }
            catch(Exception e){
                throw e;
            }finally{
                try{
                    if(inStream!=null)inStream.close();
                }catch(Exception e){
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return code;
    }
    public String connStringResponse(String stringUrl)throws RemoteException {
        InputStream stream;
        String respString = null;
        try {
            stream = getConnection(stringUrl);
            respString = readIt(stream);
        } catch (Exception e) {
            Log.i("Result", "error :" + e.getMessage());
            throw new RemoteException();
        }
        return respString;
    }

}





