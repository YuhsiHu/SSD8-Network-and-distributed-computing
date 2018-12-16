package Exam1__HTTPProxyServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;
import org.omg.CORBA.Environment;

/**
 * 
 * @author Hu Yuxi
 *
 */
public class HttpClient {

	private static int port = 8000;			
	private static int buffer_size = 8192;	
	private byte[] buffer;					
	
	Socket socket = null;
	private static final int PORT = 8000;		
	BufferedOutputStream ostream = null;
	BufferedInputStream istream = null;

	private StringBuffer header = null;		
	private StringBuffer response = null;	
	
	static private String CRLF = "\r\n";	

	
	public HttpClient() {
		buffer = new byte[buffer_size];
		header = new StringBuffer();
		response = new StringBuffer();
	}

	
	public void connect(String host) throws Exception {

		socket = new Socket(host, PORT);	//
		ostream = new BufferedOutputStream(socket.getOutputStream());	//
		istream = new BufferedInputStream(socket.getInputStream());		
	}
	
	public void connect(String host, int port) throws Exception {
		socket = new Socket(host, port);	
		ostream = new BufferedOutputStream(socket.getOutputStream());	
		istream = new BufferedInputStream(socket.getInputStream());		
	}

	
	public void processGetRequest(String request) throws Exception {
		request += CRLF + CRLF;
		buffer = request.getBytes();
		ostream.write(buffer, 0, request.length());
		ostream.flush();
		processResponse();				
	}
	

	public void processPutRequest(String request) throws Exception {	
		
		String[] wordArray = request.split(" ");
		String fileName = wordArray[1];
		String filePath = System.getProperty("user.dir") + fileName;
		File file = new File(filePath); 
		if(!file.exists()){
			System.out.println("No such file");
			return;
		}
        
		String fsize = String.valueOf(file.length()); 
		BufferedInputStream iFileStream = new BufferedInputStream(new FileInputStream(file));
		request += CRLF + CRLF + fsize + CRLF + CRLF;
		buffer = request.getBytes();
		ostream.write(buffer, 0, request.length());

		InputStream fileIn = new FileInputStream(file);
		byte[] buf = new byte[fileIn.available()];
		fileIn.read(buf);
		ostream.write(buf);
		
		iFileStream.close();
		ostream.flush();
		socket.shutdownOutput();
		processResponse();					
	}
	
	
	public void processResponse() throws Exception {
		int last = 0, c = 0;
		
	
		boolean inHeader = true;
		while (inHeader && ((c = istream.read()) != -1)) {
			switch (c) {
			case '\r':
				break;
			case '\n':
				if (c == last) {
					inHeader = false;
					break;
				}
				last = c;
				header.append("\n");
				break;
			default:
				last = c;
				header.append((char) c);
			}
		}

		
		while (istream.read(buffer) != -1) {
			response.append(new String(buffer,"iso-8859-1"));
		}
	}


	public String getHeader() {
		return header.toString();
	}

	
	public String getResponse() {
		return response.toString();
	}


	public void close() throws Exception {
		socket.close();
		istream.close();
		ostream.close();
	}
}
