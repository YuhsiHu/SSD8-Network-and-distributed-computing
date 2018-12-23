package FileSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * FileSystem
 * 
 * @author Huyuxi
 *
 */
public class FileClient {
	private static final String HOST = "127.0.0.1";// 服务器IP
	private static final int UDP_PORT = 2020;// UDP port
	private static final int TCP_PORT = 2021;// TCP port
	private static final int SENDSIZE = 1024;
	DatagramSocket dgsocket;
	Socket socket = new Socket();

	public FileClient() throws IOException {
		socket = new Socket(HOST,TCP_PORT);	
	}

	public static void main(String[] args) throws SocketException, IOException {
		new FileClient().send();
	}

	public void send() {
        try {
        	//input stream
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            //output stream
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            
            PrintWriter pw = new PrintWriter(bw, true);

            System.out.println(br.readLine());

            Scanner in = new Scanner(System.in);
            String cmd = null;
            
            //input on console
            while ((cmd = in.next()) != null) {
                pw.println(cmd);
                System.out.println("You input:"+cmd);
                if (cmd.equals("cd") || cmd.equals("get")) {
                    String dir = in.next();
                    pw.println(dir);
                    if (cmd.equals("get")) {
                        long fileLength = Long.parseLong(br.readLine());
                        System.out.println("" + fileLength);
                        //if file exist
                        if (fileLength != -1) {
                            System.out.println("Begin to download file:" + dir);
                            System.out.println("Download successfully!");
                            getFile(dir, fileLength);
                        } else {
                            System.out.println("Unknown file");
                        }
                    }
                }
                String msg = null;
                while (null != (msg = br.readLine())) {
                    if (msg.equals("CmdEnd")) {
                        break;
                    }
                    System.out.println(msg);
                }

                if (cmd.equals("bye")) {
                    break;
                }
            }
            in.close();
            br.close();
            bw.close();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	/**
	 * 接收文件
	 * 
	 * @param fileName
	 * @param fileLength
	 * @throws IOException
	 */
	private void getFile(String fileName, long fileLength) throws IOException {
		DatagramPacket dp = new DatagramPacket(new byte[SENDSIZE], SENDSIZE);
		dgsocket = new DatagramSocket(UDP_PORT);
		byte[] recInfo = new byte[SENDSIZE];
		FileOutputStream fos = new FileOutputStream(new File(("D:\\") + fileName));

		int count = (int) (fileLength / SENDSIZE) + ((fileLength % SENDSIZE) == 0 ? 0 : 1);

		while ((count--) > 0) {
			dgsocket.receive(dp);
			recInfo = dp.getData();
			fos.write(recInfo, 0, dp.getLength());
			fos.flush();
		}
		dgsocket.close();
		fos.close();
	}
}
