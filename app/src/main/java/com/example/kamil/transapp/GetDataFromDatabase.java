package com.example.kamil.transapp;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Adams on 2017-11-29.
 */

public class GetDataFromDatabase extends AsyncTask<String,Void,String>  {

    Context ctx;
    private AlertDialog Message;
    private AsyncResponse delegate = null;

    GetDataFromDatabase(Context ctx,AsyncResponse delegate){
        this.ctx = ctx;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... dataToGet) {


        String getDataURL = "http://limitlessgames.pl/getDataFromDatabase.php";
        String post_data = "";
        String result = "";
        String line = "";

            try {

                URL url = new URL(getDataURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter tablesToGet = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                post_data +=  URLEncoder.encode("table_name", "UTF-8") + "=" + URLEncoder.encode(dataToGet[0], "UTF-8") + "&" //set table to get data from
                + URLEncoder.encode("column_name", "UTF-8") + "=" + URLEncoder.encode(dataToGet[1], "UTF-8") + "&"
                        + URLEncoder.encode("condition", "UTF-8") + "=" + URLEncoder.encode(dataToGet[2], "UTF-8") + "&"
                        + URLEncoder.encode("pattern", "UTF-8") + "=" + URLEncoder.encode(dataToGet[3], "UTF-8") + "&";

                Log.d("tag",post_data);
                tablesToGet.write(post_data);

                tablesToGet.flush();
                tablesToGet.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader valueFromTables = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));


                while ((line = valueFromTables.readLine()) != null) {
                    result += line;
                }

                result = result.replaceAll("\\s", "");
                valueFromTables.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return result;
    }

    @Override
    protected void onPreExecute() {

       // Message = new AlertDialog.Builder(ctx).create();
       // Message.setTitle("Returning Info !"); --debug

    }

    @Override
    protected void onPostExecute(String result) {

       // Message.setMessage(result);
        //Message.show(); --debug

        if (delegate != null)
            delegate.returnResult(result);

    }




}
