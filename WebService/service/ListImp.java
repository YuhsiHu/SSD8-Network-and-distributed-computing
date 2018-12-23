package service;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 
 * @author Hu Yuxi
 * @date 2018-12-21
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class ListImp implements ListInterface{

	private Vector<TodoList> lists= new Vector<TodoList>();
	private HashMap<String,String> users=new HashMap<String,String>();
	
	/**
	 * 
	 * @throws Exception
	 */
	public ListImp() throws Exception{
		
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return 
	 * @throws RemoteException
	 */
	private boolean login(String userName, String password){
		if(!users.containsKey(userName)){
			return false;
		}
		else{
			
			if(((String)users.get(userName)).equals(password)){
			return true;	
			}
			else return false;
		}
		
	}
	
	/**
	 * 
	 * @param userName
	 * @param startD
	 * @param endD
	 * @return 
	 */
	private boolean isBusy(String userName,Date startD,Date endD){
		Vector<TodoList> utodo=new Vector<TodoList>();
		
		for(int i=0; i<lists.size(); ++i){
			TodoList list=lists.get(i);
			if(userName.equals(list.getUser())){
				utodo.add(list);
			}			
		}
		
		for(int i=0; i<utodo.size(); ++i){
			TodoList list=utodo.get(i);
			if(list.isBetween(startD,endD)){
				return true;
			}
			
		}
		return false;		
	}
	

	
	/**
	 * 
	 */
	@WebMethod
	@Override
	public String register(String userName, String password) {
		if(!users.containsKey(userName)){
			users.put(userName,password);
			return "Register successfully!";
		}
		else{
			return "User exists!";
		}
			
	}
	
	/**
	 * 
	 */
	@WebMethod
	@Override
	public String add (String userName, 
			        String password,
		        	String title,
		        	Date startDate,Date endDate){
		;
		if(login(userName,password)){
		    if(isBusy(userName,startDate,endDate)){
			   return "Invalid time!";		
		    }
		    else{
				TodoList list=new TodoList(userName,title,startDate,endDate);
		        lists.add(list);
		        return "Add successfully!";	
		    }
		}
		else return "User does not exist!";

	}
	
	/**
	 * 
	 */
	@WebMethod
	@Override
    public String query(String userName, 
    		                    String password,Date 
    		                    startDate,Date endDate ){
		String show=null;
		boolean isnull=true;
		if(!login(userName,password)){
			return "Log in failed!";
		}
		for(int i=0; i<lists.size(); i++){
			TodoList list=lists.get(i);
			if(list.isBetween(startDate, endDate)){
	             show=list.showList()+"\n";
	             isnull=false;
			}

		}
		if(!isnull){
			return show;
		}
		else
		return "User does not exist!";
    }
    
	/**
	 * 
	 */
	@WebMethod
	@Override
    public String delete(String userName,
    		String password,int todoID){
		if(!login(userName,password)){
			return "Log in failed!";
		}
		for(int i=0;i<lists.size();i++){
			TodoList todo=lists.get(i);
			if(todo.getId()==todoID){
				lists.remove(i);
		    	return "Delete successfuly!";
			}
		}
	   return "User does not exist!";

    }
    
	/**
	 * 
	 */
	@WebMethod
	@Override
    public String clear(String userName, String password){

		if(!login(userName,password)){
			return "Log in failed";
		}
		for(int i=0;i<lists.size();++i){
			TodoList list=lists.get(i);
			if(userName.equals(list.getUser())){
				lists.remove(i);			
			}
		}
		return "Clear successfully!";
    }
	

}
