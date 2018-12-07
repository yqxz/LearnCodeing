package org.tzdr.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author 狐妖小红娘
 * @version 2018年12月7日 上午8:21:27
 * 	实现一个测试用的handler
 * 	注意每一个route加入Router的顺序！
 */
public class MyHandler implements Handler<RoutingContext>{
	
	
//	${package_declaration}
//
//	/**
//	 * @author 狐妖小红娘
//	 * @version ${date} ${time}
//	 */
//	${type_declaration}

	@Override
	public void handle(RoutingContext context) {
		// TODO Auto-generated method stub
		context.request().headers().forEach(res ->{
			System.out.println(res.toString());
		});
		
		context.fileUploads().forEach(file -> {
			System.out.println("文件名是:"+file.fileName());
		});
		System.out.println(context.fileUploads());
		System.out.println("这里并不会做任何事情！只是一个简单的返回");
		context.response().setChunked(true).write("Something has happened, so handle it in MyHandler").end();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
