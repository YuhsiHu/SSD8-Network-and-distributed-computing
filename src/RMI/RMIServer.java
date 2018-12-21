package RMI;

import RMI.MeetingInterface;
import RMI.MeetingInterfaceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RMI 服务器
 *
 * @author Hu Yuxi 
 * @date 2018-12-18
 *
 */
public class RMIServer {
    /**
     * 启动 RMI 注册服务并进行对象注册
     */
    public static void main(String[] args) {
        try {
            // 启动RMI注册服务，指定端口为1099　（1099为默认端口）
            // 也可以通过命令 ＄java_home/bin/rmiregistry 1099启动
            // 这里用这种方式避免了再打开一个DOS窗口
            // 而且用命令rmi registry启动注册服务还必须事先用RMIC生成一个stub类为它所用
            LocateRegistry.createRegistry(1099);

            // 创建远程对象的一个或多个实例，下面是MeetingInterfaceImpl对象
            // 可以用不同名字注册不同的实例
            MeetingInterface meetingInterface = new MeetingInterfaceImpl();

            // 把hello注册到RMI注册服务器上，命名为Hello
            Naming.rebind("Meeting", meetingInterface);

            // 如果要把hello实例注册到另一台启动了RMI注册服务的机器上
            // Naming.rebind("//192.168.1.105:1099/Hello",hello);
            System.out.println("RMI Meeting Server is ready.");
        } catch (Exception e) {
            System.out.println("RMI Meeting Server failed: " + e);
        }
    }
}
