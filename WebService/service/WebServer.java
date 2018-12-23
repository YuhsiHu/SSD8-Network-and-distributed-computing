package service;

import javax.xml.ws.Endpoint;

/**
 * @author Hu Yuxi
 * @date 2018-12-21
 *
 */
public class WebServer {
	public static void main(String[] args) throws Exception{
		Endpoint.publish("http://localhost:8080/ListImp",
				new ListImp());
		System.out.println("WebService start");
		System.out.println("Addressַ:http://localhost:8080/ListImp?wsdl");
	}
	
}
