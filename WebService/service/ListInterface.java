package service;

import java.util.Date;

/**
 * @author Hu Yuxi
 * @date 2018-12-21
 *
 */
public interface ListInterface {
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public String register(String userName, String password);
		
	/**
	 * 
	 * @param userName
	 * @param password 
	 * @param title
	 * @param startDate
	 * @param endDate 
	 * @return 
	 */
	public String add (String userName, String password, String title,Date startDate,Date endDate);
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param startDate 
	 * @param endDate 
	 * @return
	 */
    public String query(String userName, String password,Date startDate,Date endDate );
    
    /**
     * 
     * @param userName
     * @param password
     * @param todoID
     * @return
     */
    public String delete(String userName, String password,int todoID);
    
    /**
     * 
     * @param userName
     * @param password
     * @return
     */
    public String clear(String userName, String password);

}
