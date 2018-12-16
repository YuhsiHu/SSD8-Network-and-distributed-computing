package Exam1__HTTPProxyServer;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 
 * @author Hu Yuxi
 *
 */
public class Handler implements Runnable {

	private SocketChannel socketChannel;
	private static String rootPath = "D:/Server"; // Root path

	/**
	 * Initialize handler
	 * @param socketChannel
	 */
	public Handler(SocketChannel socketChannel) {
		File path = new File(rootPath);
		if(!path.isDirectory()){
			System.err.println("root path");
		}
		this.socketChannel = socketChannel;
	}
	
	/**
	 * Begin to handle
	 */
	public void run() {
		handle(socketChannel);
	}

	/**
	 * 
	 * @param socketChannel
	 */
	public void handle(SocketChannel socketChannel) {
		try {
			Socket socket = socketChannel.socket();

			DataInputStream reader = new DataInputStream((socket.getInputStream()));
			OutputStream out = socket.getOutputStream();

			System.out.println("Socket address and port:" + socket.getInetAddress() + ":"+ socket.getPort());

			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			socketChannel.read(buffer);
			buffer.flip();
			String request = decode(buffer);
			String[] requestArray = request.split("\r\n");
			String showReq = requestArray[0];
			System.out.println(socket.getInetAddress() + ":" + socket.getPort() + ">" + showReq);

			//handle HTTP message
			String[] wordArray = request.split(" ");
			String cmd = wordArray[0];
			String fileName = "";
			switch (cmd) {
			case "GET":
				fileName = wordArray[1].substring(0, wordArray[1].length() - 4);
				doGet(reader, out, fileName);
				break;
			case "PUT":
				fileName = wordArray[1];
				wordArray = fileName.split("\r\n");
				fileName = wordArray[0];
				String fileSize = wordArray[2];
				doPut(reader, out, buffer, fileName, fileSize);
				break;
			default:
				StringBuffer sb = new StringBuffer("HTTP/1.0 400 Bad Request\r\n");
				sb.append("Content-Type:text/html\r\n\r\n");
				socketChannel.write(encode(sb.toString()));
				break;
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
	 * Set the charset
	 */
	private Charset charset = Charset.forName("GBK");

	/**
	 * 
	 * @param buffer
	 * @return decode
	 */
	public String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = charset.decode(buffer);
		return charBuffer.toString();
	}

	/**
	 * 
	 * @param str
	 * @return encode
	 */
	public ByteBuffer encode(String str) {
		return charset.encode(str);
	}

	/**
	 * 
	 * @param reader
	 * @param out
	 * @param requestPath
	 * @throws Exception
	 */
	private void doGet(DataInputStream reader, OutputStream out, String requestPath) throws Exception {
		int fileType = -1;
		
		//set the file type
		String[] wordArray = requestPath.split("\\.");
		String type = wordArray[wordArray.length - 1];
		if (type.equals("html")) {
			fileType = 0;
		} else {
			fileType = 1;
		}
		
		//if file exists
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
		}else {
			//if file does not exist
			sendMessage(1, fileType, -1);
			out.close();
			reader.close();
			System.out.println("No such file. Request complete.");
		}
	}


	/**
	 * 
	 * @param reader
	 * @param out
	 * @param buffer
	 * @param requestPath
	 * @param fileSize
	 * @throws Exception
	 */
	private void doPut(DataInputStream reader, OutputStream out, ByteBuffer buffer, String requestPath, String fileSize) throws Exception {

		StringBuffer file = new StringBuffer();
		byte[] b = new byte[8194];
		int reqStat = -1;
		int fileType = -1;

		long size = Long.parseLong(fileSize);

		long times = size / 8194;
		if (size % 8194 != 0)
			times++;
		for (int i = 0; i < times; i++) {
			if (reader.read(b) != -1) {
				file.append(new String(b, "iso-8859-1"));
			}
		}

		//only handle the html web page
		String[] wordArray = requestPath.split("\\.");
		String type = wordArray[wordArray.length - 1];
		if (type.equals("html")) {
			fileType = 0;
		} else {
			fileType = 1;
		}

	
		File f = new File(rootPath + requestPath);
		if (f.exists()) {
			f.delete();
			reqStat = 2;
		} else {
			reqStat = 3;
		}

		sendMessage(reqStat, fileType, size);

		FileOutputStream outfile = new FileOutputStream(f); 
		outfile.write(file.toString().getBytes("iso-8859-1"));
		outfile.flush();
		outfile.close();
		System.out.println("Request complete.");
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
		
		case 2:
			sb.append("HTTP/1.0 200 OK\r\n");
			sb.append("Content-Type:");
			if (type == 0) {
				sb.append("text/html\r\n");
			} else {
				sb.append("image/jpeg\r\n");
			}
			sb.append("Content-Length:" + size + "\r\n\r\n");
			socketChannel.write(encode(sb.toString()));
			break;
	
		case 3:
			sb.append("HTTP/1.0 201 Created\r\n");
			sb.append("Content-Type:");
			if (type == 0) {
				sb.append("text/html\r\n");
			} else {
				sb.append("image/jpeg\r\n");
			}
			sb.append("Content-Length:" + size + "\r\n\r\n");
			socketChannel.write(encode(sb.toString()));
			break;
		}

	}
}
