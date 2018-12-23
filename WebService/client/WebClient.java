package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * Web Client
 * 
 * @author Hu Yuxi
 * @date 2018-12-21
 *
 */
public class WebClient {
	public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * List all of options
	 */
	public static void showMenu() {
		System.out.println("");
		System.out.println("功能目录");
		System.out.println("[1] 注册用户");
		System.out.println("[2] 添加事项");
		System.out.println("[3] 显示事项列表");
		System.out.println("[4] 删除事项");
		System.out.println("[5] 清空事项");
		System.out.println("[6] 退出");
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getChoose() throws Exception {
		int choose = -1;
		System.out.print("Choose an option:");
		while (choose <= 0 || choose > 6) {
			String strChoose = in.readLine();
			choose = Integer.parseInt(strChoose);
			if (choose <= 0 || choose > 6)
				System.err.println("Invalid option! Please choose again!");
		}
		return choose;
	}

	public static void main(String args[]) throws Exception {
		ListImp webClient = new ListImpService().getClientListPort();

		boolean isEnd = false;
		while (!isEnd) {
			showMenu();
			int choose = getChoose();

			switch (choose) {
			case 1: {
				String[] arg = new String[5];
				System.out.println("用户名:");
				arg[3] = in.readLine();
				System.out.println("密码:");
				arg[4] = in.readLine();
				System.out.println(webClient.register(arg[3], arg[4]));
				break;
			}
			case 2: {
				String[] arg = new String[9];
				System.out.println("用户名:");
				arg[3] = in.readLine();
				System.out.println("密码:");
				arg[4] = in.readLine();
				System.out.println("待办事项标题:");
				arg[6] = in.readLine();
				System.out.println("事项开始日期(格式:yyyy-M-dd):");
				arg[7] = in.readLine();
				System.out.println("事项截止日期(格式:yyyy-M-dd):");
				arg[8] = in.readLine();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd", Locale.US);
				Date start = format.parse(arg[7]);
				Date end = format.parse(arg[8]);
				System.out.println(webClient.add(arg[3], arg[4], arg[6], start, end));
				break;
			}
			case 3: {
				String[] arg = new String[7];
				System.out.println("用户名:");
				arg[3] = in.readLine();
				System.out.println("密码:");
				arg[4] = in.readLine();
				System.out.println("日期前限(格式:yyyy-M-dd):");
				arg[5] = in.readLine();
				System.out.println("日期后限(格式:yyyy-M-dd):");
				arg[6] = in.readLine();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd", Locale.US);
				Date start = format.parse(arg[5]);
				Date end = format.parse(arg[6]);
				System.out.println("ID" + "\t" + "Title" + "\t" + "userName" + "\t" + "StartTime" + "\t" + "endTime");
				System.out.println(webClient.query(arg[3], arg[4], start, end));
				break;
			}
			case 4: {
				String[] arg = new String[6];
				System.out.println("用户名:");
				arg[3] = in.readLine();
				System.out.println("密码:");
				arg[4] = in.readLine();
				System.out.println("待办事项ID:");
				arg[5] = in.readLine();
				int id = Integer.parseInt(arg[5]);
				System.out.println(webClient.delete(arg[3], arg[4], id));
				break;
			}

			case 5: {
				String[] arg = new String[5];
				System.out.println("用户名:");
				arg[3] = in.readLine();
				System.out.println("密码:");
				arg[4] = in.readLine();

				System.out.println(webClient.clear(arg[3], arg[4]));
				break;
			}

			case 6:
				isEnd = true;
				break;
			}
		}
	}
}