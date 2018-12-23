package Exam1__HTTPProxyServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpProxyServer {
	private static String host = "127.0.0.1"; //IP
	private static int port = 8000; //HTTP port

	private ServerSocketChannel serverSocketChannel = null;
	private ExecutorService executorService;
	private static final int POOL_MULTIPLE = 4;

	/**
	 * Initialize the Server
	 * @throws IOException
	 */
	public HttpProxyServer() throws IOException {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_MULTIPLE);
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
		System.out.println("127.0.0.1:8000");
	}

	/**
	 * Begin to service
	 */
	public void service() {
		while (true) {  
	          SocketChannel socketChannel=null;  
	          try {  
	            socketChannel = serverSocketChannel.accept();  
	            executorService.execute(new HttpProxyHandler(socketChannel));  
	          }catch (IOException e) {  
	             e.printStackTrace();  
	          }  
	        }  
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		new HttpProxyServer().service();
	}
}
