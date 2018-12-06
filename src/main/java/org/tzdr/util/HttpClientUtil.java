package org.tzdr.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import org.tzdr.base.BaseConfig;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author 狐妖小红娘
 * @version 2018年12月6日 上午9:13:21
 * 练习HTTP客户端的实现
 * 一团糟糕的测试和练习！核心包有太多的bug是现在的我解决不了的
 */
public class HttpClientUtil {
	
	private static Logger logger=LoggerFactory.getLogger(HttpClientUtil.class);
	/**
	 * 创建一个发送 基于 TLS h2的HTTP/2 请求     使用应用层协议协商(ALPN)启用TLS
	 * 其实它现在是一个发送HTTP1.1请求的客户端
	 */
	private static final HttpClient http_core_Client=BaseConfig.vertx.createHttpClient(new HttpClientOptions()
//			.setProtocolVersion(HttpVersion.HTTP_2)   使用它必须启用APLN  但是会报错
//		    .setUseAlpn(true)    启用这个方法会报错！暂时没找出来好的办法
			.setSsl(true)
			.setVerifyHost(false)
		    .setTrustAll(true)
		    .setIdleTimeout(2)
		   );
	/**
	 * 发送一个get请求
	 * @param url
	 * @return
	 */
	public static Future<String> sendGet(String url) {
		Future<String> future=Future.future();
		try {
			HttpClientRequest request = http_core_Client.getAbs(url, response->{
				response.bodyHandler(body->{
					future.complete(body.toString());
				});
			});
			request.headers().set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			request.end();
		} catch (Exception e) {
			logger.error("请求失败:"+e.getMessage());
			future.complete(e.getMessage());
		}
		return future;
	}
	
	/**
	 * 发送一个简单的POST请求
	 * @param url		要请求的URL
	 * @param params	请求携带的参数
	 * @return
	 */
	public static Future<String> sendPost(String url,Map<String, String> params) {
		Future<String> future=Future.future();
		try {
			HttpClientRequest request = http_core_Client.postAbs(url, response->{
				response.bodyHandler(body->{
					future.complete(body.toString());
				});
			});
			request.headers().set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			
			if (params.size()>0) {
				StringBuilder sb=new StringBuilder();
				params.forEach((k,v)->{
						sb.append(k+"="+v+"&");
				});
				sb.deleteCharAt(sb.length()-1);
				request.setChunked(true).write(sb.toString());
			}
			request.end();
		} catch (Exception e) {
			logger.error("请求失败:"+e.getMessage());
			future.complete(e.getMessage());
		}
		return future;
	} 
	
	/**
	 *一个失败的练习！它现在还不能发送一个文件流到后台去
	 * @param url
	 * @param file
	 * @return
	 */
	public static Future<String> sendFile(String url,File file) {
		Future<String> future=Future.future();
		try {
			HttpClientRequest request = http_core_Client.postAbs(url, response->{
				response.bodyHandler(body->{
					future.complete(body.toString());
				});
			});
			request.headers().set(HttpHeaders.CONTENT_TYPE, "multipart/form-data;boundary=-----WebKitFormBoundaryX3mHuP4Uhvo8Zy3O");
			InputStream input=new FileInputStream(file);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte[] bs = new byte[1024];  
            int len; 
            while (-1 != (len = input.read(bs))) {
                bos.write(bs, 0, len);
            }
            input.close();
			bos.close();
			Buffer buffer=Buffer.buffer(bos.toByteArray());
			request.setChunked(true).write(buffer);
			request.end(buffer);
		} catch (Exception e) {
			logger.error("请求失败:"+e.getMessage());
			future.complete(e.getMessage());
		}
		return future;
	}
	
	public static void main(String[] args) {
		File file=new File("C:\\Users\\wyf\\Desktop\\如果喜欢的话.jpg");
		sendFile("http://localhost:5412/mapway/inspection/uav/img/upload", file);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
