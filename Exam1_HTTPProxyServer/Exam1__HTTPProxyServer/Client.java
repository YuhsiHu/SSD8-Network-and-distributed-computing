package Exam1__HTTPProxyServer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Client {
	
	private static int port = 8000;				
	private static int buffer_size = 8192;		
	private static String CRLF = "\r\n";		

	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(
			System.in));						
	static PrintWriter screen = new PrintWriter(System.out, true);	

	public static void main(String[] args) throws Exception {
		try {
			//initialize
			HttpClient myClient = new HttpClient();

			//root path
			if (args.length != 1) {
				System.err.println("Usage: Client <server>");
				System.exit(0);
			}

			//add root path
			myClient.connect(args[0]);		
			
			screen.println(args[0] + " is listening to your request:");
			String request = keyboard.readLine()+"\r\n";
			request += keyboard.readLine()+"\r\n";
			request += keyboard.readLine();

			if (request.startsWith("GET")) {
				myClient.processGetRequest(request);	
			} else {
				screen.println("Bad request! \n");		
				myClient.close();
				return;
			}

			screen.println("Header: \n");
			screen.print(myClient.getHeader() + "\n");
			screen.flush();

			if (request.startsWith("GET")) {
				//handle the GET on console and save file
				screen.println();
				screen.print("Enter the name of the file to save: ");
				screen.flush();
				String filename = keyboard.readLine();
				FileOutputStream outfile = new FileOutputStream(filename);
				String response = myClient.getResponse();
				outfile.write(response.getBytes("iso-8859-1"));
				outfile.flush();
				outfile.close();
			}
			myClient.close();					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
