package FileSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import FileSystem.Handler;

/**
 * File System
 * 
 * @author Huyuxi
 * @date 2018-11-16
 */
public class FileServer extends Thread{
	private static final String HOST = "127.0.0.1";// 服务器IP
	private static final int UDP_PORT = 2020;// UDP port
	private static final int TCP_PORT = 2021;// TCP port
	static File root;
	ServerSocket serverSocket;// TCP
	DatagramSocket socket;// UDP
	ExecutorService executorService;
	final int POOLSIZE = 4;// thread pool size
	private String path;

	/**
	 * 
	 * @throws IOException
	 */
	public FileServer() throws IOException {
		// create socket
		serverSocket = new ServerSocket(TCP_PORT, 2);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);
		System.out.println("Server started.");
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void service(String path) throws IOException {
		Socket socket = null;
		while (true) {
			try {
				// 等待并取出用户连接，并创建套接字
				socket = serverSocket.accept();				
				Handler handler = new Handler(socket,path);
				System.out.println(path);		
				executorService.execute(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param args
	 * @throws SocketException
	 * @throws IOException
	 * D:
	 */
	public static void main(String[] args) throws SocketException, IOException {
		// pass root directory by String[] args
		if (args.length != 1) {
			System.err.println("Please input your root path!");
			return;
		}

		// 判断参数是否有效
		try {
			root = new File(args[0]);
			if (!root.isDirectory()) {
				System.err
						.println(root.getAbsolutePath() + " does not exist or is not a directory，please input again!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Root : " + root.getAbsolutePath());
		// start server
		new FileServer().service(args[0]);
	}
}
