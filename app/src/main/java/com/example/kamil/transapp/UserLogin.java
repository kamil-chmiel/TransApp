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
 * Created by Kamil on 25.11.2017.
 *  Login,Register and Unregister service
 */



public class UserLogin extends AsyncTask<String,Void,String> {

    private AsyncResponse delegate = null;
    Context context;
    private AlertDialog alertDialog;
    private String type;

    UserLogin(Context ctx, AsyncResponse delegate){
        context = ctx;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {


        String login_url = "http://limitlessgames.pl/login.php";
        String addUser_url = "http://limitlessgames.pl/register.php";
        String removeUser_url = "http://limitlessgames.pl/unregister.php";
        StringBuilder temp = new StringBuilder(51); //longest info in database is VARCHAR 51
        String result= "";
        String line;
        type = params[0];


        switch (type) {
            case "login": {
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

                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));


                    while ((line = bufferedReader.readLine()) != null) {
                        temp.append(line);
                    }

                    result = temp.toString();
                    result = result.replaceAll("\\s", "");
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "register": {
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

                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                            + URLEncoder.encode("worker_type", "UTF-8") + "=" + URLEncoder.encode(worker_type, "UTF-8");

                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));


                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    result = result.replaceAll("\\s", "");
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "unregister": {
                try {

                    String user_name = params[1];
                    URL url = new URL(removeUser_url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));


                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    result = result.replaceAll("\\s", "");
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }


        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle("Action Status!");

    }

    @Override
    protected void onPostExecute(String result) {


        switch (type) {
            case "login": {
                if (result.equals("error")) {

                    alertDialog.setMessage("Wrong username or password. ");
                    alertDialog.show();
                } else {
                    if (delegate != null)
                        delegate.returnResult(result);
                }
                break;
            }

            case"register": {
                if (result.equals("error")) {
                    alertDialog.setMessage("Register unsuccessful!");
                    alertDialog.show();
                } else {
                    alertDialog.setMessage("Register successful!");
                    alertDialog.show();
                    if (delegate != null)
                        delegate.returnResult(result);
                }
                break;
            }

            case "unregister": {
                if (result.equals("error")) {
                    alertDialog.setMessage("Deleting user unsuccessful!");
                    alertDialog.show();
                } else {
                    alertDialog.setMessage("Deleting user successful!");
                    alertDialog.show();
                    if (delegate != null)
                        delegate.returnResult(result);
                }
                break;
            }
        }



    }


    @Override
    protected void onProgressUpdate(Void... params) {
    }

}
