package com.remote_teacher.deneme002;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.fragment.Dimension;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class ListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    AlertDialog.Builder alertDialogBuilder = null;
    SharedPreferences sharedPreferences;
    SessionList sessionList=new SessionList();
    String userName=null;
    String password=null;
    File photoFile = null;
    String imageFileName="";
    int imageWidth=0;
    int imageHeight=0;

    private static final int CAMERA_REQUEST = 1888;
    static final int REQUEST_TAKE_PHOTO = 1;

    Intent cameraIntent=null;

    ProgressDialog progressDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alertDialogBuilder = new AlertDialog.Builder(ListActivity.this);
        super.onCreate(savedInstanceState);
        sharedPreferences=ListActivity.this.getSharedPreferences("RTEACHER_SHOT_PREFERENCES",0);
        setContentView(R.layout.activity_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        fillSessionList();
        if(sessionList.size()>0){
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, sessionList.getAsStringList());
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(this);
        }else{
            showAlertDialog("No Schedule Exists TODAY");
        }

    }

    public void fillSessionList() {
        userName=sharedPreferences.getString("email",null);
        password=sharedPreferences.getString("password",null);


        String URLString="https://10.0.2.2/rteacher/rteacher/php/mobileSessionValidator.php?username="+userName+"&password="+password;
        URL loginURL=null;
        try {
            loginURL=new URL(URLString);
        } catch (MalformedURLException e) {
            showAlertDialog("URL:"+URLString+" is mal formed.");
            return ;
            //e.printStackTrace();
        }

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            showAlertDialog("TLS SSLContext algorithm does not exists.");
            return ;
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
                return ;
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
            while ((line = bufferedReader.readLine()) != null) {
                Session session =new Session();
                String[] lineSplitted=line.trim().split("#");
                if(lineSplitted!=null && lineSplitted.length>1){
                    session.setHour("Course Start Hour : "+lineSplitted[0]);
                    session.setSessionId(lineSplitted[1]);
                    if(lineSplitted[4].trim().equals("127.0.0.1"))lineSplitted[4]="10.0.2.2";
                    session.setIp(lineSplitted[4]);
                    sessionList.addSession(session);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            showAlertDialog("Error Reading from Server retry again " + e.getMessage() );

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedHour=((TextView)view).getText().toString();
        sessionList.select(selectedHour);

        cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);



        if (cameraIntent.resolveActivity(getPackageManager()) != null){
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            if (photoFile != null) {
                uploadFile(photoFile);
            }
        }
    }

    private File createImageFile() throws IOException {

        File storageDirectory=new File(Environment.getExternalStorageDirectory(),"rteacher");
        boolean storageDirOk=false;
        if(!storageDirectory.exists()){
            storageDirOk=storageDirectory.mkdirs();
        }else{
            storageDirOk=true;
        }
        if (!storageDirOk) {
            String errorMessage="Storage Directory Can Not Be Created :"+Environment.getExternalStorageDirectory()+"/rteacher/";
            showAlertDialog(errorMessage);
            throw new IOException(errorMessage);
        }
        imageFileName = ""+System.currentTimeMillis();
        File image = new File(storageDirectory+"/"+imageFileName+".jpg");
        if(!image.exists()){
            image.createNewFile();
        }

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public int uploadFile(final File fileToUpload) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

//Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(fileToUpload.getAbsolutePath(), options);
        imageWidth = options.outWidth;
        imageHeight = options.outHeight;


        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int serverResponseCode=-1;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        String URLString="https://10.0.2.2/rteacher/rteacher/php/fileupload.php?username="+userName+"&sessionid="+sessionList.getSelectedSession().getSessionId();
        int maxBufferSize = 1 * 1024 * 1024;
        try {
                FileInputStream fileInputStream = new FileInputStream(fileToUpload);


                URL url = new URL(URLString);
// Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("fileToUpload", fileToUpload.getAbsolutePath());
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='fileToUpload';filename='"
                                + fileToUpload.getAbsolutePath() + "'" + lineEnd);
                        dos.writeBytes(lineEnd);
// create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
// read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
// send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
// Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    showAlertDialog("Image Upload Completed.");
                    sendMessageToServer(sessionList.getSelectedSession(),imageFileName);
                }
//close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                showAlertDialog("MalformedURLException, URL:"+URLString+"  ErrorMessage:"+ex.getMessage());
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                showAlertDialog("Exception, ErrorMessage:"+e.getMessage());
                Log.e("Exception", "Exception : "
                        + e.getMessage(), e);
            }

            return serverResponseCode;

    }

    public void sendMessageToServer(Session session,String imageid) throws IOException {
        Socket socket = new Socket(session.getIp(), 2010);
        OutputStream out = socket.getOutputStream();
        PrintWriter output = new PrintWriter(out);
        output.print(session.getSessionId().trim() + " " + imageid.trim()+" "+imageWidth+" "+imageHeight);
        output.flush();
        out.flush();
        out.close();
        socket.close();
    }


}
