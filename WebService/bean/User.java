package bean;

/**
 * 用户类
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class User {

	private String username;// 用户名
	private String password;// 密码

	/**
	 * 无参构造函数，必须要有，否则绑定WebService后运行会出错
	 */
	public User() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取密码
	 * 
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "用户 [username=" + username + ", password=" + password + "]";
	}

}
