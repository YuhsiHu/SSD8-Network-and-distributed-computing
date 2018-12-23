package Exam1__HTTPProxyServer;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 
 * @author Hu Yuxi
 * @date 2018-12-14
 */
public class HttpProxyHandler implements Runnable {

	private SocketChannel socketChannel;
	private static String rootPath = "D:/ProxyServer"; // Root
	

	private static String aimHost = "127.0.0.1";
	private static int aimPort = 80;
	private static int buffer_size = 8192;		
	private static String CRLF = "\r\n";
	static PrintWriter screen = new PrintWriter(System.out, true);
	
	/**
	 * Initialize the root path
	 * @param socketChannel
	 */
	public HttpProxyHandler(SocketChannel socketChannel) {
		File path = new File(rootPath);
		if(!path.isDirectory()){
			System.err.println("root path is not a directory!");
		}
		this.socketChannel = socketChannel;
	}

	/**
	 * Begin to run
	 */
	public void run() {
		handle(socketChannel);
	}

	/**
	 * Handle
	 * @param socketChannel
	 */
	public void handle(SocketChannel socketChannel) {
		try {
			Socket socket = socketChannel.socket();

			DataInputStream reader = new DataInputStream((socket.getInputStream()));
			OutputStream out = socket.getOutputStream();

			System.out.println("Socket address and port is:" + socket.getInetAddress() + ":" + socket.getPort());

			ByteBuffer buffer = ByteBuffer.allocate(1024);
			socketChannel.read(buffer);
			buffer.flip();
			String request = decode(buffer);
			System.out.println("request:"+request);
			System.out.println(socket.getInetAddress() + ":" + socket.getPort() + ">\n" + request);
			String[] requestArray = request.split("\r\n");
			request = requestArray[0];
			System.out.println("requestArray[0]:"+request);
		
			String[] wordArray = request.split("\r\n");		
			String cmd = wordArray[0];
			System.out.println("cmd is:"+cmd);
			wordArray = request.split(" ");					//ַ
			String filePath = wordArray[1];
			filePath = filePath.substring(7);
			System.out.println("file path:"+filePath);
			String[] pathItem = filePath.split("/");
			String[] hostItem = pathItem[0].split("\\:");
			//debug
			System.out.println("path Item:"+pathItem.length);
			System.out.println("pathItem[0]:"+pathItem[0]);
					
			String fileName = "/"+pathItem[1];
			aimHost = hostItem[0];
			if(hostItem.length==2){
				aimPort = Integer.parseInt(hostItem[1]);
			}
			
		
			HttpClient myClient = new HttpClient();
			myClient.connect(aimHost, aimPort);
			
			//handle the GET 
			System.out.println("Handle the GET, the request is:"+request);
			if (request.startsWith("GET")) {				
				request = "GET " + fileName;
				myClient.processGetRequest(request);
				screen.println("Header: \n");
				String header = myClient.getHeader();
				screen.print(header + "\n");
				screen.flush();
				
				String[] headerItem = header.split(" ");
				//int stat = Integer.parseInt(headerItem[1]);
				int stat=200;
				if(stat == 200){													
					FileOutputStream outfile = new FileOutputStream(rootPath+fileName);
					doTrans(reader, out, fileName);
				} else {															
					System.out.println("no such file. request complete.");
					sendMessage(1,-1,-1);
				}
			} else {										
				StringBuffer sb = new StringBuffer(
						"HTTP/1.0 400 Bad Request\r\n");
				sb.append("Content-Type:text/html\r\n\r\n");
				socketChannel.write(encode(sb.toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socketChannel != null)
					socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param reader
	 * @param out
	 * @param requestPath
	 * @throws Exception
	 */
	private void doTrans(DataInputStream reader, OutputStream out,
			String requestPath) throws Exception {
		int fileType = -1;
		
		
		String[] wordArray = requestPath.split("\\.");
		String type = wordArray[wordArray.length - 1];
		if (type.equals("html")) {
			fileType = 0;
		} else {
			fileType = 1;
		}
		
		if (new File(rootPath + requestPath).exists()) {
			sendMessage(0, fileType, -1);
			InputStream fileIn = new FileInputStream(rootPath + requestPath);
			byte[] buf = new byte[fileIn.available()];
			fileIn.read(buf);
			out.write(buf);
			out.close();
			fileIn.close();
			reader.close();
			System.out.println("request complete.");
		} 
	}

	private Charset charset = Charset.forName("GBK");

	/**
	 * 
	 * @param buffer
	 * @return
	 */
	public String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = charset.decode(buffer);
		return charBuffer.toString();
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public ByteBuffer encode(String str) {
		return charset.encode(str);
	}

	/**
	 * 
	 * @param stat
	 * @param type
	 * @param size
	 * @throws IOException
	 */
	private void sendMessage(int stat, int type, long size) throws IOException {
		StringBuffer sb = new StringBuffer();
		switch (stat) {
		
		case 0:
			sb.append("HTTP/1.0 200 OK\r\n");
			if (type == 0) {
				sb.append("text/html\r\n\r\n");
			} else {
				sb.append("image/jpeg\r\n\r\n");
			}
			socketChannel.write(encode(sb.toString()));
			break;
		
		case 1:
			sb.append("HTTP/1.0 404 Not Found\r\n\r\n");
			socketChannel.write(encode(sb.toString()));
			break;
		}

	}
}
