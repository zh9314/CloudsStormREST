import infscall.CtrlAgent;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import basic.RESTRequest;


public class testREST {

	public static void main(String[] args) {
		/*Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "123");
		String ret = RESTRequest.get("145.100.133.145", 8080, "CloudsStormCA/ctrl/execute", params);
		System.out.println(ret);*/
		CtrlAgent ctrlAgent = new CtrlAgent();
		String ret ;
		if( ( ret = ctrlAgent.init("145.100.133.145").setObjType("subTopology").addObjet("data_src_2").provision() ) == null ){
			System.out.println("Infrastructure code is not executed properly!");
			return ;
		}
		System.out.println(ret);
		/*if( ctrlAgent.init("145.100.133.145").checkExeStatus("0") )
			System.out.println("Success");
		else 
			System.out.println("Fail");*/
	}

}
