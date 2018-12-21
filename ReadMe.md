SSD8 Network and Distributed Computing：Exercise and Exam
=============================================
Exercise 1 基于TCP&UDP的网络文件服务
---------------------------------------------
1)	复习Java I/O和Socket编程相关概念和方法；
2)	基于Java Socket TCP和UDP实现一个简易的网络文件服务程序，包含服务器端FileServer和客户端FileClient；
3)	服务器端启动时需传递root目录参数，并校验该目录是否有效；
4)	服务器启动后，开启TCP：2021端口，UDP：2020端口，其中，TCP连接负责与用户交互，UDP负责传送文件；
5)	客户端启动后，连接指定服务器的TCP 2021端口，成功后，服务器端回复信息：“客户端IP地址:客户端端口号>连接成功”；
6)	连接成功后，用户可通过客户端命令行执行以下命令：
	[1]	ls	服务器返回当前目录文件列表（<file/dir>	name	size）
	[2]	cd  <dir>	进入指定目录（需判断目录是否存在，并给出提示）
	[3]	get  <file>	通过UDP下载指定文件，保存到客户端当前目录下
	[4]	bye	断开连接，客户端运行完毕
7)	服务器端支持多用户并发访问，不用考虑文件过大或UDP传输不可靠的问题。

File System：  
* `FileClient`：客户端  
* `FileServer`：服务器端  
* `Handler`：执行客户端与服务器端交互

Exercise 2：Web服务器和客户端
--------------------------------------------
  代码`Client.java`和`HTTPClient.java`实现了基于命令行的HTTP客户端的GET请求的功能。将服务器地址（域名或IP地址）设置为启动参数，如www.nwpu.edu.cn，启动客户端后，将建立到服务器80端口的TCP连接，用户可通过控制台输入GET命令实现服务器资源的请求。
请完善`HTTPClient.java`中的processPutRequest方法使其支持PUT命令。为了简化操作，可直接将客户端运行环境的某文件（如face.jpg）PUT到服务器指定的路径，如：PUT /smile.jpg HTTP/1.0。  
 
  提示：PUT请求时Header中必须包含“Content-Length”项。  

  构建单线程的HTTP服务器。  
  编写HTTP服务器，创建一个端口号为80的套接字，并等待传入的请求。服务器只需成功地响应使用GET和PUT方法的请求，但对其他所有请求（有效的或无效的）的都应返回RFC指定的响应。在任何情况下确保服务器不会崩溃或挂起。  
  服务器的根目录，即http://<your-computer>.<your-domain>/ 应指向在命令行中传递给程序的目录。例如，根目录设置为服务器端c:\www目录，则当客户端请求GET /test/index.html时，服务器应向客户端发送文件C:\www\test\index.html来响应请求，如果此文件不存在，则返回404响应。  
  在成功的响应中，服务器应该指定至少两种MIME类型。服务器应该从文件的扩展名中推导出一个文件的MIME类型，例如，文件名以.htm或.html结尾的文件假定为text / html类型。服务器应至少识别HTML和JPEG文件，即文件扩展名分别为.htm和.jpg的文件。服务器必须能够将.jpg图像嵌入到HTML文档中。此时，一次只需为一个连接提供服务。也就是说，服务器是一个无限循环，读取请求并发出响应。  

  将服务器修改为支持并发接收请求的模式。    
  Web服务器通常能够处理并发请求。并发请求指当一个请求未处理完时另外一个请求同时开始处理。请将前面步骤中建立的Web服务修改为支持并发接收请求的模式，可以采用Java多线程或线程池的方式。  

  参数说明： 
   *  1.Client主函数传参：localhost       
   *  2.客户端GET到的文件在工程目录下，客户端想要上传PUT的文件应该存在桌面      
   *  3.客户端向服务器端PUT的文件在被保存webroot/saving之下，重复PUT会直接覆盖      
   *  4.Java doc在工程目录下doc中      

