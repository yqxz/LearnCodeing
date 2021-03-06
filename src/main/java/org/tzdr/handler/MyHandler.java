package org.tzdr.handler;

import java.util.Date;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author 狐妖小红娘
 * @version 2018年12月7日 上午8:21:27
 * 	实现一个测试用的handler
 * 	注意每一个route加入Router的顺序！
 */
public class MyHandler implements Handler<RoutingContext>{
	

	@Override
	public void handle(RoutingContext context) {
		// TODO Auto-generated method stub
		System.out.println("MyHandler");
//		context.request().headers().forEach(res ->{
//			System.out.println(res.toString());
//		});
//		
//		context.fileUploads().forEach(file -> {
//			System.out.println("文件名是:"+file.fileName());
//		});
		
		context.put("title", 1);
		context.put("id", 2);
		context.put("newPage", "test");
		context.put("rawContent", "rawContent");
		context.put("content", "content");
		context.put("timestamp", new Date().toString());
		
		System.out.println(context.fileUploads());
		System.out.println("这里并不会做任何事情！只是一个简单的返回");
		context.response().setChunked(true).write("Something has happened, so handle it in MyHandler").end();
	}
}
