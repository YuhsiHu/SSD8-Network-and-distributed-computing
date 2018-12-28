package server;

import iface.MyInterface;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * drive a server
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class MyServer {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MyInterface service = new MyImpl();
			LocateRegistry.createRegistry(8421);
			Naming.bind("rmi://127.0.0.1:8421/RMIMessage", service);
			System.out.println("Now Message Center is serving");
		} catch (RemoteException e) {
			System.out.println("Remote Error");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.out.println("AlreadyBound Error");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("URL Error");
			e.printStackTrace();
		}
	}
}