Exercise 3:RMI分布式议程服务
---------------------------------------------
  1)	复习RMI的基本概念和原理方法；  
  2)	使用Java RMI创建一个分布式议程共享服务。不同的用户可以使用这个共享议程服务执行查询、添加和删除会议的操作。服务器支持会议的登记和清除等功能；  
  3)	议程共享服务包括以下功能：用户注册、添加会议、查询会议、删除会议、清除会议；  
  4)	用户注册  
  新用户注册需要填写用户名和密码，如果用户名已经存在，则注册失败并打印提示信息，否则注册成功并打印成功提示。使用如下命令进行用户注册：  
  java [clientname] [servername] [portnumber] register [username] [password]    
  5)	添加会议
  已注册的用户可以添加会议。会议必须涉及到两个已注册的用户，一个只涉及单个用户的会议无法被创建。会议的信息包括开始时间、结束时间、会议标题、会议参与者。当一个会议被添加之后，它必须出现在创建会议的用户和参加会议的用户的议程中。如果一个用户的新会议与已经存在的会议出现时间重叠，则无法创建会议。最终，用户收到会议添加结果的提示。使用如下命令进行会议的添加：    
  java [clientname] [servername] [portnumber] add [username] [password] [otherusername] [start] [end] [title]  
  6)	查询会议  
  已注册的用户通过给出特定时间区间（给出开始时间和结束时间）在议程中查询到所有符合条件的会议记录。返回的结果列表按时间排序。在列表中，包括开始时间、结束时间、会议标题、会议参与者等信息。使用如下命令进行会议的查询：  
  java [clientname] [servername] [portnumber] query [username] [password] [start] [end]  
  7)	删除会议  
  已注册的用户可以根据会议的信息删除由该用户创建的会议。使用如下命令进行删除会议操作：   
  java [clientname] [servername] [portnumber] delete [username] [password] [meetingid]  
  8)	清除会议  
  已注册的用户可以清除所有由该用户创建的会议。使用如下命令进行清除操作：  
  java [clientname] [servername] [portnumber] clear [username] [password]  

  
代码文件夹中包含6个.java文件，分别为：  
   *  `Meeting.java`  
   * `User.java`  
   * `RMIClient.java`  
   * `MeetingInterface.java`  
   * `MeetingInterfaceImpl.java`  
   * `RMIServer.java`  
    

各个文件的功能分别为：  
 * `Meeting.java`：会议类  
 * `User.java`：用户类 
 * `RMIClient.java`：实现客户端的功能  
 * `MeetingInterface.java`：会议接口，继承remote  
 * `MeetingInterfaceImpl.java`：实现会议接口  
 * `RMIServer.java`：实现服务器端功能 

在运行时，按先后次序运行RMIServer.java->RMIClient.java，在Client的控制台界面按格式输入请求，即可观察到预期功能的实现。

参数说明：  
客户端：  
 * 主函数传参：localhost 1099 register 用户名 密码     
 * 添加会议：add abc（其他用户） 2018-12-21-11:15 2018-12-21-11:30 testMeeting（会议名）  
 * 查找会议：query 2018-12-21-11:00 2018-12-21-12:00  
 * 删除会议：delete 0（会议ID），只有用户自己创建的可以自己删除  
 * 清除会议：clear，可以清除所有由自己创建的会议    

Exercise 4：待办事项列表
--------------------------------------------
//TODO



Exam 1:HTTP Proxy Server
---------------------------------------------
基于实验一和实验二，逐一分析报文发现，浏览器在打开网页时候会先有CONNECT的请求，这个是没有处理过的，并且许多网页后缀并不是.html，而是带有各种参数。这导致一开始就会有异常抛出，不会进行接下来的响应。所以，基于此我只能写出控制台上发送GET请求将文件保存到客户端运行目录当中的代码。一共有6个类。

在参考网上代码之后，实现了` SingleHttpProxyServer.java` ，这个可以实现通过浏览器打开想要的网页。
  
代码文件夹中包含7个.java文件，分别为：  
   *  `Client.java`  
   * `Handler.java`  
   * `HttpClient.java`  
   * `HttpProxyHandler.java`  
   * `HttpProxyServer.java`  
   * `HttpServer.java`  
   * `SingleHttpProxyServer.java`（单独运行即可，用浏览器打开网页）  

其中Client.java、HttpProxyServer.java和HttpServer.java可作为Java应用运行。各个文件的功能分别为：  
 * `Client.java`：实例化客户端  
 * `Handler.java`：实现服务器的单线程任务  
 * `HttpClient.java`：实现客户端的功能  
 * `HttpProxyHandler.java`：实现代理服务器功能及单线程任务  
 * `HttpProxyServer.java`：创建代理服务器的线程池，实例化代理服务器  
 * `HttpServer.java`：创建服务器的线程池，实例化服务器  

在运行时，按先后次序运行HttpServer.java->HttpProxyServer.java->Client.java，在Client的控制台界面按格式输入请求，即可观察到预期功能的实现。

参数说明：  
客户端：  
 * 运行目录：工程目录中   
   
服务器：  
*  根目录：默认为D:\Server  
*  端口：默认为80  
* 主机：默认本机  

代理服务器：  
* 根目录：默认为D:\ProxyServer  
* 端口：默认为8000  
* 主机：默认本机   
