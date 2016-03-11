<?php
/**
* 一个用来访问MySQL的类
*/
class DataAccess {
	var $db; //用于存储数据库连接
	var $query; //用于存储查询源
	//构造函数.
	/**
	* 创建一个新的DataAccess对象
	* @param $host 数据库服务器名称
	* @param $user 数据库服务器用户名
	* @param $pass 密码
	* @param $db 数据库名称
	*/
	function __construct($host,$user,$pass,$db) {
		$this->db=mysql_pconnect($host,$user,$pass); //连接数据库服务器
		mysql_select_db($db,$this->db); //选择所需数据库
		//前者是构造函数参数
		//后者是类的数据成员
		if (!$this->db)
  		{
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
		$this->query=mysql_unbuffered_query($sql,$this->db); // Perform query here
	}
	//获取一条记录
	/**
	* 以数组形式返回查询结果的一行记录，通过循环调用该函数可遍历全部记录
	* @return mixed
	*/
	function getRow () {
		if ( $row=mysql_fetch_array($this->query,MYSQL_ASSOC) )
		//MYSQL_ASSOC参数决定了数组键名用字段名表示
			return $row;
		else
			return false;
	}
}
?>