package org.tzdr.util;
/**
* @author 狐妖小红娘
* @version 创建时间：2018年12月1日 下午11:48:09
* 类说明	练习Vert.x的数据库支持
*/

import java.util.List;
import org.tzdr.base.BaseConfig;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

public class JdbcManager {
	private JDBCClient jdbc;
	private static Logger logger=LoggerFactory.getLogger(JdbcManager.class);
	/**
	 * 私有构造方法！创建默认jdbc
	 * @param jdbcClient
	 */
	public JdbcManager(JDBCClient jdbcClient) {
		if (jdbcClient==null) {
			this.jdbc=JDBCClient.createShared(BaseConfig.vertx, BaseConfig.DEFAULT_JDBC_CONFIG);
			if (jdbc!=null) {
				logger.info("初始化默认JDBC成功");
			}
			return;
		}
		this.jdbc=jdbcClient;
	}
	
	/**
	 * 执行一个无参数的select语句 返回结果集
	 * @param sql
	 * @return	Future<List<JsonObject>>
	 */
	public Future<List<JsonObject>> query(String sql) {
		Future<List<JsonObject>> future=Future.future();
		jdbc.getConnection(conn->{
			if (conn.succeeded()) {
				conn.result().query(sql, res->{
					if (res.succeeded()) {
						future.complete(res.result().getRows());
					}else {
						future.fail(res.cause());
					}
					conn.result().close();
				});
			}else {
				future.fail(conn.cause());
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
		jdbc.getConnection(conn->{
			if (conn.succeeded()) {
				conn.result().queryWithParams(sql, params, res->{
					if (res.succeeded()) {
						future.complete(res.result().getRows());
					}else {
						future.fail(res.cause());
					}
					conn.result().close();
				});
			}else {
				future.fail(conn.cause());
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
		jdbc.getConnection(conn->{
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
	
	public Future<List<Integer>> batch(List<String> sqlStatements,SQLConnection conn) {
		Future<List<Integer>> future=Future.future();
		conn.batch(sqlStatements,res->{
			if (res.succeeded()) {
				future.complete(res.result());
			}else {
				future.fail(res.cause());
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
		jdbc.getConnection(conn->{
			if (conn.succeeded()) {
				conn.result().batchWithParams(sqlStatement,params,res->{
					if (res.succeeded()) {
						future.complete(res.result());
					}else {
						future.fail(res.cause());
					}
					conn.result().close();
				});
			}else {
				future.fail(conn.cause());
			}
		});
		return future;
	}
	
	public Future<List<Integer>> batchWithParams(String sqlStatement, List<JsonArray> params,SQLConnection conn) {
		Future<List<Integer>> future=Future.future();
		conn.batchWithParams(sqlStatement,params,res->{
			if (res.succeeded()) {
				future.complete(res.result());
			}else {
				future.fail(res.cause());
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
		jdbc.getConnection(conn->{
			if (conn.succeeded()) {
				conn.result().update(sql,res->{
					if (res.succeeded()) {
						future.complete(res.result());
						res.result();
					}else {
						future.fail(res.cause());
					}
					conn.result().close();
				});
			}else {
				future.fail(conn.cause());
			}
		});
		return future;
	}
	
	public Future<UpdateResult> update(String sql,SQLConnection conn) {
		Future<UpdateResult> future=Future.future();
		conn.update(sql,res->{
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
		jdbc.getConnection(conn->{
			if (conn.succeeded()) {
				conn.result().updateWithParams(sql,params,res->{
					if (res.succeeded()) {
						future.complete(res.result());
					}else {
						future.fail(res.cause());
					}
					conn.result().close();
				});
			}else {
				future.fail(conn.cause());
			}
		});
		return future;
	}
	
	public Future<UpdateResult> updateWithParams(String sql,JsonArray params,SQLConnection conn) {
		Future<UpdateResult> future=Future.future();
		conn.updateWithParams(sql,params,res->{
			if (res.succeeded()) {
				future.complete(res.result());
			}else {
				future.fail(res.cause());
			}
		});
		return future;
	}
	
	/**
	 * 处理事务相关的操作开始
	 * @return	返回一个SQLConnection,记得要关闭连接！
	 */
	public Future<SQLConnection> getSQLConnection() {
		Future<SQLConnection> future=Future.future();
		 jdbc.getConnection(conn->{
			if (conn.succeeded()) {
				future.complete(conn.result());
			} else {
				future.fail(conn.cause());
			}
		});
		 return future;
	}
	
	/**
	 * 开启事务
	 * @param connection
	 * @return
	 */
	public Future<Void> setAutoCommit(SQLConnection connection) {
		Future<Void> future=Future.future();
		connection.setAutoCommit(false, start->{
			if (start.succeeded()) {
				future.complete();
			} else {
				future.fail(start.cause());
			}
		});
		 return future;
	}
	
	/**
	 * 提交事务，关闭连接
	 * @param connection
	 * @return
	 */
	public Future<Void> commit(SQLConnection connection) {
		Future<Void> future=Future.future();
		connection.commit( commit->{
			if (commit.succeeded()) {
				future.complete();
			} else {
				future.fail(commit.cause());
			}
			connection.close();
		});
		 return future;
	}
	
	/**
	 * 回滚事务,关闭连接
	 * @param connection
	 * @return
	 */
	public Future<Void> rollback(SQLConnection connection) {
		Future<Void> future=Future.future();
		connection.rollback(rollback->{
			if (rollback.succeeded()) {
				future.complete();
			} else {
				future.fail(rollback.cause());
			}
			connection.close();
		});
		 return future;
	}
	//怎么封装一些方便需要处理事务的SQL呢
	
	public static void main(String[] args) {
		JdbcManager jdbc = new JdbcManager(null);
		jdbc.getSQLConnection().setHandler(conn->{
			if (conn.succeeded()) {
				SQLConnection connection = conn.result();//获得连接
				jdbc.setAutoCommit(connection).setHandler(res->{//开启事务
					jdbc.update("delete from goods where goodsid=12", connection).setHandler(re->{
						if (re.succeeded()) {
							System.out.println("删除成功");
						}
						if (res.failed()) {
						}
					});
					jdbc.update("insert into goods values(default,'在路上','散文',5)", connection).setHandler(re->{
						if (re.succeeded()) {
							System.out.println("插入成功");
						}
						if (res.failed()) {
						}
					});
				});
			}
		});
	}
	
}
