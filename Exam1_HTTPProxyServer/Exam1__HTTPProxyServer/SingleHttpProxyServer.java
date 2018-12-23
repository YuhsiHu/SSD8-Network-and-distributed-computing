package Exam1__HTTPProxyServer;
import java.lang.reflect.Constructor;
import java.net.*;
import java.io.*;

/**
 * 
 * @author Hu Yuxi
 * @date 2018-12-14
 *
 */
public class SingleHttpProxyServer extends Thread {

    static public int RETRIES = 5;//尝试连接次数
    static public int PAUSE = 5;//尝试间隔时间
    static public int TIMEOUT = 60;//超时时限
    static public int BUFSIZ = 1024;//输入缓冲大小
    static public boolean logging = false;//不记录已传输的数据
    static public OutputStream log = null;//输出日志流
    protected Socket socket;
    static private String parent = null;//父服务器
    static private int parentPort = -1;//父服务器端口号
    
    /**
     * Initialize
     * @param name
     * @param port
     */
    static public void setParentProxy(String name, int port) {
        parent = name;
        parentPort = port;
    }
    
    /**
     * Initialize
     * @param s
     */
    public SingleHttpProxyServer(Socket s) {
        //create proxy thread
        this.socket = s;
        //start proxy thread
        start();
    }
    /**
     * 
     * @param c
     * @param browser
     * @throws IOException
     */
    public void writeLog(int c, boolean browser) throws IOException {
        //output log
        log.write(c);
    }
    /**
     * 
     * @param bytes
     * @param offset
     * @param len
     * @param browser
     * @throws IOException
     */
    public void writeLog(byte[] bytes, int offset, int len, boolean browser) throws IOException {
        //output log
        for (int i = 0; i < len; i++)
            writeLog((int) bytes[offset + i], browser);
    }
    /**
     * 
     * @param url
     * @param host
     * @param port
     * @param sock
     * @return
     */
    public String printLog(String url, String host, int port, Socket sock) {
    	//log format
        java.text.DateFormat cal = java.text.DateFormat.getDateTimeInstance();
        System.out.println(cal.format(new java.util.Date()) + " - " + url + " " + sock.getInetAddress() + "\n");
        return host;
    }
    /**
     * Begin to run
     */
    public void run() {
        String line;
        String host;
        int port = 80;//port is 80
        Socket outbound = null;
        try {
        	//set timeout
            socket.setSoTimeout(TIMEOUT);
            InputStream is = socket.getInputStream();
            //create outputstream
            OutputStream os = null;
            try {
            	//get message line
                line = "";
                host = "";
                int state = 0;
                boolean space;
                while (true) {
                    //无限循环
                    int c = is.read();
                    //read from inputstream
                    if (c == -1)//nothing to read
                        break;
                    if (logging)
                    	//write log
                        writeLog(c, true);
                    //if c is space
                    space = Character.isWhitespace((char) c);
                    switch (state) {
                    //state
                    case 0:
                        if (space)
                        	//continue
                            continue;                   
                      //change the state
                        state = 1;                       
                    case 1:
                        if (space) {
                            state = 2;
                            //continue
                            continue;
                        }
                        //add message to line
                        line = line + (char) c;
                        break;
                    case 2:
                        if (space)
                            continue; 
                        //change state
                        state = 3;
                    case 3:
                        if (space) {
                        	//change state
                            state = 4;
                            //get url
                            String host0 = host;
                            int n;
                            n = host.indexOf("//");
                            //get url without HTTP/1.1 or HTTP/1.0
                            if (n != -1)
                                //not found
                                host = host.substring(n + 2);
                            n = host.indexOf('/');
                            if (n != -1)
                                //not found
                                host = host.substring(0, n);
                            n = host.indexOf(":");
                            //exist port
                            if (n != -1) {
                                //not found
                                port = Integer.parseInt(host.substring(n + 1));
                                host = host.substring(0, n);
                            }
                            host = printLog(host0, host, port, socket);
                            //获得网站域名
                            if (parent != null) {
                                host = parent;
                                port = parentPort;
                            }
                            int retry = RETRIES;
                            while (retry-- != 0) {
                                try {
                                    //创建连接到目标服务器
                                    outbound = new Socket(host, port);
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Can not create connection to："+e.getMessage());
                                }
                                //wait
                                Thread.sleep(PAUSE);
                            }
                            if (outbound == null)
                                break;
                            outbound.setSoTimeout(TIMEOUT);
                            //设置超时时间，防止read方法导致的阻塞
                            os = outbound.getOutputStream();
                            //获得输出流对象
                            os.write(line.getBytes());
                            //将信息写入流
                            os.write(' ');
                            os.write(host0.getBytes());
                            //将信息写入流
                            os.write(' ');
                            writeInfo(is, outbound.getInputStream(), os, socket
                                    .getOutputStream());
                            //调用方法将信息写入日志，套接字数据的交换
                            break;
                        }
                        host = host + (char) c;
                        break;
                    }
                }
            } catch (IOException e) {
            	
            }
        } catch (Exception e) {
        	
        } finally {
            try {
                socket.close();
            } catch (Exception e1) {
            	
            }
            try {
                outbound.close();
            } catch (Exception e2) {
            	
            }
        }
    }
    
