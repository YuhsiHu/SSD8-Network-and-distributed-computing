package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Hu Yuxi
 * @date 2018-12-18
 * @see java.rmi.Remote
 */
public interface MeetingInterface extends Remote {

	/**
	 * 注册用户
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否注册成功
	 * @throws RemoteException
	 */
	public boolean registerUser(String username, String password) throws RemoteException;

	/**
	 * 添加会议
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param otherusers
	 *            参与者用户名列表
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param title
	 *            会议标题
	 * @return 添加会议信息
	 * @throws RemoteException
	 */
	public String addMeeting(String username, String password, String[] otherUsers, String start, String end,
			String title) throws RemoteException;

	/**
	 * 查询会议
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return 会议信息
	 * @throws RemoteException
	 */
	public String queryMeeting(String username, String password, String start, String end) throws RemoteException;

	/**
	 * 删除会议
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param meetingID
	 *            会议ID
	 * @return 是否删除成功
	 * @throws RemoteException
	 */
	public boolean deleteMeeting(String username, String password, int meetingID) throws RemoteException;

	/**
	 * 清除会议
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否清除成功
	 * @throws RemoteException
	 */
	public boolean clearMeeting(String username, String password) throws RemoteException;
}
