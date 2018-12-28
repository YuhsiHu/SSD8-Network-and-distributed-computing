package client;

import iface.MyInterface;

import java.rmi.Naming;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import bean.MessageContent;
import bean.User;

/**
 * build a client
 * 
 * @author Hu Yuxi
 * @date 2018-12-18
 *
 */
public class MyClient {
	private static String[] cmd;
	private static String opr;
	private static Scanner bf = new Scanner(System.in);
	private static String host = "127.0.0.1";
	private static String port = "8421";
	private static MyInterface messageInterface = null;

	/**
	 * 
	 * @param args nothing 
	 * @throws Exception RMIException
	 */
	public static void main(String args[]) throws Exception {
		messageInterface = (MyInterface) Naming.lookup("rmi://" + host + ":" + port + "/RMIMessage");
		handleCommand();
		while (!opr.equals("quit")) {
			switch (opr) {
			case "register":
				register();
				break;
			case "show":
				showUsers();
				break;
			case "check":
				checkMessages();
				break;
			case "leave":
				leaveMessage();
				break;
			default:
				System.out.println("invalidation input");
			}
		}
	}

	/**
	 * register
	 */
	public static void register() throws Exception {
		String username = cmd[1];
		String password = cmd[2];
		// 初始化一个用户
		User user = new User(username, password);
		if (messageInterface.register(user)) {
			System.out.println("successful register!!");
		} else {
			System.out.println("Failed to register,user already exists!!");
		}
		handleCommand();
	}

	/**
	 * Show users
	 */
	public static void showUsers() throws Exception {
		String usernames = messageInterface.showUsers();
		System.out.println(usernames);
		handleCommand();
	}

	/**
	 * Leave message
	 */
	public static void leaveMessage() throws Exception {
		String username = cmd[1];
		String password = cmd[2];
		String receiver_name = cmd[3];
		String message_text = cmd[4];
		String information = messageInterface.leaveMessage(username, password, receiver_name, message_text);
		System.out.println(information);
		handleCommand();
	}

	/**
	 * Check messages
	 */
	public static void checkMessages() throws Exception {
		String username = cmd[1];
		String password = cmd[2];
		User user = new User(username, password);
		Vector<MessageContent> information = messageInterface.checkMessages(user);
		Iterator<MessageContent> it = information.iterator();// 为留言创建迭代器
		int count = 1;// 统计留言数目
		int flag = 0;// 标志有无留言
		while (it.hasNext()) {
			flag = 1;
			MessageContent mc = it.next();
			if (mc.getSender().equals("ERROR")) {
				// 错误提示
				System.out.println(mc.getText());
			} else {
				// 有留言
				System.out.println("No." + count + " " + mc.toString() + "\n");
				count++;
			}
		}
		// 无留言提示
		if (flag == 0) {
			System.out.println("No message now!");
		}
		handleCommand();
	}

	/**
	 * Show the command menu
	 */
	public static void displayMenu() {
		System.out.println("RMI menu:\n" + "\t1.register user\n\t\tregister <username> <password>\n"
				+ "\t2.show users\n\t\tshow\n" + "\t3.check messages\n\t\tcheck <username> <password>\n"
				+ "\t4.leave other a message\n\t\tleave <username> <password> <aim_name> <text>\n" + "\t5.quit\n");
	}

	/**
	 * Get user's command
	 */
	public static void handleCommand() {
		displayMenu();
		String cmds = bf.nextLine();
		cmd = cmds.split(" ");
		opr = cmd[0];
	}
}