    /**
     * 
     * @param is0
     * @param is1
     * @param os0
     * @param os1
     * @throws IOException
     */
    void writeInfo(InputStream is0, InputStream is1, OutputStream os0,
            OutputStream os1) throws IOException {
        //读取流中信息写入日志
        try {
            int ir;
            byte bytes[] = new byte[BUFSIZ];
            //创建字节数组，大小：1024
            while (true) {
                try {
                    if ((ir = is0.read(bytes)) > 0) {
                        //判断读取输入流的信息
                        os0.write(bytes, 0, ir);
                        //将读取的数据写入输出流对象中
                        if (logging)
                            writeLog(bytes, 0, ir, true);
                        //写入日志
                    } else if (ir < 0)
                        //读取完毕
                        break;
                } catch (InterruptedIOException e) {
                	
                    //捕获中断IO流异常
                }
                try {
                    if ((ir = is1.read(bytes)) > 0) {
                        //判断读取输入流的信息
                        os1.write(bytes, 0, ir);
                        //将读取的数据写入输出流对象中
                        if (logging)
                            writeLog(bytes, 0, ir, false);
                        //写入日志
                    } else if (ir < 0)
                        //读取完毕
                        break;

                } catch (InterruptedIOException e) {
                	
                    //捕获中断IO流异常
                }
            }
        } catch (Exception e0) {
        	
            //捕获异常
        }
    }


    /**
     * Handle the socket
     * @param port
     * @param clobj
     */
    static public void proxyStart(int port, Class<SingleHttpProxyServer> clobj) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);//根据端口创建服务器端Socket对象
            while (true) {
                Class[] objClass = new Class[1];
                Object[] obj = new Object[1];
                objClass[0] = Socket.class;
                try {
                    Constructor cons = clobj.getDeclaredConstructor(objClass);//建立代理服务器
                    obj[0] = serverSocket.accept();//等待连接
                    cons.newInstance(obj); // 创建HttpProxy或其派生类的实例,创建传入类
                } catch (Exception e) {
                    Socket socket = (Socket) obj[0];
                    try {
                        socket.close();
                    } catch (Exception ec) {
                    	
                    }
                }
            }
        } catch (IOException e) {
        	
        }
    }
    
    /**
     * 
     * @param args
     */
    static public void main(String args[]) {
        System.out.println("HTTP Proxy Server start!");
        SingleHttpProxyServer.log = System.out;//output the log on console
        SingleHttpProxyServer.logging = false;
        SingleHttpProxyServer.proxyStart(8000, SingleHttpProxyServer.class);
    }

}
