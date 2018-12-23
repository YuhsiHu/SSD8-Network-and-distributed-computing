package HTTPServer;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * HttpServer 
 * @author Hu Yuxi
 * @date 2018-12-07
 * 
 */
public class HttpServer {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 81;
    private static final int TCP = 2021;
    static File root;
    ServerSocket serverSocket;
    ExecutorService executorService;
    private final int POOLSIZE = 4;// 线程池容量
    private String rootpath;//用于获取服务器根路径

    /**
     * 构造函数的复写
     * @param args 传进来的rootpath
     * @throws IOException
     */
    public HttpServer(String[] args) throws IOException {
        serverSocket = new ServerSocket(PORT,2);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);
        rootpath = args[0];
        System.out.println(rootpath);
        System.out.println("服务器启动。");
    }


    public static void main(String[] args) throws IOException {
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
        new HttpServer(args).service();        //启动服务器
    }
    /**
     * 运行服务器
     */
    public void service(){
        Socket socket = null;
        while (true){
            try{
                //等待并取出用户连接，并创建套接字
                socket = serverSocket.accept();
                System.out.println(socket.getPort());
                System.out.println(socket.getLocalPort());
                HttpHandler httpHandler = new HttpHandler(socket,rootpath);
                System.out.println("**********New Socket**********");
                executorService.execute(httpHandler);//启动线程
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
