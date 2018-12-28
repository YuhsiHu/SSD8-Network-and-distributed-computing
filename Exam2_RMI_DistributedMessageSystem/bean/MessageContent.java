package bean;

import java.io.Serializable;
import java.util.Date;

/**
 * define the class of message
 * 
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
public class MessageContent implements Serializable{
	private String sender;// 发送者
	private String receiver;// 接收者
	private Date sendTime;// 发送时间
	private String text;// 留言内容

	/**
	 * 
	 * @return 发送者
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * 
	 * @param sender
	 *            发送者
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * 
	 * @return 接收者
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * 
	 * @param receiver
	 *            接收者
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * 
	 * @return 发送时间
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * 
	 * @param sendTime
	 *            发送时间
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * 
	 * @return 留言内容
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 *            留言内容
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Message\n\tSender:" + sender + "\n\tReceiver:" + receiver + "\n\tContent:" + text + "\n\tSend Time:"
				+ sendTime;
	}

}
