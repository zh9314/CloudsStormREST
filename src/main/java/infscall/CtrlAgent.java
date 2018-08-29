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

import basic.RESTRequest;

public class CtrlAgent {
	
	private static final String sysTmp = File.separator + "tmp" + File.separator;   ///currently only in Unix mode
	
	private boolean bug = false; 
	
	private String serverIP = null;
	
	private String appID = "123";  ///default value
	
	private boolean logOn = true;
	
	private String objectType = null;
	
	private ArrayList<String> objects = null; 
	
	private ArrayList<String> cmds = null;
	
	private ArrayList<HScalingRequest> hRequests = null;
	
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
		String rawServerIP = null;
		try {
			rawServerIP = FileUtils.readFileToString(ctrlInfoFile, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			bug = true;
			return this;
		}
		
		this.serverIP = rawServerIP.replace("\n", "").trim();
		if(testOnline()){
			return this;
		}
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
	
	public CtrlAgent setLog(boolean logOp){
		this.logOn = logOp;
		return this;
	}
	
	public CtrlAgent addObjet(String obj){
		if(objects == null)
			objects = new ArrayList<String>();
		objects.add(obj.trim());
		return this;
	}
	
	public CtrlAgent addCMD(String cmd){
		if(cmds == null)
			cmds = new ArrayList<String>();
		cmds.add(cmd.trim());
		return this;
	}
	
	public CtrlAgent addHScalingReq(HScalingRequest newReq){
		if(hRequests == null)
			hRequests = new ArrayList<HScalingRequest>();
		hRequests.add(newReq);
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
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/rest/ctrl/execute", params);
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
		
		reset();
		String xmlString = doc.asXML();
		System.out.println(xmlString);
		return RESTRequest.postXML(serverIP, 8080, "CloudsStormCA/rest/ctrl/provision", xmlString);
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
		
		reset();
		String xmlString = doc.asXML();
		return RESTRequest.postXML(serverIP, 8080, "CloudsStormCA/rest/ctrl/delete", xmlString);
	}
	
	public String execute(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		if(this.objects == null || this.objectType == null || this.objects.size() == 0)
			return null;
		if(this.cmds == null || this.cmds.size() == 0)
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
		if(!this.logOn){
			Element logEle = root.addElement("Log");
			logEle.addText("false");
		}
		Element cmdsEle = root.addElement("CMDs");
		for(int ci = 0 ; ci<cmds.size() ; ci++){
			Element cmdEle = cmdsEle.addElement("CMD"+ci);
			cmdEle.addText(cmds.get(ci));
		}
		reset();
			
		String xmlString = doc.asXML();
		System.out.println(xmlString);
		return RESTRequest.postXML(serverIP, 8080, "CloudsStormCA/rest/ctrl/execute/cmd", xmlString);
	}
	
	public String hscale(){
		if(bug)
			return null;
		if(this.serverIP == null)
			return null;
		if(this.appID == null)
			return null;
		if(this.hRequests == null || this.hRequests.size() == 0)
			return null;
		
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement("request");
		Element appIdEle = root.addElement("AppID");
		appIdEle.addText(this.appID);
		
		Element reqsEle = root.addElement("ScaleReq");
		for(int ri = 0 ; ri < hRequests.size() ; ri++){
			HScalingRequest curReq = hRequests.get(ri);
			Element reqEle = reqsEle.addElement("ScaleReq"+ri);
			Element objTypeEle = reqEle.addElement("ObjectType");
			objTypeEle.addText(curReq.targetObjectType);
			Element objsEle = reqEle.addElement("Objects");
			objsEle.addText(curReq.targetObjects);
			if(curReq.cloudProvider != null){
				Element cpEle = reqEle.addElement("CP");
				cpEle.addText(curReq.cloudProvider);
			}
			if(curReq.dataCentre != null){
				Element dcEle = reqEle.addElement("DC");
				dcEle.addText(curReq.dataCentre);
			}
			if(curReq.scaledSTName != null){
				Element nameEle = reqEle.addElement("ScaledSTName");
				nameEle.addText(curReq.scaledSTName);
			}
			if(curReq.scalingDirection != null){
				Element sdEle = reqEle.addElement("OutIn");
				sdEle.addText(curReq.scalingDirection);
			}
		}
		reset();
		
		String xmlString = doc.asXML();
		System.out.println(xmlString);
		return RESTRequest.postXML(serverIP, 8080, "CloudsStormCA/rest/ctrl/hscale", xmlString);
	}
	
	public boolean checkExeStatus(String exeID){
		if(this.appID == null)
			return false;
		Map<String, String> params = new HashMap<String, String>();
		params.put("exeid", exeID);
		params.put("appid", appID);
		String ret = RESTRequest.get(serverIP, 8080, "CloudsStormCA/rest/ctrl/check", params);
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
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/rest/ctrl/delete/ctrl", params);
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
		return RESTRequest.get(serverIP, 8080, "CloudsStormCA/rest/ctrl/delete/all", params);
	}
	
	////timeOut in seconds
	public boolean waitInfras(String exeID, int timeOut){
		if(this.serverIP == null)
			return false;
		if(this.appID == null)
			return false;
		if(exeID == null)
			return false;
		int count = 0;
		while(true){
			try {
				if( this.checkExeStatus(exeID) )
					return true;
				count++;
				if(count*2 > timeOut)
					return false;

				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		
	}
	
	private void reset(){
		bug = false;
		logOn = true;
		objectType = null;
		if(objects != null)
			objects.clear();
		if(cmds != null)
			cmds.clear();
		if(hRequests != null)
			hRequests.clear();
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
		String ret = RESTRequest.get(serverIP, 8080, "CloudsStormCA/rest/ctrl/test", null);
		if(ret == null)
			return false;
		else
			return true;
	}

}
