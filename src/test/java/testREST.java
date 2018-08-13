import infscall.CtrlAgent;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import basic.RESTRequest;


public class testREST {

	public static void main(String[] args) {
		CtrlAgent ctrlAgent = new CtrlAgent();
		String exeID ;
		if( ( exeID = ctrlAgent.init("145.100.133.145").setObjType("VM")
					.addObjet("hadoop_3nodes_1.Node1")
					.addObjet("hadoop_3nodes_1.Node2")
					.addObjet("hadoop_3nodes_1.Node3")
					.addCMD("/root/hadoop/sbin/hadoop-daemon.sh stop datanode")
					.addCMD("/root/hadoop/sbin/yarn-daemon.sh stop nodemanager").execute() ) == null ){
		System.out.println("Infrastructure code is not executed properly!");
		return ;
		}
		System.out.println("'exeid' = "+exeID);
		if(ctrlAgent.waitInfras(exeID, 400))
			System.out.println("CMDs Finished!");
		else
			System.out.println("CMDs Failed!");
		/*if( ( exeID = ctrlAgent.init("145.100.133.145").setObjType("subTopology").addObjet("data_src_2").provision() ) == null ){
			System.out.println("Infrastructure code is not executed properly!");
			return ;
		}
		System.out.println(exeID);
		if(ctrlAgent.waitInfras(exeID, 300))
			System.out.println("Finished!");
		else
			System.out.println("Failed!");*/
		/*if( ctrlAgent.init("145.100.133.145").checkExeStatus("1534075474574") )
			System.out.println("Success");
		else 
			System.out.println("Fail");*/
	}

}
