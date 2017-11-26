package com.example.kamil.transapp;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.content.Context;

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
 * Created by Kamil on 25.11.2017.
 */



public class BackgroundWorker extends AsyncTask<String,Void,String> {

    private ResultCheck mListener;
    Context context;
    AlertDialog alertDialog;
    String type;

    BackgroundWorker (Context ctx, ResultCheck mListener){
        context = ctx;
        this.mListener = mListener;
    }

    @Override
    protected String doInBackground(String... params) {


        String login_url = "http://limitlessgames.pl/login.php"; // standard address localhost
        String addUser_url = "http://limitlessgames.pl/register.php";
        type = params[0];

        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";


                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                result = result.replaceAll("\\s", "");
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("register"))
        {
            try {
                String user_name = params[1];
                String password = params[2];
                String worker_type = params[3];
                URL url = new URL(addUser_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8") +"&"
                        + URLEncoder.encode("workerType", "UTF-8")+"="+URLEncoder.encode(worker_type,"UTF-8");

                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";


                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                result = result.replaceAll("\\s", "");
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");


    }

    @Override
    protected void onPostExecute(String result) {

        if(type.equals("login"))
        {
            if(result.equals("error")) {

                alertDialog.setMessage("Wrong login or password.");
                alertDialog.show();
            }
            else
            {
                if (mListener != null)
                    mListener.myMethod(result);
            }
        }
        else if(type.equals("register"))
        {
            if(result.equals("error"))
            {
                alertDialog.setMessage("Register unsuccessful");
                alertDialog.show();
            }
            else
            {
                alertDialog.setMessage("Register successful");
                alertDialog.show();
                if (mListener != null)
                    mListener.myMethod(result);
            }
        }


    }

    private boolean myMethod(boolean value) {
        //handle value
        return value;
    }

    @Override
    protected void onProgressUpdate(Void... params) {
    }

}
