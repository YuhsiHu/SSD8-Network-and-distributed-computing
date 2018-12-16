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
FileClient：客户端  
FileServer：服务器端  
Handler：执行客户端与服务器端交互

Exercise 2：Web服务器和客户端
--------------------------------------------
//TODO




Exam 1:HTTP Proxy Server
---------------------------------------------
基于实验一和实验二，逐一分析报文发现，浏览器在打开网页时候会先有CONNECT的请求，这个是没有处理过的，并且许多网页后缀并不是.html，而是带有各种参数。这导致一开始就会有异常抛出，不会进行接下来的响应。所以，基于此我只能写出控制台上发送GET请求将文件保存到客户端运行目录当中的代码。一共有6个类。

在参考网上代码之后，实现了SingleHttpProxyServer.java，这个可以实现通过浏览器打开想要的网页。
  
代码文件夹中包含7个.java文件，分别为：  
   *  `Client.java`  
   * `Handler.java`  
   * `HttpClient.java`  
   * `HttpProxyHandler.java`  
   * `HttpProxyServer.java`  
   * `HttpServer.java`  
   * `SingleHttpProxyServer.java`（单独运行即可，用浏览器打开网页）  

其中Client.java、HttpProxyServer.java和HttpServer.java可作为Java应用运行。各个文件的功能分别为：  
 * Client.java：实例化客户端  
 * Handler.java：实现服务器的单线程任务  
 * HttpClient.java：实现客户端的功能  
 * HttpProxyHandler.java：实现代理服务器功能及单线程任务  
 * HttpProxyServer.java：创建代理服务器的线程池，实例化代理服务器  
 * HttpServer.java：创建服务器的线程池，实例化服务器  

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