package org.tzdr.util;

import org.tzdr.base.BaseConfig;

import io.vertx.redis.RedisClient;
import io.vertx.redis.impl.RedisClientImpl;

/**
 * @author 狐妖小红娘
 * @version 2018年12月8日 下午2:15:19
 * 	练习一下vert.x对Redis的封装
 */
public class RedisClientUtil {
	
	/**
	 * 	每一个前面都会有这样的碎碎念！2018/12/9这个下午西安的雾霾散了，太阳出来了，我来公司改点东西，很安静的样子！很喜欢
	 * 	我想继续看Redis的东西！但是又在这乱扯，浪费生命的感觉真美好，不是吗！！！
	 * 	南山有雨！一十九已……去年的这个时候！我是想起了南宫还是师傅，小师傅真的很有趣、很有趣    我好坑的咯！
	 * 	最近的学习效率好低的~我想想会有什么好点的办法没有！太阳出来了、雾霾少了许多，想起师傅的口头禅 忧伤！
	 */
	
	private static final RedisClient redis=new RedisClientImpl(BaseConfig.vertx, BaseConfig.Default_Redis_Config);
	


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
