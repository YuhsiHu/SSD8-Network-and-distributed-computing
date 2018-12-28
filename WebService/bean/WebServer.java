package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * 实现Web Service服务器
 * @author Hu Yuxi
 * @date 2018-12-28
 *
 */
@WebService(name="MyFirstService",  portName="servicePort", targetNamespace="http://www.service.com")
public class WebServer {
	private List<User> userList=new ArrayList<User>();//用户列表
	private List<TodoList> itemList=new ArrayList<TodoList>();//项目列表
	private int itemId=0;//项目ID
	/**
	 * 用户注册
	 * @param user 待注册的用户实例
	 * @return 注册是否成功
	 */
	@WebMethod
	public boolean userRegister(User user){
		if(userList==null){
			//用户列表为空，直接添加
			userList.add(user);
			return true;
		}
		for(User i:userList){
			//检测重名
			if(i.getUsername().equals(user.getUsername())){
				return false;
			}
		}
		//未检测到重名，添加
		userList.add(user);
		//返回添加成功
		return true;
	}
	/**
	 * 添加项目到代办事项列表
	 * @param creater 创建者的用户名
	 * @param item 待加入的事项
	 * @return 添加是否成功
	 */
	@WebMethod
	public boolean addItem(String creater,TodoList item){
		ArrayList<TodoList> list=new ArrayList<TodoList>();
		/*
		 * 获取该用户的所有item
		 */
		for(TodoList i:itemList){
			if(i.getCreater().equals(creater)){
				list.add(i);
			}
		}
		/*
		 * 若该用户没有待办事项，则直接添加项目，返回添加成功
		 */
		if(list.isEmpty()){//该用户无记录，直接添加
			itemId++;
			item.setTodoListID(itemId);//设置ID
			itemList.add(item);
			return true;//返回添加成功
		}
		/*
		 * 若该用户有待办事项，则检测时间冲突
		 */
		for(TodoList i:list){
			//检查时间冲突
			if(!i.getStartTime().after(item.getStartTime())&&!item.getStartTime().after(i.getEndTime())){
				//start夹在某个已存在项目的始末中间
				return false;
			}
			else if(!i.getStartTime().after(item.getEndTime())&&!item.getEndTime().after(i.getEndTime())){
				//end夹在某个已存在项目的始末中间
				return false;
			}
			else if(!item.getStartTime().after(i.getStartTime())&&!i.getStartTime().after(item.getEndTime())){
				//某个已存在项目的起始时间夹在start和end之间
				return false;
			}
			else if(!item.getStartTime().after(i.getEndTime())&&!i.getEndTime().after(item.getEndTime())){
				//某个已存在项目的结束时间夹在start和end之间
				return false;
			}
		}
		/*
		 * 未检测到时间冲突，则添加项目
		 */
		itemId++;
		item.setTodoListID(itemId);//设置ID
		itemList.add(item);
		//返回添加成功
		return true;
	}
	/**
	 * 查询项目
	 * @param creater 创建者的用户名
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return 结果列表，包含所有查找到的项目
	 */
	@WebMethod
	public ArrayList<TodoList> queryItem(String creater,Date start,Date end){
 
		/*
		 * 若itemList为空，直接返回null
		 */
		if(itemList.isEmpty()){
			return null;
		}
		/*
		 * 从itemList筛选出创建者的所有项目
		 */
		ArrayList<TodoList> result=new ArrayList<TodoList>();
		for(TodoList i:itemList){
			if(!start.after(i.getStartTime())&&!end.before(i.getEndTime())&&i.getCreater().equals(creater)){
				//条件：在时间区间内，且为creater创建
				result.add(i);
			}
		}
		/*
		 * 若该用户在该区间内没有项目，返回为空，否则返回结果列表
		 */
		if(result.isEmpty()){
			return null;//返回为空
		}else{
			return result;//返回结果列表
		}
	}
	/**
	 * 删除项目
	 * @param creater 创建者的用户名
	 * @param id 待删除的项目ID
	 * @return
	 */
	@WebMethod
	public boolean deleteItem(String creater,int id){
		//用来保存待删除的项目（不可直接在for循环内删除）
		TodoList deleted = null;
		
		for(TodoList i:itemList){
			/*
			 * 条件1:项目ID相同
			 * 条件2:该项目由该用户创建
			 */
			if(i.getTodoListID()==id&&i.getCreater().equals(creater)){
				deleted=i;
				break;
			}
		}
		if(deleted==null){
			//未找到所要删除的项目，返回删除失败
			return false;
		}else{
			//找到要删除的项目，删除
			itemList.remove(deleted);
			return true;
		}
	}
	/**
	 * 清空用户的代办事项列表
	 * @param creater 创建者的用户名
	 * @return 是否清空成功
	 */
	@WebMethod
	public boolean clearItem(String creater){
		ArrayList<TodoList> deleteList=new ArrayList<TodoList>();//待删除列表
		
		for(TodoList i:itemList){
			//判断创建者是否为用户
			if(i.getCreater().equals(creater)){
				//添加到待删除列表
				deleteList.add(i);
			}
		}
		if(deleteList.isEmpty()){
			//若deleteList为空，则直接返回true，清空成功
			return true;
		}
		for(TodoList i:deleteList){
			//对deleteList中的元素逐个删除
			itemList.remove(i);
		}
		//返回清空成功
		return true;
	}
	/**
	 * 主程序入口
	 * @param args
	 */
	public static void main(String[] args){
		Endpoint.publish("http://127.0.0.1:8001/webservice/myservice",new WebServer());
		//导出客户端代码:wsimport -keep http://127.0.0.1:8001/webservice/myservice?wsdl
		System.out.println("服务器发布成功");
		
	}
}
