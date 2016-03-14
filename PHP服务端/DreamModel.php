<?php
//! Model类
/**
 * 它的主要部分是对应于各种数据操作的函数
 * 如：数据的显示、插入、删除等
 */
class DreamsModel {
	var $dao;
	//DataAccess类的一个实例（对象）
	//! 构造函数
	function __construct($dao) {
		$this -> dao = $dao;
	}

	function listDreams() {//获取全部梦想
		$this -> dao -> fetch("select * from dream_wordcircle");
	}

	function postDream($dream) {//插入一条新梦想
		$sql = "INSERT INTO `dreamdb`.`dream_wordcircle` VALUES('" . $dream[author] . "','" . $dream[date] . "','" . $dream[content] . "','" . $dream[title] . "','" . $dream[status] . "','" . $dream[password] . "','" . $dream[guid] . "','" . $dream[type] . "','" . $dream[commentstatus] . "','" . $dream[commentcount] . "'";
		//调试时用echo输出一下看看是否正确是一种常用的调试技巧
		if($this -> dao -> fetch($sql)){
			
		}
		else{
			return false;
		}
	}

	function deleteDream($id) {//删除一条梦想，$id是该条梦想的id
		$sql = "DELETE FROM `dreamdb`.`dream_wordcircle` WHERE `dream_wordcircle`.`ID`=" . $id;
		$this -> dao -> fetch($sql);
	}

	function updateDream($dream, $id) {//更新一条梦想，$id是该梦想的id
		$sql = "UPDATE `dreamdb`.`dream_wordcircle` SET `wordcircle_author`='" . $dream[author] . "',`wordcircle_date`='" . $dream[date] . "',`wordcircle_content`='" . $dream[content] . "',`wordcircle_titile`='" . $dream[title] . "',`wordcircle_status`='" . $dream[status] . "',`wordcircle_password`='" . $dream[password] . "',`wordcircle_guid`='" . $dream[guid] . "',`wordcircle_type`='" . $dream[type] . "',`wordcircle_comment_status`='" . $dream[commentstatus] . "',`wordcircle_comment_count`='" . $dream[commentcount] . "'";
	}
	function getDreamID(){
		//$sql="SELECT ID FROM `dreamdb`.`dream_wordcircle` WHERE wordcircle_";
	}
	function getDream() {//获取以数组形式存储的一条梦想
		//View利用此方法从查询结果中读出数据并显示
		if ($onedream = $this -> dao -> getRow()) {
			return $onedream;
		} else {
			return false;
		}
	}

}
?>