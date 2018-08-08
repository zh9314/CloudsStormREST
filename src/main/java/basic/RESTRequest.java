package basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			System.out.println(repCode);
			if ( repCode >= 200 && repCode <= 299) 
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			else {
			    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			    success = false;
			}
			
			String line;
			while ((line = br.readLine()) != null) 
				output += line + "\n";
			
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
