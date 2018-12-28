package bean;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 待办事项类
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class TodoList {

	private int todoListID;// 项目ID
	private String creater;// 项目的创建者用户名
	private Date startTime;// 起始时间
	private Date endTime;// 结束时间
	private String title;// 标题

	/**
	 * 无参构造函数，必须要有，否则绑定WebService后运行会出错
	 */
	public TodoList() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param todoListID
	 *            事项ID
	 * @param creater
	 *            创建者用户名
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @param title
	 *            标题
	 */
	public TodoList(int todoListID, String creater, Date startTime, Date endTime, String title) {
		super();
		this.todoListID = todoListID;
		this.creater = creater;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
	}

	/**
	 * 获取创建者的用户名
	 * 
	 * @return 创建者的用户名
	 */
	public String getCreater() {
		return creater;
	}

	/**
	 * 设置创建者的用户名
	 * 
	 * @param creater
	 */
	public void setCreater(String creater) {
		this.creater = creater;
	}

	/**
	 * 获取事项ID
	 * 
	 * @return 项目ID
	 */
	public int getTodoListID() {
		return todoListID;
	}

	/**
	 * 设置事项ID
	 * 
	 * @param todoListID
	 */
	public void setTodoListID(int todoListID) {
		this.todoListID = todoListID;
	}

	/**
	 * 获取起始时间
	 * 
	 * @return 起始时间
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 设置起始时间
	 * 
	 * @param xmlGregorianCalendar
	 *            起始时间
	 */
	public void setStartTime(Date date) {
		this.startTime = date;
	}

	/**
	 * 获取结束时间
	 * 
	 * @return 结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 设置结束时间
	 * 
	 * @param xmlGregorianCalendar
	 *            结束时间
	 */
	public void setEndTime(Date date) {
		this.endTime = date;
	}

	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "待办事项 [todoListID=" + todoListID + ", startTime=" + startTime + ", endTime=" + endTime + ", title="
				+ title + "]";
	}

}
