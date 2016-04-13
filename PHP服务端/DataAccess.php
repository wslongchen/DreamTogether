<?php
/**
 * 一个用来访问MySQL的类
 */
header("Content-type:text/html;charset=utf-8");
class DataAccess {
	var $db;
	//用于存储数据库连接
	var $query;
	//用于存储查询源
	//构造函数.
	/**
	 * 创建一个新的DataAccess对象
	 * @param $host 数据库服务器名称
	 * @param $user 数据库服务器用户名
	 * @param $pass 密码
	 * @param $db 数据库名称
	 */
	function __construct($host, $user, $pass, $db) {
		$this -> db = mysql_pconnect($host, $user, $pass);
		//连接数据库服务器
		mysql_select_db($db, $this -> db);
		mysql_query("set names utf8;");
		//选择所需数据库
		//前者是构造函数参数
		//后者是类的数据成员
		//require_once('PrepareSql.php');
		$sql = "CREATE TABLE IF NOT EXISTS dream_posts (
				ID INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
				post_author INT(11) NOT NULL,
				post_date DATETIME NOT NULL,
				post_content LONGTEXT NOT NULL,
				post_titile text NOT NULL,
				post_imgs varchar(255),
				post_status VARCHAR(20) NOT NULL,
				post_password VARCHAR(20) NOT NULL,
				post_guid VARCHAR(255),
				post_type INT(11) NOT NULL,
				post_comment_status VARCHAR(20) NOT NULL,
				post_comment_count INT(11)
				);
				CREATE TABLE IF NOT EXISTS dream_posts_meta(
				dpmeta_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
				post_id INT(11),
				meta_key varchar(255),
				meta_value longtext);
				CREATE TABLE IF NOT EXISTS dream_users (
				ID INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
				user_login varchar(60) NOT NULL,
				user_pass varchar(64) NOT NULL,
				user_nickname varchar(50) NOT NULL,
				user_img varchar(100),
				user_phone varchar(100) NOT NULL,
				user_email varchar(100) NOT NULL,
				user_url VARCHAR(100),
				user_registered datetime NOT NULL,
				user_activation_key varchar(60),
				user_status INT(11) NOT NULL,
				user_display_name varchar(250)
				)
				CREATE TABLE IF NOT EXISTS dream_users_meta(
				dumeta_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
				user_id INT(6) NOT NULL,
				meta_key varchar(255) NOT NULL,
				meta_value longtext NOT NULL
				)
				CREATE TABLE IF NOT EXISTS dream_feedback(
				id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
			    time varchar(200) NOT NULL,
			    content varchar(500) NOT NULL,
			    name varchar(20) DEFAULT NULL,
			    contact varchar(20) DEFAULT NULL
				)
				";
				
			mysql_query($sql,$this->db);
		//$sql = new ReadSql($host, $user, $pass, $db);
		//$rst = $sql -> Import("./log_db.sql");
		//if ($rst) {
		//	echo "Success！";
		//}
		if (!$this -> db) {
			die('Could not connect: ' . mysql_error());
		}
	}

	//执行SQL语句
	/**
	 * 执行SQL语句，获取一个查询源并存储在数据成员$query中
	 * @param $sql 被执行的SQL语句字符串
	 * @return void
	 */
	function fetch($sql) {
		$this -> query = mysql_unbuffered_query($sql, $this -> db);
		// Perform query here
	}
	
	function query($sql) {
		$result=mysql_query($sql,$this->db);
		
		$this -> queryt=$result;
	return $result;
	}
	
	function getResult() {
		if ($row = mysql_fetch_array($this -> queryt, MYSQL_ASSOC))
			//MYSQL_ASSOC参数决定了数组键名用字段名表示
			return $row;
		else
			return false;
	}
	//获取一条记录
	/**
	 * 以数组形式返回查询结果的一行记录，通过循环调用该函数可遍历全部记录
	 * @return mixed
	 */
	function getRow() {
		if ($row = mysql_fetch_array($this -> query, MYSQL_ASSOC))
			//MYSQL_ASSOC参数决定了数组键名用字段名表示
			return $row;
		else
			return false;
	}

	function initTables() {
		$sql = "CREATE TABLE IF NOT EXISTS dream_posts (
				ID INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
				post_author INT(11) NOT NULL,
				post_date DATETIME NOT NULL,
				post_content LONGTEXT NOT NULL,
				post_titile text NOT NULL,
				post_status VARCHAR(20) NOT NULL,
				post_password VARCHAR(20) NOT NULL,
				post_guid VARCHAR(255),
				post_type INT(11) NOT NULL,
				post_comment_status VARCHAR(20) NOT NULL,
				post_comment_count INT(11)
				);
				CREATE TABLE IF NOT EXISTS dream_posts_meta(
				dpmeta_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
				post_id INT(11),
				meta_key varchar(255),
				meta_value longtext);";
		IF ($this -> db != null) {
			mysql_query($sql,$this->db);
		}
	}

}
?>