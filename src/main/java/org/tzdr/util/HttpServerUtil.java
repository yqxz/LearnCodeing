package org.tzdr.util;

import org.tzdr.base.BaseConfig;
import org.tzdr.handler.MyHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
* @author 狐妖小红娘
* @version 创建时间：2018年12月1日 下午11:47:53
* 类说明	练习Vert.x编写HTTP服务端和客户端
*/
public class HttpServerUtil {


	/**
	 * core包练习http服务端
	 */
	public static void HttpServer() {
		//配置HTTP服务端
		HttpServerOptions options = new HttpServerOptions().setPort(541).setHost("localhost").setMaxWebsocketFrameSize(1000000);
		//创建HTTP服务端
		HttpServer server  = BaseConfig.vertx.createHttpServer(options);
		//先创建requestHandler再去监听,不然会抛出异常
		server.requestHandler(request->{
			request.bodyHandler(r->{
					//分块编码需要添加的头信息
//				 request.response().putHeader("Transfer-Encoding", "chunked").write("你是不是兔子").write("nihao").end();
//				 request.response().sendFile("C:\\Users\\Administrator\\Desktop\\Core · Vert.x 官方文档中文翻译_files\\style.css");
				System.out.println("nihao"); 
				if (request.path().equals("/hello")) {
					 request.response().setChunked(true).write("测试下").write("nihao").end(new JsonObject().put("result", true).put("data","啊tring").toString());
				 }
			});
		});
		server.listen(res -> {
			  if (res.succeeded()) {
			    System.out.println("Server is now listening!");
			  } else {
			    System.out.println("Failed to bind!");
			  }
		});
	}
	
	public static void HttpWebServer() {
		HttpServerOptions options=new HttpServerOptions();
		options.setHost("localhost").setPort(541);
		HttpServer server  = BaseConfig.vertx.createHttpServer(options);
		//被加载的第一个route
		Route route2 = BaseConfig.Main_Router.route("/main");
		route2.handler(routingContext->{
			// 所有的请求都会调用这个处理器处理
			  HttpServerResponse response = routingContext.response();
			  response.putHeader("content-type", "text/plain");
			  // 写入响应并结束处理
			  response.end("Hello World from MAIN_ROUTER!");
		});
		
		//被加载的第二个route
		Route route = BaseConfig.restAPI.route("/restapi");
		route.handler(routingContext->{
			// 所有的请求都会调用这个处理器处理
				System.out.println(Integer.valueOf("sad"));
			  HttpServerResponse response = routingContext.response();
			  response.putHeader("content-type", "text/plain");
			  // 写入响应并结束处理
			  response.end("Hello World from restAPI");
		});
		//第二个route的失败处理handler
		route.failureHandler(fail->{
			System.out.println("程序出现错误:"+fail.failure().getMessage());
			fail.response().end("a problem ource  in you ");
		});
		
		//第三个route认证练习延后
//		AuthProvider authProvider =null;
//		authProvider.authenticate(new JsonObject().put("username","tuzi").put("password", "daren"), res->{
//			if (res.succeeded()) {
//
//			    System.out.println("is ok");
//
//			  } else {
//			    res.cause().printStackTrace();
//			  }
//		});
//		AuthHandler basicAuthHandler  = BasicAuthHandler.create(authProvider);
//		Route route3 = BaseConfig.restAPI.route("/hello");
//		route3.handler(basicAuthHandler);
		
		BaseConfig.restAPI.route().handler(CorsHandler.create("*"));
		
		BaseConfig.restAPI.route(HttpMethod.POST,"/myhandler").handler(new MyHandler());
		
//		BaseConfig.restAPI.route("/myhandler/*").handler(routingContext->{
//			// 所有的请求都会调用这个处理器处理
//		  HttpServerResponse response = routingContext.response();
//		  response.putHeader("content-type", "text/plain");
//		  // 写入响应并结束处理
//		  response.write("I will be end something");
//		  routingContext.next();
//		});
		
		
//		BaseConfig.restAPI.route("/myhandler/*").handler(routingContext->{
//			// 所有的请求都会调用这个处理器处理
//		  HttpServerResponse response = routingContext.response();
//		  response.putHeader("content-type", "text/plain");
//		  // 写入响应并结束处理
//		  response.end("I from a next() method");
//		});
		
		StaticHandler staticHandler = StaticHandler.create();
		staticHandler.setWebRoot("src/main/webapp");
		BaseConfig.restAPI.route("/static/*").handler(staticHandler);
		//CorsHandler  一个已经实现了的跨域资源请求类！而且对预检命令进行了处理
		//开启服务监听
		server.requestHandler(BaseConfig.Main_Router).listen(listen->{
			if (listen.succeeded()) {
				System.out.println("服务监听成功");
			} else {
				System.out.println("服务启动失败"+listen.cause().getMessage());
			}
		});
	}
	
	public static void main(String[] args) {
		HttpWebServer();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
