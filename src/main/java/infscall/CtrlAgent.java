package infscall;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import basic.CommonTool;
import basic.RESTRequest;

public class CtrlAgent {
	
	private static final String sysTmp = CommonTool.formatDirWithSep(System.getProperty("java.io.tmpdir"));
	
	private boolean bug = false; 
	
	private String serverIP = null;
	
	private String appID = "123";  ///default value
	
	private String objectType = null;
	
	private ArrayList<String> objects = null; 
	
	public CtrlAgent init(String serverIP){
		if(serverIP == null || serverIP.trim().equals("")){
			bug = true;
			return this;
		}
			
		
		this.serverIP = serverIP;
		
		if(testOnline())
			return this;
		else{
			bug = true;
			return this;
		}
	}
	
	public CtrlAgent init(){
		File ctrlInfoFile = new File(sysTmp + "ctrl.info");
		if(!ctrlInfoFile.exists()){
			bug = true;
			return this;
		}
		this.serverIP = null;
		try {
			serverIP = FileUtils.readFileToString(ctrlInfoFile, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			bug = true;
			return this;
		}
		
		if(testOnline())
			return this;
		else{
			bug = true;
			return this;
		}
	}
	
	public CtrlAgent setAppID(String appID){
		this.appID = appID;
		return this;
	}
	
	public CtrlAgent setObjType(String objType){
		String ot = objType.trim().toLowerCase();
		if(!ot.equals("vm") && !ot.equals("subtopology"))
		{
			bug = true;
			return this;
		}
		this.objectType = ot;
		return this;
		
	}
	
	public CtrlAgent addObjet(String obj){
		if(objects == null)
			objects = new ArrayList<String>();
		objects.add(obj.trim());
		return this;
	}
	
	public String exeIC(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", this.appID);
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/ctrl/execute", params);
	}
	
	public String provision(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		if(this.objects == null || this.objectType == null || this.objects.size() == 0)
			return null;
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement("request");
		Element appIdEle = root.addElement("AppID");
		Element objTypeEle = root.addElement("ObjectType");
		Element objEle = root.addElement("Objects");
		appIdEle.addText(this.appID);
		objTypeEle.addText(this.objectType);
		String objstr = this.objects.get(0);
		for(int oi = 1 ; oi<this.objects.size() ; oi++)
			objstr += ("||" + this.objects.get(oi));
		objEle.addText(objstr);
		String xmlString = doc.asXML();
		return RESTRequest.postXML(serverIP, 8080, "CloudsStormCA/ctrl/provision", xmlString);
	}
	
	public String delete(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		if(this.objects == null || this.objectType == null || this.objects.size() == 0)
			return null;
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement("request");
		Element appIdEle = root.addElement("AppID");
		Element objTypeEle = root.addElement("ObjectType");
		Element objEle = root.addElement("Objects");
		appIdEle.addText(this.appID);
		objTypeEle.addText(this.objectType);
		String objstr = this.objects.get(0);
		for(int oi = 1 ; oi<this.objects.size() ; oi++)
			objstr += ("||" + this.objects.get(oi));
		objEle.addText(objstr);
		String xmlString = doc.asXML();
		return RESTRequest.postXML(serverIP, 8080, "CloudsStormCA/ctrl/delete", xmlString);
	}
	
	public boolean checkExeStatus(String exeID){
		if(this.appID == null)
			return false;
		Map<String, String> params = new HashMap<String, String>();
		params.put("exeid", exeID);
		params.put("appid", appID);
		String ret = RESTRequest.get(serverIP, 8080, "CloudsStormCA/ctrl/check", params);
		if(ret == null)
			return false;
		else 
			return true;
	}
	
	public String deleteCtrl(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appID);
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/ctrl/delete/ctrl", params);
	}
	
	public String deleteAll(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appID);
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/ctrl/delete/all", params);
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
