package org.tzdr.util;

import org.tzdr.base.BaseConfig;

import io.vertx.redis.RedisClient;
import io.vertx.redis.impl.RedisClientImpl;

/**
 * @author 狐妖小红娘
 * @version 2018年12月8日 下午2:15:19
 * 练习一下vert.x对Redis的封装
 */
public class RedisClientUtil {
	private static final RedisClient redis=new RedisClientImpl(BaseConfig.vertx, BaseConfig.Default_Redis_Config);
	
	
	
//	安装服务    redis-server  --service-install redis.windows.conf
//	卸载服务    redis-server  --service-uninstall
//	启动服务             redis-server --service-start
//	停止服务             redis-server --service-stop
//	redis-cli.exe -h 服务器 -p 端口 -a 密码         如果不使用密码登录的话！会没有权限操作   auth  password

	public static void main(String[] args) {
		redis.dbsize(res -> {
			if (res.succeeded()) {
				
				System.out.println(res.result());
			} else {
				System.out.println(res.cause().getMessage());
			}
		});
	}
}
