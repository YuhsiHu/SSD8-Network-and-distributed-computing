package FileSystem;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * File System
 * @author Huyuxi
 * @date 2018-11-16
 */
public class Handler implements Runnable {

    private Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    PrintWriter pw;
//    private final String rootPath = "D:\\";
//    public static String tempPath = "D:\\";

    private String rootPath=null;
    private static String tempPath=null;
    private static final String HOST = "127.0.0.1";//服务器IP
    private static final int UDP_PORT = 2020;//UDP服务器端口
    private static final int TCP_PORT = 2021;//TCP服务器端口
    private static final int SENDSIZE = 1024;
    DatagramSocket dgsocket;//UDP
    SocketAddress socketAddres;

    /**
     * 
     * @param socket
     * 
     */
    public Handler(Socket socket,String path) {
        this.socket = socket;
        this.rootPath=path+"\\"; 	
    	this.tempPath=this.rootPath;
    }

    /**
     * initialize root path
     * @param path
     */
    public void setRootPath(String path) {
    	this.rootPath=path+"\\"; 	
    	this.tempPath=this.rootPath;
    }
    /**
     * initialize the stream
     * @throws IOException
     */
    public void initStream() throws IOException {
    	// 输入流，读取信息
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 输出流，写信息
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         // 装饰输出流，true,每写一行就刷新输出缓冲区，不用flush
        pw = new PrintWriter(bw, true);
    }

    /**
     * 实现Runnable接口
     * 多线程
     * @return 
     */
    public synchronized void run() {
        try {
        	//initialize stream
            initStream();
            //初始化消息
            String ipport = "客户端IP地址:" + socket.getInetAddress() + "客户端端口号:" + socket.getPort() + "> 连接成功";
            pw.println(ipport);
            String info = null;
            //get command
            while (null != (info = br.readLine())) {
            	System.out.println("info"+info);
            	//quit 
                if (info.equals("bye")) {
                	pw.println("Disconnect successfully!");
                    break;
                } else {
                    switch (info) {
                        case "ls":
                        	System.out.println("temppath:  "+tempPath);
                            ls(tempPath);
                            break;
                        case "cd":
                            String dir = null;
                            if (null != (dir=br.readLine())) {
                            	cd2dir(dir);
                            } else {
                                pw.println("please input a direction after cd");
                            }
                            break;
                        case "cd..":
                        	cd2up();
                            break;
                        case "get":
                            String fileName = br.readLine();
                            sendFile(fileName);
                            break;
                        default:
                            pw.println("unknown cmd");
                    }
                    pw.println("CmdEnd");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != socket) {
                try {
                    br.close();
                    bw.close();
                    pw.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * get <file> 通过UDP下载指定文件，保存到客户端当前目录下
     * @param fileName
     * @throws SocketException
     * @throws IOException
     * @throws InterruptedException
     */
    private void sendFile(String fileName) throws SocketException, IOException,InterruptedException {
        //判断文件是否存在
        if (!isFileExist(fileName)) {
            pw.println(-1);
            return;
        }
        //文件存在
        File file = new File(tempPath + "\\" + fileName);
        pw.println(file.length());

        //UDP传输
        dgsocket = new DatagramSocket();
        socketAddres = new InetSocketAddress(HOST, UDP_PORT);
        DatagramPacket dp;

        byte[] sendInfo = new byte[SENDSIZE];
        int size = 0;
        dp = new DatagramPacket(sendInfo, sendInfo.length, socketAddres);
        BufferedInputStream bfdIS = new BufferedInputStream(new FileInputStream(file));

        while ((size = bfdIS.read(sendInfo)) > 0) {
            dp.setData(sendInfo);
            dgsocket.send(dp);
            sendInfo = new byte[SENDSIZE];
            //确保发送顺序
            TimeUnit.MICROSECONDS.sleep(1);
        }
        bfdIS.close();
        dgsocket.close();
    }
    
    /**
     * 
     * 返回至root
     * 
     */
    private void cd2up() {
        if (tempPath.equals(rootPath)) {
            pw.println("The current path is the root path.");
        } else {
            for (int i = tempPath.length(); i > 0; i--) {
                if (tempPath.substring(i - 1, i).equals("\\")) {
                	tempPath = tempPath.substring(0, i - 1);
                    pw.println((new File(tempPath)).getName() + " > OK");
                    break;
                }
            }
        }
    }

    /**
     * 
     * ls 服务器返回当前目录文件列表（<file/dir>	 name size） 
     * @param currentPath
     * 
     */
    private void ls(String currentPath) {
        File rootFile = new File(currentPath);
        File[] fileList = rootFile.listFiles();
        System.out.println(currentPath);
        int MaxLength = 40;
        for (File file : fileList) {
        	System.out.println(file.getName());
            if (file.isFile()) {           	
                pw.println("<file>" + "\t" + file.getName()
                        + addSpace(MaxLength - file.getName().length())
                        + (file.length()/1000) + "KB");
            } else if (file.isDirectory()) {
                pw.println("<dir>" + "\t" + file.getName()
                        + addSpace(MaxLength - file.getName().length())
                        + (file.length()/1000) + "KB");
            }
        }
        return;
    }

    /**
     * 文件名后加空格对齐
     * @param count
     * @return space str
     */
    public static String addSpace(int count) {
        String str = "";
        for (int i = 0; i < count; i++) {
            str += " ";
        }
        return str;
    }

    /**
     * cd <dir> 进入指定目录（需判断目录是否存在，并给出提示）
     * @param dir
     */
    private void cd2dir(String dir) {
        Boolean isExist = false;
        Boolean isDir = true;
        File rootFile = new File(tempPath);
        System.out.println(tempPath);
        File[] fileList = rootFile.listFiles();
        //扫描当前路径下是否存在这个目录
        for (File file : fileList) {
            if (file.getName().equals(dir)) {
                isExist = true;
                if (file.isDirectory()) {
                    isDir = true;
                    break;
                } else {
                    isDir = false;
                    pw.println("You cannot cd file, only directory admitted");
                }
            }
        }
        //是合法的文件夹，可以进入，并更改当前路径
        if (isExist && isDir) {
        	tempPath = tempPath + "\\" + dir;
            pw.println(dir + " > OK");
        } else if (isDir && (!isExist)) {
            pw.println("Sorry, "+ dir + " directory does not exist!");
        }
    }

    /**
     * 
     * @param fileName
     * @return isFileExist
     */
    public static boolean isFileExist(String fileName) {
        boolean isExist = false;

        File rootFile = new File(tempPath);
        File[] fileList = rootFile.listFiles();

        for (File file:fileList){
            if (file.getName().equals(fileName) && file.isFile()){
                isExist = true;
            }
        }
        return isExist;
    }
}

