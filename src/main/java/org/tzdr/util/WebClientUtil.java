package org.tzdr.util;

import java.io.File;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import org.tzdr.base.BaseConfig;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;

/**
 * @author 狐妖小红娘
 * @version 2018年12月7日 上午11:07:14
 * 	练习webClient实现HTTP客户端
 */
public class WebClientUtil {
	/**
	 * 	歌名<故梦>   想看鲁迅的两地书
	 * 	闲言碎语！下面的实现大多可以编写为流式的API，不过为了过程更清晰，易于理解每一步干了什么，暂时用这种方式实现！
	 * 	不知道，流式的API和这样的分步书写运行起来有什么区别！实现的速度不一样还是
	 * 	注意每一步流式操作执行后的结果是什么
	 * 	喜欢神之血吗？还是喜欢人呢  like  god  like man   like  you always
	 * 	怎么样的憧憬是你一直想去保持的呢！我们在字里行间怎么可能看得出来   如果你不说的话
	 * 	
	 */
	
	/**
	 * 	客户端配置
	 */
	private static final WebClientOptions options=new WebClientOptions()
			.setSsl(true)			//启用ssh
			.setVerifyHost(false)	//不启用主机名验证
			.setTrustAll(true);		//信任所有的服务器
	/**
	 * 	初始化客户端
	 */
	private static final WebClient webClient=WebClient.create(BaseConfig.vertx,options);
	
	/**
	 * 	发送一个GET请求
	 * @param url
	 * @return
	 */
	public static Future<String> getString(String url) {
		Future<String> future=Future.future();
		webClient.getAbs(url).send(ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body().toString());
			} else {
				future.fail("发送get请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个GET请求,返回一个JsonObject对象
	 * @param url
	 * @return
	 */
	public static Future<JsonObject> getJson(String url) {
		Future<JsonObject> future=Future.future();
		webClient.getAbs(url)
			.as(BodyCodec.jsonObject()).send(ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body());
			} else {
				future.fail("发送get请求"+url+""
						+ "失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个无参数POST请求
	 * @param url
	 * @param params	
	 * @return
	 */
	public static Future<String> postString(String url) {
		Future<String> future=Future.future();
		 webClient.postAbs(url)
		 	.send(ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body().toString());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个POST请求，上传文件
	 * @param url
	 * @param file
	 * @return			我将这个部署在自己的那台破电脑上的时候！这个sendMultipartForm方法就失败了，然后return都没有！
	 * 					我很郁闷但是调试也毫无结果,我的电脑少了一些Vert.x的低层实现吗
	 */
	public static Future<String> postString(String url,File file) {
		MultipartForm form=MultipartForm.create();
		//除了文件的绝对路径，其他的参数都不是必须的！但是不能为null    但是加了这个方法容易跑不通！不知道为什么，我现在这个电脑的问题
		form.binaryFileUpload(file.getName(), file.getName(), file.getAbsolutePath(), new MimetypesFileTypeMap().getContentType(file));
		Future<String> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
			request.sendMultipartForm(form,ar -> {
				if (ar.succeeded()) {
					future.complete(ar.result().body().toString());
				} else {
					future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
				}
			});
		return future;
	}
	
	/**
	 * 	发送一个无参数POST请求
	 * @param url
	 * @param params	
	 * @return
	 */
	public static Future<JsonObject> postJson(String url) {
		Future<JsonObject> future=Future.future();
		 webClient.postAbs(url)
		 	.as(BodyCodec.jsonObject())
		 	.send(ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	/**
	 * 	发送一个POST请求
	 * @param url
	 * @param params	
	 * @return
	 */
	public static Future<String> postString(String url,JsonObject params) {
		Future<String> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		params.forEach(k -> {
			request.addQueryParam(k.getKey(), k.getValue().toString());
		});
		 request.send( ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body().toString());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	/**
	 * 	发送一个POST请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static Future<String> postString(String url,Map<String, String> params) {
		Future<String> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		params.forEach((k,v) -> {
			request.addQueryParam(k, v);
		});
		request.send(ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body().toString());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个POST请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static Future<String> postString(String url,MultiMap params) {
		Future<String> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		request.sendForm(params,ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body().toString());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个POST请求！返回JsonObject的结果
	 * @param url
	 * @param params
	 * @return
	 */
	public static Future<JsonObject> postJson(String url,JsonObject params) {
		Future<JsonObject> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		params.forEach(k -> {
			request.addQueryParam(k.getKey(), k.getValue().toString());
		});
		request.as(BodyCodec.jsonObject()).sendJsonObject(params, ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个POST请求！返回JsonObject的结果
	 * @param url
	 * @param params
	 * @return
	 */
	public static Future<JsonObject> postJson(String url,Map<String, String> params) {
		Future<JsonObject> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		params.forEach((k,v) -> {
			request.addQueryParam(k, v);
		});
		request.as(BodyCodec.jsonObject()).send(ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个POST请求，返回一个JsonObject对象
	 * @param url
	 * @param params
	 * @return
	 */
	public static Future<JsonObject> postJson(String url,MultiMap params) {
		Future<JsonObject> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		request.as(BodyCodec.jsonObject()).sendForm(params,ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	/**
	 * 	发送一个POST请求，上传文件
	 * @param url
	 * @param file
	 * @return
	 */
	public static Future<JsonObject> postJson(String url,File file) {
		MultipartForm form=MultipartForm.create();
		//除了文件的绝对路径，其他的参数都不是必须的！但是不能为null
		form.binaryFileUpload(file.getName(), file.getName(), file.getAbsolutePath(), new MimetypesFileTypeMap().getContentType(file));
		Future<JsonObject> future=Future.future();
		HttpRequest<Buffer> request = webClient.postAbs(url);
		request.as(BodyCodec.jsonObject())
			.sendMultipartForm(form,ar -> {
			if (ar.succeeded()) {
				future.complete(ar.result().body());
			} else {
				future.fail("发送post请求"+url+"失败::"+ar.cause().getMessage());
			}
		});
		return future;
	}
	
	public static void main(String[] args) {
		String url="http://localhost:541/rest/myhandler";
		postJson(url, new File("C:\\Users\\wyf\\Desktop\\如果喜欢的话.jpg")).setHandler(res -> {
			if (res.succeeded()) {
				System.out.println(res.result());
			} else {
				System.out.println(res.cause().getMessage());
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
