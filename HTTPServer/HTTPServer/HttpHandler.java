package HTTPServer;
import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 * HttpHandler
 * @author Hu Yuxi
 * @date 2018-12-07
 * 
 */
public class HttpHandler implements Runnable{
    File file;
    private Socket socket;
    private static final int TCP = 2021;
    private static final String HOST = "127.0.0.1";
    private String rootpath;
    private String filepath;
    private String savepath;
    private static final int buffer_size = 8192;
    private static String CRLF = "\r\n";
    StringBuilder request = null;
    StringBuilder response = null;
    StringBuilder html = null;
    BufferedReader bufferedReader = null;
    BufferedInputStream inputStream = null;
    BufferedOutputStream outputStream = null;
    private byte[] buffer;
    private String line;

    /**
     * 构造函数
     * @param socket socket
     * @param rootpath root path
     */
    public HttpHandler(Socket socket,String rootpath){
        this.socket = socket;
        this.rootpath = rootpath;
        System.out.println("**********Root path: "+rootpath+"**********");
    }

    /**
     *Initialize
     * @throws IOException
     */
    public void initStream() throws IOException{
        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());
        request = new StringBuilder();
        response = new StringBuilder();
        html = new StringBuilder();
        line = "";
        filepath = rootpath;
        savepath = rootpath+"\\saving";
    }

    @Override
    /**
     * Implement run thread
     */
    public void run(){
        try{
            initStream();//Initialize
            processRequest();//调用该函数初始化request
            //判断请求,按照要求只对GET和PUT做响应
            if(request.indexOf("GET")!=-1){
                System.out.println("line: "+line);
                doGetResponse();
            }else if(request.indexOf("PUT")!=-1){
            	System.out.println("line: "+line);
                doPutResponse();
            }else{
            	System.out.println("line: "+line);
                System.out.println("未知错误");
                doBadRequest();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * processRequest 该函数负责读取客户端发来的Http请求
     * @throws IOException
     */
    private void processRequest() throws IOException{
        int last=0,c=0;
        boolean inHeader = true;
        boolean flag = false;//用flag变量来判断第一行是否读完，利用第一行来处理http报文
        String mark = "";
        while(inHeader && (c=inputStream.read())!=-1){
            switch (c){
                case '\r':
                    break;
                case  '\n':
                    if(c==last){
                        inHeader = false;
                        break;
                    }
                    last = c;
                    request.append(mark+"\n");
                    if(!flag){//如果第一行读完了，把第一行赋值给line，line负责后续处理
                        line = mark;
                    }
                    mark = "";
                    flag = true;
                    break;
                default:
                    last = c;
                    mark += (char) c;
            }
        }
        System.out.println("**********processRequest is:**********"+request+"**********End of request**********");
    }

    /**
     * doGetResponse 该函数负责处理客户端发来的GET请求
     */
    private void doGetResponse(){
    	//把命令行line分为三个部分
        String[] part = line.split(" ");
        
        System.out.println("**********doGetResponse line**********"+line+"**********End of line**********");
        if(part.length !=3){
        	//判断line的分割是否为: GET+URL+HTTP
            System.out.println("命令行组成不正确");
            doBadRequest();
        } else if(part[2].equals("HTTP/1.0") || part[2].equals("HTTP/1.1")) {
        	//判断HTTP协议是否规范
            if(part[1].equals("/") || part[1].equals("/index.html")) {
            	//符合此条件的返回index.html
                file = new File(rootpath + "\\index.html");
                System.out.println("***********doGetResponse root path**********"+rootpath+"**********End of root path**********");
                doHtmlResponse(file);
                System.out.println("***********doGetResponse response**********"+response+"**********End of response**********");
            }else if(part[1].endsWith(".html") || part[1].endsWith(".htm")){
            	 //将请求中的/全部替换成\形成路径
                filepath = rootpath+part[1].replaceAll("/","\\\\");
                file = new File(filepath);
                System.out.println("***********doGetResponse file path**********"+filepath+"**********End of file path**********");
                if(file.exists()){
                	System.out.println("**********File exists**********");
                    doHtmlResponse(file);
                }else{
                	//如果请求的文件不存在，返回404
                	System.out.println("**********File does not exist**********");
                    doNotFoundRequest();
                }
            }else if(part[1].endsWith(".jpg") || part[1].endsWith("jpeg")){
            	//将请求中的/全部替换成\形成路径
                filepath = rootpath+part[1].replaceAll("/","\\\\");
                file = new File(filepath);
                System.out.println("***********doGetResponse jpg path**********"+filepath+"**********End of jpg path**********");
                if(file.exists()){
                	System.out.println("**********JPG exists**********");
                    doJpegResponse(file);
                }else{
                	//如果请求的文件不存在，返回404
                	System.out.println("**********JPG does not exist**********");
                    doNotFoundRequest();
                }
            }else {
            	//不是HTML或JPEG的请求都不接受
            	System.out.println("**********Not html or jpg request**********");
                doBadRequest();
            }
        }else{
            System.out.println("其他错误原因");
            doBadRequest();
        }
    }

    /**
     * do PutResponse 处理客户端发来的PUT请求
     */
    private void doPutResponse(){
        try {	
            String responseHeader =new String();
    		responseHeader = "HTTP/1.1 200 OK" + CRLF;
    		responseHeader += "Server:MyHttpServer/1.1 " + CRLF;
    	    //responseHeader += "Content-length: " + file.length() + CRLF;
    		responseHeader += "Content-type: " + "text/html;charset=ISO-8859-1" + CRLF + CRLF;
            //往客户端发送
            String message1 = responseHeader;
            System.out.println("**********doPutResponse message**********"+message1+"**********");
            buffer = message1.getBytes();
            outputStream.write(buffer, 0, message1.length());
            outputStream.flush();
        	
        	
            String[] part = line.split(" ");
            savepath = savepath+part[1].replaceAll("/","\\\\");
            buffer = new byte[buffer_size];
//            while (inputStream.read(buffer) != -1) {
//                response.append(new String(buffer,"ISO-8859-1"));
//                System.out.println("**********Response toString:"+response.toString()+"**********");
//            }
            
            int c=0;
            File file = new File(savepath);
            System.out.println(savepath);
            FileOutputStream fos = new FileOutputStream(file);
            //bufferedinputstream只有关闭socket才会不阻塞，所以需要用available函数查看是否读完
            while ((inputStream.available()>0)&&((c = inputStream.read())!= -1)) {
            	System.out.println(c);
    			fos.write(c);
    			fos.flush();
    		}   
            System.out.println("final c:"+c);
            fos.flush();       
            fos.close();   
            System.out.println("***********While finished***********");
//            String message = response.toString();
//            System.out.println("**********Message Response toString:"+message+"**********");
//            File file = new File(savepath);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(message.getBytes("ISO-8859-1"));
//            fos.flush();       
//            fos.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * doBadRequest 处理错误的客户端请求
     */
    private void doBadRequest(){
        try {
            file = new File(rootpath + "\\response\\400.html");
            bufferedReader = new BufferedReader(new FileReader(file.getPath()));
            String str = bufferedReader.readLine();
            while(str!=null){
                html.append(str+"\n");
                str = bufferedReader.readLine();
            }
            //包装response
            response.append("HTTP/1.1 400 Bad Request"+CRLF);
            response.append("Date: "+new Date().toString()+CRLF);
            response.append("Content-Type: text/html;charset=ISO-8859-1"+CRLF);
            response.append("Content-Length: "+file.length()+CRLF);
            response.append(CRLF);
            response.append(html);
            //往客户端发送
            String message = response.toString();
            buffer = message.getBytes();
            outputStream.write(buffer,0,message.length());
            outputStream.flush();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(file.getName()+": "+file.getPath());
        System.out.println(response);
    }

    /**
     * doNotFoundRequest 处理404
     */
    private void doNotFoundRequest(){
        try {
            file = new File(rootpath + "\\response\\404.html");
            bufferedReader = new BufferedReader(new FileReader(file.getPath()));
            String str = bufferedReader.readLine();
            while(str!=null){
                html.append(str+"\n");
                str = bufferedReader.readLine();
            }
            //包装response
            response.append("HTTP/1.1 404 Not Found"+CRLF);
            response.append("Date: "+new Date().toString()+CRLF);
            response.append("Content-Type: text/html;charset=ISO-8859-1"+CRLF);
            response.append("Content-Length: "+file.length()+CRLF);
            response.append(CRLF);
            response.append(html);
            //往客户端发送
            String message = response.toString();
            buffer = message.getBytes();
            outputStream.write(buffer,0,message.length());
            outputStream.flush();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(file.getName()+": "+file.getPath());
        System.out.println(response);
    }

    /**
     * doHtmlResponse 客户端请求html文件
     * @param file html文件的路径
     */
    private void doHtmlResponse(File file){
        try{
            //将文件读取至内存中
            bufferedReader = new BufferedReader(new FileReader(file.getPath()));
            System.out.println("**********doHtmlResponse file path:**********"+file.getPath()+"**********");
            String str = bufferedReader.readLine();
            while (str != null) {
                html.append(str + "\n");
                str = bufferedReader.readLine();
            }
            //包装response
            response.append("HTTP/1.1 200 OK" + CRLF);
            response.append("Date: " + new Date().toString() + CRLF);
            response.append("Content-Type: text/html;charset=ISO-8859-1" + CRLF);
            response.append("Content-Length: " + file.length() + CRLF);
            response.append(CRLF);
            response.append(html);
            //往客户端发送
            String message = response.toString();
            System.out.println("**********doHtmlResponse message**********"+message+"**********");
            buffer = message.getBytes();
            outputStream.write(buffer, 0, message.length());
            outputStream.flush();
            outputStream.close();
            socket.close(); 
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * doJpegResponse 该函数负责处理客户端请求jpeg文件
     * @param file
     */
    private void doJpegResponse(File file){
        try{
            //包装response
            response.append("HTTP/1.1 200 OK" + CRLF);
            response.append("Date: " + new Date().toString() + CRLF);
            response.append("Content-Type: image/jpeg;charset=ISO-8859-1" + CRLF);
            response.append("Content-Length: " + file.length() + CRLF);
            response.append(CRLF);
            //往客户端发送
            String message = response.toString();
            buffer = message.getBytes("ISO-8859-1");
            outputStream.write(buffer, 0, message.length());
            //将文件读取至内存中并发送
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int length = 0;
            byte[] sendInfo = new byte[buffer_size];
            while((length = bis.read(sendInfo))!=-1){
            	System.out.println(length);
                outputStream.write(sendInfo,0,length);
                outputStream.flush();
            }
            
            
//            int num=0;
//            while ((num<=file.length())&&((length = bis.read())!= -1)) {         	
//            	outputStream.write(length);
//            	outputStream.flush();
//            	num++;
//            	System.out.println(num);
//    		}   
//            outputStream.flush();
            
            outputStream.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
