package org.tzdr.base;


import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.redis.RedisOptions;

/**
* @author 狐妖小红娘
* @version 创建时间：2018年12月1日 下午11:49:39
* 类说明	一些基础的配置
*/
public class BaseConfig {
	/**
	 * 每个目录的开头都是一段比较琐碎的话！用来记录一些事情
	 * 我们可以尝试着去在handle里面去用return   
	 * 打破一些定势的思维,而不只是if else语句来确定执行的流程
	 * 西安的雪变的越来越小心翼翼了！每一年都要期盼好久才会到来,往年我是除夕才会想起你的,现在已经不是了,可能随时会想起,又好像从来没忘记过
	 */
	public static final Vertx vertx=Vertx.vertx();
	/**
	 * 所有练习的一个主路由
	 */
	public static final Router Main_Router=Router.router(BaseConfig.vertx);
	/**
	 * 一个练习的子路由
	 */
	public static final Router restAPI=Router.router(BaseConfig.vertx);
	/**
	 * 一个默认的JDBC配置文件
	 */
	public static final JsonObject Default_Jdbc_Config=new JsonObject()
				.put("user", "root")
				.put("password", "1225")
				.put("url", "jdbc:mysql://localhost:3306/test?useSSL=true")
//				.put("url", "jdbc:mysql://localhost:3306/test?useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8")
				.put("driver_class", "com.mysql.cj.jdbc.Driver");
	/**
	 * mySQLClient的默认配置文件
	 * 这个客户端还需要org.slf4j的依赖！但是它竟然在pom里自己不加入、不加入、不加入，我想骂它
	 */
	public static final JsonObject mySQLClientConfig=new JsonObject()
				.put("host", "localhost")
				.put("port", 3306)
				.put("maxPoolSize", 10)
				.put("username", "root")
				.put("password", "1225")
				.put("database", "test")
				.put("charset", "UTF-8")
				.put("queryTimeout", 10000);
	/**
	 * 默认的Redis配置文件
	 */
	public static final RedisOptions Default_Redis_Config=new RedisOptions()
				.setHost("localhost")
				.setPort(6379)
				.setEncoding("UTF-8")
				.setAuth("1225");
	/**
	 * 挂载所有的子路由到主路由		
	 * 你得去思考不同的路由！部署的时候我们该怎么去一次初始化启动它
	 * 它是否更优于只使用一个路由来处理你所有的handler
	 */
	static {
		BaseConfig.Main_Router.mountSubRouter("/rest",BaseConfig.restAPI);
	}
	
	/**
	 * 	在这里做一些公用的操作,比如跨域请求的设置、所有对静态资源的访问！子路由和主路由是否都需要分别取设置跨域的访问请求
	 */
	static {
		BaseConfig.Main_Router.route().handler(CorsHandler.create("*")).handler(BodyHandler.create());
		BaseConfig.restAPI.route().handler(CorsHandler.create("*")).handler(BodyHandler.create());
	}
}