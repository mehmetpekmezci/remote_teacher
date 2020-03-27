package network;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import userInterface.ApplicationContextVariables;

public class FileUpload {

	/*
	public static void send(String fileName,long shapeID,String extension) throws MalformedURLException, IOException{
		String urlToConnect = "http://"+Constants.getServerIp()+"/rteacher/rteacher/php/fileupload.php";
		File fileToUpload = new File(fileName);
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

		
		URLConnection connection = new URL(urlToConnect).openConnection();
		connection.setDoOutput(true); // This sets request method to POST.
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
		    writer.println("--" + boundary);
		    writer.println("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\""+shapeID+"."+extension+"\"");
		    writer.println("Content-Type: application/octect-stream; charset=UTF-8");
		    writer.println();
		    BufferedReader reader = null;
		    try {
		        reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload), "UTF-8"));
		        
		        for (String line; (line = reader.readLine()) != null;) {
		            writer.println(line);
		        }
		    } catch(Exception e){
		    	e.printStackTrace();
		    }finally {
		        if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {logOrIgnore.printStackTrace();}
		    }
		    writer.println("--" + boundary + "--");
		} finally {
		    if (writer != null) writer.close();
		}
		int responseCode = ((HttpURLConnection) connection).getResponseCode();
		System.out.println(responseCode); // Should be 200
	}
	
	*/
	
	
//	public static void send(String fileName,long shapeID,String extension) throws MalformedURLException, IOException{
//		String urlToConnect = "http://"+Constants.getServerIp()+"/rteacher/rteacher/php/fileupload.php";
//		File fileToUpload = new File(fileName);
//		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
//
//		
//		
//        System.out.println("Reading image from disk. ");
//        BufferedImage img= ImageIO.read(new File(fileName));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        
//        ImageIO.write(img, extension, baos);
//        baos.flush();
//        
//        byte[] bytes = baos.toByteArray();
//        baos.close();
//        
//        System.out.println("Sending image to server. ");
//        URLConnection connection = new URL(urlToConnect).openConnection(); 
//        connection.setDoOutput(true); // This sets request method to POST.
//		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        OutputStream out = connection.getOutputStream(); 
//        DataOutputStream dos = new DataOutputStream(out);
//        
//        dos.writeInt(bytes.length);
//        dos.write(bytes, 0, bytes.length);
//        
//        System.out.println("Image sent to server. ");
//
//        dos.close();
//        out.close();
//		
//		
//		
//	}
	
	
	
	
	public  void send(String fileName,long shapeID,String extension,String serverIp,String userName,String sessionid) {
		String url = "http://"+serverIp+"/rteacher/rteacher/php/fileupload.php?username="+userName+"&sessionid="+sessionid;
		File fileToUpload = new File(fileName);
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 HttpPost httppost = new HttpPost(url);
	        try
	        {
	           
	                FileBody bin = new FileBody(fileToUpload,ContentType.MULTIPART_FORM_DATA,shapeID+"."+extension);

	                HttpEntity reqEntity = MultipartEntityBuilder.create()
	                        .addPart("fileToUpload", bin)
	                        .build();


	                httppost.setEntity(reqEntity);

	                System.out.println("executing request " + httppost.getRequestLine());
	                CloseableHttpResponse response = httpclient.execute(httppost);
	                try {
	                    System.out.println("----------------------------------------");
	                    System.out.println(response.getStatusLine());
	                    HttpEntity resEntity = response.getEntity();
	                    if (resEntity != null) {
	                        System.out.println("Response content length: " + resEntity.getContentLength());
	                    }
	                    EntityUtils.consume(resEntity);
	                } finally {
	                    response.close();
	                }
	            } catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
	                try {
						httpclient.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }

	        
	        
	}

}

