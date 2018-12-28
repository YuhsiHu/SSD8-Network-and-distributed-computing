package iface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import bean.User;

/**
 * imply what functions does the system own
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public interface MyInterface extends Remote {
	// 登录
	public boolean login(User user) throws RemoteException;

	// 注册
	public boolean register(User user) throws RemoteException;

	// 显示所有注册的用户名字
	public String showUsers() throws RemoteException;

	// 查询留言
	public Vector checkMessages(User user) throws RemoteException;

	// 给他人留言
	public String leaveMessage(String username, String password, String receiver_name, String message_text)
			throws RemoteException;
}
