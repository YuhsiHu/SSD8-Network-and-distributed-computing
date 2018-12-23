package client;

import java.util.Date;

import java.util.Date;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 
 *List 
 *@author Hu Yuxi
 *@date 2018-12-21
 *
 */
@WebService(name = "ListImp", targetNamespace = "http://service/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ListImp {
    @WebMethod
    @WebResult(partName = "return")
	public String register(String userName, String password) ;
	
    @WebMethod
    @WebResult(partName = "return")
	public String add (String userName, String password, String title,Date startDate,Date endDate);
	
    @WebMethod
    @WebResult(partName = "return")
    public String query(String userName, String password,Date startDate,Date endDate );
    
    @WebMethod
    @WebResult(partName = "return")
    public String delete(String userName, String password,int todoID);
    
    @WebMethod
    @WebResult(partName = "return")
    public String clear(String userName, String password);

}
