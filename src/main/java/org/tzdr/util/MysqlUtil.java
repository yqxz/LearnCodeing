package org.tzdr.util;

import java.util.List;
import org.tzdr.base.BaseConfig;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.UpdateResult;

/**
 * @author 狐妖小红娘
 * @version 2018年12月8日 上午9:14:42
 * mySQLCLient的实现练习	用于一些和MySQL数据库有关的操作实现		
 * 但是貌似不需要啊！有JDBCClient的类操作就已经足够了 
 * 所以PostgreSQL的练习就先算了！
 */
public class MysqlUtil {
	
	private static final AsyncSQLClient mySQLClient=MySQLClient.createShared(BaseConfig.vertx,BaseConfig.mySQLClientConfig);
	
	/**
	 * 执行一个无参数的select语句 返回结果集
	 * @param sql
	 * @return	Future<List<JsonObject>>
	 */
	public Future<List<JsonObject>> query(String sql) {
		Future<List<JsonObject>> future=Future.future();
				mySQLClient.query(sql, res->{
					if (res.succeeded()) {
						future.complete(res.result().getRows());
					}else {
						future.fail(res.cause());
					}
				});
		return future;
	}
	
	/**
	 * 执行一个有参数的select语句,返回结果集
	 * @param sql
	 * @param params
	 * @return	Future<List<JsonObject>>
	 */
	public Future<List<JsonObject>> queryWithParams(String sql,JsonArray params) {
		Future<List<JsonObject>> future=Future.future();
				mySQLClient.queryWithParams(sql, params, res->{
					if (res.succeeded()) {
						future.complete(res.result().getRows());
					}else {
						future.fail(res.cause());
					}
				});
		return future;
	}

	/**
	 * 执行多条非select语句
	 * @param sqlStatements
	 * @return	返回每条语句受影响的行数
	 */
	public Future<List<Integer>> batch(List<String> sqlStatements) {
		Future<List<Integer>> future=Future.future();
		mySQLClient.getConnection(conn -> {
			if (conn.succeeded()) {
				conn.result().batch(sqlStatements,res->{
					if (res.succeeded()) {
						future.complete(res.result());
					}else {
						future.fail(res.cause());
					}
				});
				conn.result().close();
			}else {
				future.fail(conn.cause());
			}
		});
		return future;
	}

	/**
	 * 执行多条非select  SQL语句,它们来自一个预编译的SQL语句，但是可以有不同参数
	 * @param sqlStatement	预编译的非select语句
	 * @param params		一个List<JsonArray>类型的参数
	 * @return				返回每条语句受影响的行数
	 */
	public Future<List<Integer>> batchWithParams(String sqlStatement, List<JsonArray> params) {
		Future<List<Integer>> future=Future.future();
		mySQLClient.getConnection(conn -> {
			if (conn.succeeded()) {
				conn.result().batchWithParams(sqlStatement,params,res -> {
					if (res.succeeded()) {
						future.complete(res.result());
					}else {
						future.fail(res.cause());
					}
				});
				conn.result().close();
			}else {
				future.fail(conn.cause());
			}
		});
		return future;
	}

	/**
	 * 执行一条非select SQL语句
	 * @param sql
	 * @return
	 */
	public Future<UpdateResult> update(String sql) {
		Future<UpdateResult> future=Future.future();
				mySQLClient.update(sql,res->{
					if (res.succeeded()) {
						future.complete(res.result());
						res.result();
					}else {
						future.fail(res.cause());
					}
				});
		return future;
	}

	/**
	 * 执行一条预编译的非select SQL语句
	 * @param sql
	 * @param params
	 * @return
	 */
	public Future<UpdateResult> updateWithParams(String sql,JsonArray params) {
		Future<UpdateResult> future=Future.future();
				mySQLClient.updateWithParams(sql,params,res->{
					if (res.succeeded()) {
						future.complete(res.result());
					}else {
						future.fail(res.cause());
					}
				});
		return future;
	}

}
