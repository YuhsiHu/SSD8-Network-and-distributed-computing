package service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Hu Yuxi
 * @date 2018-12-21
 *
 */
public class TodoList implements Serializable{
	private static final long serialVersionUID=1L;
	private static int sequence=1;
	private int ListID=0;
	private String users=null;
	private String title=null;
	private Date startD;
	private Date endD;
	
    /**
     * 
     * @param user 
     * @param t 
     * @param s 
     * @param e 
     */
	public TodoList(String user, 
			       String t,Date s,Date e){
		this.users=user;
		this.ListID=sequence+1;
		this.title=t;
		this.startD=s;
		this.endD=e;
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getUser(){
		return users;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId(){
		return ListID;		
	}
	
	/**
	 * 
	 * @return 
	 */
	public Date getStartDate(){
		return startD;
	}
	
	/**
	 * 
	 * @return 
	 */
	public Date getEndDate(){
		return endD;	
	}
	
	/**
	 * 
	 * @return 
	 */
	public String showList(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-M-dd",Locale.US);			    
		return ListID+"\t"+title+"\t"+users+"\t"+
		format.format(startD)+"\t"+format.format(endD);
	}
	
	/**
	 * 
	 * @param s 
	 * @param e
	 * @return
	 */
	public boolean isBetween(Date s,Date e){
		return true;
	}
}
