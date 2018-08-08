package infscall;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import basic.CommonTool;
import basic.RESTRequest;

public class CtrlAgent {
	
	private static final String sysTmp = CommonTool.formatDirWithSep(System.getProperty("java.io.tmpdir"));
	
	private String serverIP = null;
	
	public CtrlAgent init(String serverIP){
		if(serverIP == null || serverIP.trim().equals(""))
			return null;
		
		this.serverIP = serverIP;
		
		if(testOnline())
			return this;
		else
			return null;
	}
	
	public CtrlAgent init(){
		File ctrlInfoFile = new File(sysTmp + "ctrl.info");
		if(!ctrlInfoFile.exists())
			return null;
		this.serverIP = null;
		try {
			serverIP = FileUtils.readFileToString(ctrlInfoFile, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		if(testOnline())
			return this;
		else
			return null;
	}
	
	public String exeIC(String appID){
		if(this.serverIP == null)
			return null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appID);
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/ctrl/execute", params);
	}
	
	
	private boolean testOnline(){
		boolean online = test();
		int count = 0;
		while(!online){
			try {
				Thread.sleep(2000);
				online = test();
				count++;
				if(count > 15)
					return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	
	private boolean test(){
		String ret = RESTRequest.get(serverIP, 8080, "CloudsStormCA/ctrl/test", null);
		if(ret == null)
			return false;
		else
			return true;
	}

}
