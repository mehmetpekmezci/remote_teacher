package com.remote_teacher.deneme002;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class LoginActivity extends ActionBarActivity  {

    AlertDialog.Builder alertDialogBuilder = null;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=LoginActivity.this.getSharedPreferences("RTEACHER_SHOT_PREFERENCES",0);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    public void loginButtonAction(View v) {
        EditText userNameInputText=(EditText)findViewById(R.id.userNameInputText);
        EditText passwordInputText=(EditText)findViewById(R.id.passwordInputText);
        String userName=userNameInputText.getText().toString();
        String password=passwordInputText.getText().toString();


        String URLString="https://10.0.2.2/rteacher/rteacher/php/mobileLogin.php?username="+userName+"&password="+password;
        URL loginURL=null;
        try {
            loginURL=new URL(URLString);
        } catch (MalformedURLException e) {
            showAlertDialog("URL:"+URLString+" is mal formed.");
            return;
            //e.printStackTrace();
        }

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            showAlertDialog("TLS SSLContext algorithm does not exists.");
            return;
            //e.printStackTrace();
        }
        if(ctx!=null){
            try {
                ctx.init(null, new TrustManager[] {
                        new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                        }
                }, null);
            } catch (KeyManagementException e) {
                showAlertDialog("Trust Key Management Exception");
                return;
            }
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        HttpsURLConnection urlConnection=null;
        try {
            urlConnection = (HttpsURLConnection) loginURL.openConnection();
        }catch(Exception e) {
            e.printStackTrace();
            showAlertDialog("Can not open connection to URL: "+URLString);
        }
        InputStream inputStream=null;
        try {
            inputStream = urlConnection.getInputStream();
        }catch(Exception e) {
            e.printStackTrace();
            showAlertDialog("Can not get input stream of connection "+urlConnection);
        }
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                if(line.trim().equals("TRUE")){
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putString("email",userName);
                    editor.putString("password",password);
                    editor.commit();
                    Intent listIntent = new Intent(LoginActivity.this, ListActivity.class);
                    startActivityForResult(listIntent, 0);
                }else if(line.trim().equals("FALSE")) {
                    showAlertDialog("User name or password is not true !");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            showAlertDialog("Error Reading from Server retry again " + e.getMessage() );

        }


    }


    private void showAlertDialog(String errorMessage){
        alertDialogBuilder.setMessage(errorMessage);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNeutralButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
