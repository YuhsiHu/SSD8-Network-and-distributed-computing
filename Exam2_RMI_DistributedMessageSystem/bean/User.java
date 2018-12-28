package bean;

import java.io.Serializable;

/**
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class User implements Serializable{
	private String username;// 用户名
	private String password;// 密码

	/**
	 * 构造函数
	 * @param username 用户名
	 * @param password 密码
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 获得用户名
	 * @return the username 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获得密码
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
