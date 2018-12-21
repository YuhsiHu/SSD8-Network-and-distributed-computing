package RMI;

import RMI.Meeting;
import RMI.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Hu Yuxi
 * @date 2018-12-18
 * @see java.rmi.server.UnicastRemoteObject
 *
 */
public class MeetingInterfaceImpl extends UnicastRemoteObject implements MeetingInterface {

	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Meeting> meetings = new ArrayList<>();

	private static int meetingID = 0;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");

	private static final String REGISTER_USER_SUCCESS = "注册成功";
	private static final String REGISTER_USER_FAILURE = "注册失败";

	private static final String INVALID_TIME_FORMAT = "无效时间格式,应为yyyy-MM-dd-HH:mm";
	private static final String INVALID_TIME = "无效时间";
	private static final String INVALID_USER = "无效用户";
	private static final String ADD_MEETING_SUCCESS = "添加会议成功!";

	/**
	 * 构造函数
	 * 
	 * @throws RemoteException
	 */
	public MeetingInterfaceImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean registerUser(String username, String password) throws RemoteException {
		for (User user : users) {
			if (isUserExist(username)) {
				//已存在的用户，不可以注册
				System.out.println(REGISTER_USER_FAILURE);
				return false;
			}
		}
		//新用户，可以注册
		User user = new User(username, password);
		//添加到用户列表
		users.add(user);
		System.out.println(user.toString() + REGISTER_USER_SUCCESS);
		return true;
	}

	@Override
	public String addMeeting(String username, String password, String[] otherUserNames, String start, String end,
			String title) throws RemoteException {

		String info = null;

		ArrayList<User> otherUsers = new ArrayList<>();
		User launchUser = new User(username, password);

		Date startTime = null;
		Date endTime = null;

		/*
		 * 判断时间格式
		 */
		try {
			startTime = dateFormat.parse(start);
			endTime = dateFormat.parse(end);
		} catch (ParseException e) {
			info = INVALID_TIME_FORMAT;
			e.printStackTrace();
			return info;
		}

		/*
		 * 判断时间重叠与倒置
		 */
		if (isOverlap(startTime, endTime) || isReversed(startTime, endTime)) {
			info = INVALID_TIME;
			return info;
		}

		/*
		 * 判断添加者是否存在
		 */
		if (!isUserExist(launchUser)) {
			info = INVALID_USER;
			return info;
		}

		/*
		 * 判断参与者是否合理
		 */
		if (otherUserNames.length < 1 || !isUsersExist(otherUserNames)) {
			info = INVALID_USER;
			return info;
		} else {
			for (String temp : otherUserNames) {
				for (User user : users) {
					if (user.getName().equals(temp)) {
						otherUsers.add(user);
					}
				}
			}
		}

		/*
		 * 添加会议
		 */
		Meeting meeting = new Meeting(meetingID++, title, startTime, endTime, launchUser, otherUsers);
		meetings.add(meeting);
		info = ADD_MEETING_SUCCESS;

		return info;
	}

	@Override
	public String queryMeeting(String username, String password, String start, String end) throws RemoteException {
		String info = "";

		User user = new User(username, password);

		Date startTime = null;
		Date endTime = null;

		/*
		 * 判断时间格式
		 */
		try {
			startTime = dateFormat.parse(start);
			endTime = dateFormat.parse(end);
		} catch (ParseException e) {
			info = INVALID_TIME_FORMAT;
			e.printStackTrace();
			return info;
		}

		/*
		 * 判断添加者是否存在
		 */
		if (!isUserExist(user)) {
			info = INVALID_USER;
			return info;
		}

		for (Meeting meeting : meetings) {
			if (isBetween(meeting.getStartTime(), startTime, endTime)
					&& isBetween(meeting.getEndTime(), startTime, endTime)) {
				info += meeting.toString();
				info += "\n";
			}
		}

		return info;
	}

	@Override
	public boolean deleteMeeting(String username, String password, int meetingID) throws RemoteException {

		User user = new User(username, password);

		/*
		 * 判断添加者是否存在
		 */
		if (!isUserExist(user)) {
			System.out.println(INVALID_USER);
			return false;
		}

		/*
		 * 删除会议
		 */
		for (Meeting meeting : meetings) {
			if (meeting.getMeetingID() == meetingID && meeting.getLaunchUser().equals(user)) {
				meetings.remove(meeting);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean clearMeeting(String username, String password) throws RemoteException {

		User user = new User(username, password);

		/*
		 * 判断添加者是否存在
		 */
		if (!isUserExist(user)) {
			System.out.println(INVALID_USER);
			return false;
		}

		/*
		 * 清除会议
		 */
		Iterator<Meeting> meetingIterator = meetings.iterator();
		while (meetingIterator.hasNext()) {
			if (meetingIterator.next().getLaunchUser().equals(user)) {
				meetingIterator.remove();
			}
		}
		return true;
	}

	/**
	 * 判断一组用户是否都存在
	 *
	 * @param usernames
	 *            用户名列表
	 * @return
	 */
	private boolean isUsersExist(String[] usernames) {
		boolean isAllExist = true;
		for (String username : usernames) {
			if (!isUserExist(username)) {
				isAllExist = false;
				break;
			}
		}
		return isAllExist;
	}

	/**
	 * 判断用户是否存在
	 *
	 * @param username
	 *            用户名
	 * @return 是否存在
	 */
	private boolean isUserExist(String username) {
		boolean isExist = false;
		for (User user : users) {
			if (user.getName().equals(username)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	/**
	 * 判断用户是否存在
	 *
	 * @param user
	 *            用户
	 * @return 是否存在
	 */
	private boolean isUserExist(User user) {
		boolean isExist = false;
		for (User temp : users) {
			if (temp.equals(user)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}
	/**
	 * 判断新会议是否与原会议时间重叠
	 *
	 * @param startTime
	 *            新会议开始时间
	 * @param endTime
	 *            新会议结束时间
	 * @return 是否重叠
	 */
	private boolean isOverlap(Date startTime, Date endTime) {
		boolean isOverlap = false;
		for (Meeting meeting : meetings) {
			if (isOverlap(meeting.getStartTime(), meeting.getEndTime(), startTime, endTime)) {
				isOverlap = true;
				return isOverlap;
			}
		}
		return isOverlap;
	}

	/**
	 * 判断时间是否颠倒
	 *
	 * @param former
	 *            开始时间
	 * @param latter
	 *            结束时间
	 * @return 是否颠倒
	 */
	private boolean isReversed(Date former, Date latter) {
		return former.after(latter);
	}

	/**
	 * 判断[st1 et1]与[st2 et2]时间是否重叠
	 *
	 * @param st1
	 *            起始时间1
	 * @param et1
	 *            结束时间1
	 * @param st2
	 *            起始时间2
	 * @param et2
	 *            结束时间2
	 * @return 是否重叠
	 */
	private boolean isOverlap(Date st1, Date et1, Date st2, Date et2) {
		boolean isOverlap = isBetween(st2, st1, et1) || isBetween(et2, st1, et1) || isBetween(st1, st2, et2)
				|| st1.equals(st2) || et1.equals(et2);// 两时间是否相等
		return isOverlap;
	}

	/**
	 * 判断date是否在时间段[former latter]内
	 *
	 * @param date
	 *            时间
	 * @param former
	 *            起始时间
	 * @param latter
	 *            结束时间
	 * @return 是否在时间段中
	 */
	private boolean isBetween(Date date, Date former, Date latter) {
		boolean isBetween = (date.after(former) && date.before(latter)) || date.equals(former) && date.equals(latter);
		return isBetween;
	}

}
