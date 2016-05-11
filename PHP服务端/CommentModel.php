<?php
//! Model类
/**
 * 它的主要部分是对应于各种数据操作的函数
 * 如：数据的显示、插入、删除等
 */
class CommentModel {
	var $dao;
	//DataAccess类的一个实例（对象）
	//! 构造函数
	function __construct($dao) {
		$this -> dao = $dao;
	}

	function postComment($comment) {//插入一条新梦想
		$sql = "INSERT INTO `dream_comments` (`post_id`, `comment_user_id`, `comment_content`, `comment_time`) VALUES('" . $comment[dreamid] . "','" . $comment[userid] . "','" . $comment[content] . "','" . $comment[time] . "')";
		//调试时用echo输出一下看看是否正确是一种常用的调试技巧
		if(!empty($comment) && $this -> dao -> query($sql)){
			$json_out["ret"]=0;
			$json_out["post"]="post successed";
			return $json_out;
		}
		else{
			$json_out["ret"]=1;
			$json_out["post"]="post failed!";
			return $json_out;
		}
	}
	

	function getCommentWithAuthor($id){
		$sql="select * from dream_comments where post_id='".$id."' order by comment_time DESC";
		$this->dao->query($sql);
		$comments=array();
		
		$user=new UserModel($this->dao);
		while($comment=$this->dao->getResult()){
			$userInfo=$user->getUserInfo($comment["comment_user_id"]);
			$comment["comment_user_id"]=$userInfo;
			//var_dump($dream["post_author"]);
			array_push($comments,$comment);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$comments;
		return $json_output;
	}
	
	function getCommentByAuthor($id){
		
	}
}
?>