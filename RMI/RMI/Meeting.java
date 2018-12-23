package RMI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Hu Yuxi
 * @date 2018-12-18
 * @see java.io.Serializable
 */
public class Meeting implements Serializable {

	/**
	 * 会议ID primary key
	 */
	private int meetingID;

	/**
	 * 会议title
	 */
	private String title;

	/**
	 * 会议开始时间与结束时间
	 */
	private Date startTime;
	private Date endTime;

	/**
	 * 会议添加者与参与者
	 */
	private User launchUser;
	private ArrayList<User> otherUsers;

	/**
	 * 构造函数
	 *
	 * @param meetingID
	 *            会议ID
	 * @param title
	 *            会议标题
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param launchUser
	 *            会议添加者
	 * @param otherUsers
	 *            会议参与者
	 */
	public Meeting(int meetingID, String title, Date startTime, Date endTime, User launchUser,
			ArrayList<User> otherUsers) {
		this.meetingID = meetingID;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.launchUser = launchUser;
		this.otherUsers = otherUsers;
	}

	/**
	 * 
	 * @return meetingID
	 */
	public int getMeetingID() {
		return meetingID;
	}

	/**
	 * 
	 * @param meetingID
	 */
	public void setMeetingID(int meetingID) {
		this.meetingID = meetingID;
	}

	/**
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 
	 * @return end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 
	 * @return user
	 */
	public User getLaunchUser() {
		return launchUser;
	}

	/**
	 * 
	 * @param launchUser
	 */
	public void setLaunchUser(User launchUser) {
		this.launchUser = launchUser;
	}

	/**
	 * 
	 * @return other users
	 */
	public ArrayList<User> getOtherUsers() {
		return otherUsers;
	}

	/**
	 * 
	 * @param otherUsers
	 */
	public void setOtherUsers(ArrayList<User> otherUsers) {
		this.otherUsers = otherUsers;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Meeting))
			return false;

		Meeting meeting = (Meeting) obj;

		if (getMeetingID() != meeting.getMeetingID())
			return false;
		if (!getTitle().equals(meeting.getTitle()))
			return false;
		if (getStartTime() != null ? !getStartTime().equals(meeting.getStartTime()) : meeting.getStartTime() != null)
			return false;
		if (getEndTime() != null ? !getEndTime().equals(meeting.getEndTime()) : meeting.getEndTime() != null)
			return false;
		if (!getLaunchUser().equals(meeting.getLaunchUser()))
			return false;
		return getOtherUsers() != null ? getOtherUsers().equals(meeting.getOtherUsers())
				: meeting.getOtherUsers() == null;
	}

	@Override
	public String toString() {
		return "Meeting{" + "meetingID=" + meetingID + ", title='" + title + '\'' + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", launchUser=" + launchUser.toString() + ", " + "\n" + "otherUsers="
				+ otherUsers.toString() + '}';
	}
}
