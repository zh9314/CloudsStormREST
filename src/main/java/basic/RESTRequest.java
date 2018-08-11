package basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RESTRequest {
	
	public static String get(String serverIP, int port, String urlPrefix, Map<String, String> params) {
		String output = "";
		boolean success = true;
		try {
			String urlStr = "http://"+serverIP+":"+port+"/"+urlPrefix;
			boolean fp = true;
			if(params != null)
				for(Map.Entry<String, String> entry : params.entrySet()){
					if(fp){
						urlStr += ("?"+entry.getKey().trim()+"="+entry.getValue().trim());
						fp = false;
					}
					else
						urlStr += ("&"+entry.getKey().trim()+"="+entry.getValue().trim());
				}
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader br = null;
			
			int repCode = conn.getResponseCode();
			if ( repCode >= 200 && repCode <= 299) {
				InputStream inputStream = conn.getInputStream();
				if(inputStream != null)
					br = new BufferedReader(new InputStreamReader(inputStream));
				 
			}
			else {
				InputStream inputStream = conn.getErrorStream();
				if(inputStream != null)
					br = new BufferedReader(new InputStreamReader(inputStream));
			    success = false;
			}
			
			if(br != null){
				String line;
				while ((line = br.readLine()) != null) 
					output += line + "\n";
			}
			
			conn.disconnect();
		}catch (IOException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
		if(success)
			return output;
		else
			return null;
	}
	
	
	public static String postXML(String serverIP, int port, String urlPrefix, String xmlString) {
		String output = "";
		boolean success = true;
		try {
			String urlStr = "http://"+serverIP+":"+port+"/"+urlPrefix;
			
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");
			
			OutputStream os = conn.getOutputStream();
			os.write(xmlString.getBytes());
			os.flush();

			BufferedReader br = null;
			
			int repCode = conn.getResponseCode();
			if ( repCode >= 200 && repCode <= 299) {
				InputStream inputStream = conn.getInputStream();
				if(inputStream != null)
					br = new BufferedReader(new InputStreamReader(inputStream));
				 
			}
			else {
				InputStream inputStream = conn.getErrorStream();
				if(inputStream != null)
					br = new BufferedReader(new InputStreamReader(inputStream));
			    success = false;
			}
			
			if(br != null){
				String line;
				while ((line = br.readLine()) != null) 
					output += line + "\n";
			}
			
			conn.disconnect();
		}catch (IOException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
		if(success)
			return output;
		else
			return null;
	}

}
