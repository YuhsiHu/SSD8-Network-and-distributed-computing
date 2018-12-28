package server;

import iface.MyInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import bean.MessageContent;
import bean.User;

/**
 * implement the planning function
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class MyImpl extends UnicastRemoteObject implements MyInterface {
	private Hashtable users;// 用户账户名和密码用键值对存储
	private Vector<MessageContent> content;// 存储消息内容

	/**
	 * Initialize
	 * 
	 * @throws RemoteException
	 */
	public MyImpl() throws RemoteException {
		users = new Hashtable<String, String>();
		content = new Vector<MessageContent>();
	}

	/**
	 * handle log in
	 */
	public boolean login(User user) throws RemoteException {

		String username = user.getUsername();
		String password = user.getPassword();

		if (users.containsKey(username) && users.get(username).equals(password)) {
			// 存在该用户名且密码正确，登录成功
			return true;
		}
		return false;
	}

	/**
	 * handle register
	 */
	public boolean register(User user) throws RemoteException {

		String username = user.getUsername();
		String password = user.getPassword();
		if (users.containsKey(username))
			// 用户名已经存在，注册失败
			return false;

		// 否则注册成功
		users.put(username, password);
		return true;
	}

	/**
	 * handle show users
	 */
	public String showUsers() throws RemoteException {
		StringBuilder sb = new StringBuilder();
		Set<String> usernames = users.keySet();

		if (usernames.size() == 0) {
			// 用户列表为空
			sb.append("no such user");
		} else {
			// 只返回用户姓名，将所有用户的账户密码都返回给客户端不合适
			for (String username : usernames) {
				sb.append(username + "\n");
			}
		}
		return sb.toString();
	}

	/**
	 * handle check messages
	 */
	public Vector checkMessages(User user) throws RemoteException {
		StringBuilder sb = new StringBuilder();
		// 获得用户名和密码
		String username = user.getUsername();
		String password = user.getPassword();
		// 将用于返回的向量
		Vector<MessageContent> information = new Vector<MessageContent>();

		if (!users.containsKey(username)) {
			// 账户不存在
			MessageContent e = new MessageContent();
			e.setSender("ERROR");
			e.setText("No such user!");
			information.add(e);
			return information;
		} else if (!this.login(user)) {
			// 登录失败不可查看
			MessageContent e = new MessageContent();
			e.setSender("ERROR");
			e.setText("Wrong user name or password!");
			information.add(e);
			return information;
		} else {
			if (content.isEmpty()) {
				// 无留言，提示
				MessageContent e = new MessageContent();
				e.setSender("ERROR");
				e.setText("No message now!");
				information.add(e);
				return information;
			} else {
				// 有留言
				Iterator<MessageContent> it = content.iterator();// 为留言创建迭代器
				while (it.hasNext()) {
					// 逐条判断每条留言是不是给自己的
					MessageContent mc = it.next();
					if (mc.getReceiver().equals(username)) {
						// 是给这个用户的留言,添加
						information.add(mc);
					} else {
						// do nothing
					}
				}
			}
		}
		return information;
	}

	/**
	 * handle leave messages
	 */
	public String leaveMessage(String username, String password, String receiver_name, String message_text)
			throws RemoteException {
		StringBuilder sb = new StringBuilder();
		User user = new User(username, password);
		if (username.equals(receiver_name)) {
			// 不可以留言给自己
			sb.append("Cannot leave yourself messages");
		} else if (!users.containsKey(username)) {
			// 发送者不存在
			sb.append("No such sender");
		} else if (!users.containsKey(receiver_name)) {
			// 接收者不存在
			sb.append("No such receiver");
		} else if (!login(user)) {
			// 登录失败
			sb.append("Wrong username or password!");
		} else {
			// 创建留言
			Date time = new Date();
			MessageContent mc = new MessageContent();
			mc.setSender(username);
			mc.setReceiver(receiver_name);
			mc.setSendTime(time);
			mc.setText(message_text);
			content.add(mc);
			sb.append("Successful!");
		}
		return sb.toString();
	}
}
