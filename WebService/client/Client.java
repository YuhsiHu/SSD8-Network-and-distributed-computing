package client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.service.MyFirstService;
import com.service.User;
import com.service.WebServerService;

import com.service.TodoList;


/**
 * 客户端
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class Client {

	private static SimpleDateFormat format = new SimpleDateFormat("M-d-h:m");// 格式化日期
	private static User user = new User();// 创建的是com.service.User实例，其中没有有参构造函数
	private static WebServerService webService;// 实例化com.service.WebServerService
	private static MyFirstService service;// 实例化com.service.MyFirstService


	/**
	 * 处理命令
	 * 
	 * @param line
	 *            客户端输入的命令行
	 * @return 是否结束程序的标记（1为结束程序，0为继续获取输入）
	 */
	public static int handle(String line) {
		if (line.equals("quit")) {// 输入为quit时
			System.out.println("Bye");
			return 1;// 退出程序
		} else if (line.equals("help")) {// 输入为help时
			outputIntroduction();// 打印菜单
			return 0;
		} else if (line.startsWith("add")) {// 输入以add开头时
			addItem(line);// 处理add命令
			return 0;
		} else if (line.startsWith("query")) {// 输入以query开头时
			queryItem(line);// 处理query命令
			return 0;
		} else if (line.startsWith("delete")) {// 输入以delete开头时
			deleteItem(line);// 处理delete命令
			return 0;
		} else if (line.equals("clear")) {//// 输入为clear时
			service.clearItem(user.getUsername());// 调用service的clearitem方法
			System.out.println("清空完成");
			return 0;
		} else {// 输入的命令不在菜单中时
			System.err.println("非法命令");
			return 0;
		}
	}

	/**
	 * 处理add命令
	 * 
	 * @param line
	 *            用户输入的命令
	 */
	public static void addItem(String line) {
		String[] command = line.split(" ");
		if (command.length != 4) {// 参数个数不为4
			System.err.println("参数个数错误");
		} else if (!command[0].equals("add")) {// 第一个参数不是add
			System.err.println("非法命令");
		} else {
			try {
				Date start = format.parse(command[1]);// 提取日期
				Date end = format.parse(command[2]);// 提取日期

				if (start.before(end)) {// 检测时间顺序是否正确
					TodoList item = new TodoList();// 创建的是com.service.TodoList实例
					item.setCreater(user.getUsername());// 设置创建者
					item.setStartTime(transform(start));// 设置起始时间
					item.setEndTime(transform(end));// 设置结束时间
					item.setTitle(command[3]);// 设置标签
					// 在Date转换为XMLGregorianCalendar要把“年、月、日、时、分、秒”都设置			
					if (service.addItem(user.getUsername(), item)) {// 调用service的addItem方法
						System.out.println("添加成功");
					} else {
						System.err.println("添加失败，与您已有项目时间冲突");
					}
				} else {
					System.err.println("时间顺序错误");
				}
			} catch (ParseException e) {
				System.err.println("日期格式解析错误");
				// e.printStackTrace();
			}
		}
	}

	/**
	 * 处理query命令
	 * 
	 * @param line
	 *            用户输入的命令
	 */
	public static void queryItem(String line) {
		String[] command = line.split(" ");
		if (command.length != 3) {// 参数个数不为3
			System.err.println("参数个数错误");
			return;
		} else if (!command[0].equals("query")) {// 第一个参数不是query
			System.err.println("非法命令");
			return;
		} else {
			try {
				Date start = format.parse(command[1]);// 提取日期
				Date end = format.parse(command[2]);// 提取日期
				if (!start.before(end)) {// 检测时间顺序是否正确
					System.err.println("时间顺序错误");
					return;
				}
				List<TodoList> result = new ArrayList<TodoList>();
				result = service.queryItem(user.getUsername(), transform(start), transform(end));// 调用service的queryItem方法
				// 补全日期信息“年、月、日、时、分、秒”一个不少
				if (result.isEmpty()) {// 查询为空
					System.out.println("查询为空");
					return;
				} else {
					for (TodoList i : result) {
						/*
						 * 获取起始时间信息
						 */
						int start_month = i.getStartTime().getMonth();
						int start_day = i.getStartTime().getDay();
						int start_hour = i.getStartTime().getHour();
						int start_minute = i.getStartTime().getMinute();
						/*
						 * 获取结束时间信息
						 */
						int end_month = i.getEndTime().getMonth();
						int end_day = i.getEndTime().getDay();
						int end_hour = i.getEndTime().getHour();
						int end_minute = i.getEndTime().getMinute();
						/*
						 * 打印项目的信息
						 */
						String str = "item" + "[ ID=" + i.getTodoListID() + ", " + "start:" + start_month + "月"
								+ start_day + "日" + start_hour + "时" + start_minute + "分, " + "end:" + end_month + "月"
								+ end_day + "日" + end_hour + "时" + end_minute + "分, " + "label:" + i.getTitle() + ", "
								+ "creater:" + i.getCreater() + " ]";
						System.out.println(str);
					}
					return;
				}
			} catch (ParseException e) {
				System.err.println("日期格式解析错误");
				// e.printStackTrace();
			}

		}
	}

	/**
	 * 将Date形式的日期转化为XMLGregorianCalendar形式的日期
	 * 
	 * @param date
	 *            Date形式的日期
	 * @return XMLGregorianCalendar形式的日期
	 */
	public static XMLGregorianCalendar transform(Date date) {
		XMLGregorianCalendar result = null;
		Calendar calendar = Calendar.getInstance();// 构造函数Calendar()为protected，不可用
		// 使用默认时区和语言环境获得一个日历。
		calendar.setTime(date);
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();// 获取新的 DatatypeFactory 实例
			// 返回：新的 DatatypeFactory 实例
			result = factory.newXMLGregorianCalendar();// 创建新的 XMLGregorianCalendar 实例。
			// 返回：新的 XMLGregorianCalendar
			/*
			 * 设置“年、月、日、时、分、秒”信息
			 */
			result.setYear(calendar.get(Calendar.YEAR));
			result.setMonth(calendar.get(Calendar.MONTH) + 1);// Calendar.MONTH的取值范围为0-11
			result.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			result.setHour(calendar.get(Calendar.HOUR_OF_DAY));
			result.setMinute(calendar.get(Calendar.MINUTE));
			result.setSecond(calendar.get(Calendar.SECOND));
			// “年、月、日、时、分、秒”都要设置，否则会出错，空指针异常
			return result;
		} catch (DatatypeConfigurationException e) {
			// 如果该实现不可用，或者无法实例化。
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 处理delete命令
	 * 
	 * @param line
	 *            用户输入的命令
	 */
	public static void deleteItem(String line) {
		String[] command = line.split(" ");
		if (command.length != 2) {// 参数个数不为2
			System.err.println("参数个数错误");
		} else if (!command[0].equals("delete")) {// 第一个参数不是delete
			System.err.println("非法命令");
		} else {
			int id = Integer.parseInt(command[1]);// 获取项目ID
			if (service.deleteItem(user.getUsername(), id)) {// 调用service的deleteItem方法
				System.out.println("删除成功");
			} else {
				System.err.println("删除失败");
			}
		}
	}

	/**
	 * 打印菜单
	 */
	public static void outputIntroduction() {
		System.out.println("Web Service MENU:");
		System.out.println("1.add:");
		System.out.println("      <start-time> <end-time> <label> 日期格式:Month-Day-Hour:Minute");
		System.out.println("2.query:");
		System.out.println("      <start-time> <end-time>");
		System.out.println("3.delete:");
		System.out.println("      <item-id>");
		System.out.println("4.clear:");
		System.out.println("      no arguments");
		System.out.println("5.help:");
		System.out.println("      no arguments");
		System.out.println("6.quit:");
		System.out.println("      no arguments");
	}
	/**
	 * 主程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		webService = new WebServerService();// 获取Service
		service = webService.getServicePort();// 获取portName
		System.out.println("客户端获取WebService端口成功");
		/*
		 * 用户注册
		 */
		Scanner in = new Scanner(System.in);
		System.out.println("正在注册...");
		while (true) {
			System.out.println("请输入用户名");
			String username = in.nextLine();// 获取输入
			System.out.println("请输入密码");
			String password = in.nextLine();// 获取输入
			user.setUsername(username);// 设置用户名
			user.setPassword(password);// 设置密码
			if (service.userRegister(user)) {
				System.out.println("注册成功");
				break;
			} else {
				System.err.println("注册失败，该用户名已存在");
			}
		}
		/*
		 * 打印菜单
		 */
		outputIntroduction();
		/*
		 * 处理命令
		 */
		int tag = 0;
		while (true) {
			String command = in.nextLine();
			tag = handle(command);
			if (tag == 1) {
				break;// 输入为quit时，退出程序
			}
		}
		in.close();
	}
}
