package RMI;

import RMI.MeetingInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * RMI 客户端
 *
 * @author Hu Yuxi
 * @date 2018-12-18
 *
 */
public class RMIClient {

    private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    
    private static final String WRONG_PARAMETER = "参数错误";
    private static final String SUCCESS = "操作成功";
    private static final String FAILURE = "操作失败";
    private static final String TIME_FORMAT = "时间格式应为：yyyy-MM-dd-HH:mm";

    private static String username = null;
    private static String password = null;

    static MeetingInterface rmi;

    /**
     * Initialize
     * @param args
     *           localhost 
     *           1099 
     *           register 
     *           用户名 
     *           密码
     */
    public static void main(String[] args) {
        /**
         * 创建远程对象
         */
        try {
            if (args.length < 3) {
                System.err.println(WRONG_PARAMETER);
                System.exit(0);
            }
            String host = args[0];
            String port = args[1];
            /*
            通过查找获得远程对象
             */
            rmi = (MeetingInterface) Naming.lookup("//" + host + ":" + port + "/Meeting");

            /**
             * 注册服务
             */
            if (args[2].equals("register")) {
                if (args.length != 5) {
                	//新用户注册
                    System.err.println(WRONG_PARAMETER);
                    System.exit(0);
                }
                boolean flag = rmi.registerUser(args[3], args[4]);
                if (!flag) {
                	//注册失败
                    System.err.println(FAILURE);
                    System.err.println("该用户名已经存在！");
                } else {
                    username = args[3];
                    password = args[4];
                    System.out.println(username + SUCCESS);
                }
            } else {//其他服务
                username = args[3];
                password = args[4];
                String[] cmds = Arrays.copyOfRange(args, 5, args.length);
                service(cmds);
            }
            /**
             * 显示帮助
             */
            helpMenu();

            /**
             * 其他服务
             */
            while (true) {
                System.out.println("Input an operation: ");
                String operation = bf.readLine();
                String[] cmds = operation.split(" ");
                service(cmds);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle the commands
     *
     * @param cmds commands
     */
    private static void service(String[] cmds) throws RemoteException {
        if (cmds[0].equals("add")) {
            doAdd(cmds);
        } else if (cmds[0].equals("delete")) {
            doDelete(cmds);
        } else if (cmds[0].equals("clear")) {
            doClear();
        } else if (cmds[0].equals("query")) {
            doQuery(cmds);
        } else if (cmds[0].equals("help")) {
            helpMenu();
        } else if (cmds[0].equals("quit")) {
            System.exit(0);
        }else System.err.println(WRONG_PARAMETER);
    }

    /**
     * show help menu
     */
    private static void helpMenu() {
        System.out.println(TIME_FORMAT);
        System.out.println("RMI Menu:");
        System.out.println("\t" + "1.add");
        System.out.println("\t\t" + "arguments:<username> <start> <end> <title>");
        System.out.println("\t" + "2.delete");
        System.out.println("\t\t" + "arguments:<meetingid>");
        System.out.println("\t" + "3.clear");
        System.out.println("\t\t" + "arguments:no args");
        System.out.println("\t" + "4.query");
        System.out.println("\t\t" + "arguments:<start> <end>");
        System.out.println("\t" + "5.help");
        System.out.println("\t\t" + "arguments:no args");
        System.out.println("\t" + "6.quit");
        System.out.println("\t\t" + "arguments:no args");
    }

    /**
     * add meeting
     *
     * @param cmds 添加命令
     * @throws RemoteException
     */
    private static void doAdd(String[] cmds) throws RemoteException {
        String info;
        if (cmds.length < 5) {
            info = WRONG_PARAMETER;
            System.err.println(info);
        } else {
            String[] otherUserNames = Arrays.copyOfRange(cmds, 1, cmds.length - 3);
            info = rmi.addMeeting(username, password, otherUserNames, cmds[cmds.length - 3], cmds[cmds.length - 2], cmds[cmds.length - 1]);
            System.out.println(info);
        }
    }

    /**
     * delete meeting
     *
     * @param cmds 删除命令
     * @throws RemoteException
     */
    private static void doDelete(String[] cmds) throws RemoteException {
        if (cmds.length != 2) {
            System.err.println(WRONG_PARAMETER);
        } else {
            boolean flag = rmi.deleteMeeting(username, password, Integer.parseInt(cmds[1]));
            if (flag){
                System.out.println(SUCCESS);
            }else System.err.println(FAILURE);
        }
    }

    /**
     * clear meeting
     *
     * @throws RemoteException
     */
    private static void doClear() throws RemoteException {
        boolean flag = rmi.clearMeeting(username, password);
        if (flag) {
            System.out.println(SUCCESS);
        }else System.err.println(FAILURE);
    }

    /**
     * search for meeting
     *
     * @param cmds 查询命令
     * @throws RemoteException
     */
    private static void doQuery(String[] cmds) throws RemoteException {
        if (cmds.length != 3) {
            System.err.println(WRONG_PARAMETER);
        } else {
            String info = rmi.queryMeeting(username, password, cmds[1],cmds[2]);
            System.out.println(info);
        }
    }
}
