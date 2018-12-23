package HTTPServer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * Class <em>HttpClient</em> is a class representing a simple HTTP client.
 *
 * @author wben
 */

public class HttpClient {

	/**
	 * default HTTP port is port 81
	 */
	private static int port = 81;

	/**
	 * Allow a maximum buffer size of 8192 bytes
	 */
	private static int buffer_size = 8192;

	/**
	 * Response is stored in a byte array.
	 */
	private byte[] buffer;

	/**
	 * My socket to the world.
	 */
	Socket socket = null;

	/**
	 * Default port is 81.
	 */
	private static final int PORT = 81;

	/**
	 * Output stream to the socket.
	 */
	BufferedOutputStream ostream = null;

	/**
	 * Input stream from the socket.
	 */
	BufferedInputStream istream = null;

	/**
	 * StringBuffer storing the header
	 */
	private StringBuffer header = null;

	/**
	 * StringBuffer storing the response.
	 */
	private StringBuffer response = null;
	
	/**
	 * String to represent the Carriage Return and Line Feed character sequence.
	 */
	static private String CRLF = "\r\n";

	/**
	 * HttpClient constructor;
	 */
	public HttpClient() {
		buffer = new byte[buffer_size];
		header = new StringBuffer();
		response = new StringBuffer();
	}

	/**
	 * <em>connect</em> connects to the input host on the default http port --
	 * port 80. This function opens the socket and creates the input and output
	 * streams used for communication.
	 */
	public void connect(String host) throws Exception {

		/**
		 * Open my socket to the specified host at the default port.
		 */
		socket = new Socket(host, PORT);

		/**
		 * Create the output stream.
		 */
		ostream = new BufferedOutputStream(socket.getOutputStream());

		/**
		 * Create the input stream.
		 */
		istream = new BufferedInputStream(socket.getInputStream());
	}

	/**
	 * <em>processGetRequest</em> process the input GET request.
	 */
	public void processGetRequest(String request) throws Exception {
		/**
		 * Send the request to the server.
		 */
		request += CRLF + CRLF;
		buffer = request.getBytes();
		ostream.write(buffer, 0, request.length());
		ostream.flush();
		/**
		 * waiting for the response.
		 */
		processResponse();
	}
	
	/**
	 * <em>processPutRequest</em> process the input PUT request.
	 */
	public void processPutRequest(String request) throws Exception {
		//=======start your job here============//
		String[] split = request.split(" ");//获取request的每一段信息
		if(split.length!=3){
			System.out.println("Bad Request");
			close();
		}else if(split[2].equals("HTTP/1.0") || split[2].equals("HTTP/1.1")){
			String filepath = "C:\\Users\\admin\\Desktop";
			filepath = filepath + split[1].replaceAll("/","\\\\");//定义文件传输路径
			File file = new File(filepath);
			if(file.exists()) {
			//包装PUT报文
				StringBuilder putMessage = new StringBuilder();
				putMessage.append(request);
				putMessage.append(CRLF);
				if(split[1].endsWith(".jpg") || split[1].endsWith(".jpeg")){
					putMessage.append("Content-Type: image/jpeg;charset=ISO-8859-1" + CRLF);
				}else if(split[1].endsWith(".html") || split[1].endsWith(".htm")){
					putMessage.append("Content-Type: text/html;charset=ISO-8859-1" + CRLF);
				}else{
					System.out.println("Invalid file type");
					close();
				}
				putMessage.append("Content-Length: "+file.length()+CRLF);
				putMessage.append(CRLF);
				//send to server
				String message = putMessage+"";
				buffer = message.getBytes("ISO-8859-1");
				ostream.write(buffer,0,message.length());
				ostream.flush();
				System.out.println("**********Message send to server**********"+message+"********************");
				//read file and send it to server
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				int length = 0;
				byte[] sendInfo = new byte[buffer_size];
				while((length = bis.read(sendInfo))!=-1){
					ostream.write(sendInfo,0,length);
					ostream.flush();
				}
				
			}else{
				System.out.println("File does not exist");
			}
		}else{
			System.out.println("Bad Request");
			close();
		}
		System.out.println("**********HTTP Client process PUT Request finished**********");
		processResponse();
		//=======end of your job============//
	}
	
	/**
	 * <em>processResponse</em> process the server response.
	 * 
	 */
	public void processResponse() throws Exception {
		int last = 0, c = 0;
		/**
		 * Process the header and add it to the header StringBuffer.
		 */
		boolean inHeader = true; // loop control
		System.out.println("***********inHeader: "+inHeader);
		while (inHeader && ((c = istream.read()) != -1)) {
			//System.out.println("*****C****"+c+"****");
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
			System.out.println("***********Header:"+header);
		}
		System.out.println(header);
		System.out.println("***********Get Header While finished**********");
		/**
		 * Read the contents and add it to the response StringBuffer.
		 */
		int len=0;
		int num=0;
		while ((len=istream.read(buffer)) != -1) {
			//response.append(new String(buffer, "UTF-8"));		
			//读取真实字节数后append
			response.append(new String(buffer,0, len));
			num++;
			System.out.println(num);
			//System.out.println("*********"+response.toString());
		}
	}

	/**
	 * Get the response header.
	 */
	public String getHeader() {
		return header.toString();
	}

	/**
	 * Get the server's response.
	 */
	public String getResponse() {
		return response.toString();
	}

	/**
	 * Close all open connections -- sockets and streams.
	 */
	public void close() throws Exception {
		socket.close();
		istream.close();
		ostream.close();
	}
}
