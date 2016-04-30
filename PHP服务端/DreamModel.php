<?php
//! Model类
/**
 * 它的主要部分是对应于各种数据操作的函数
 * 如：数据的显示、插入、删除等
 */
class DreamModel {
	var $dao;
	//DataAccess类的一个实例（对象）
	//! 构造函数
	function __construct($dao) {
		$this -> dao = $dao;
	}

	function listDreams() {//获取全部梦想
		$this -> dao -> fetch("select * from dream_posts where post_type='0' order by post_date DESC,ID DESC");
	}

	function postDream($dream) {//插入一条新梦想
		$sql = "INSERT INTO `dream_posts` (`post_author`, `post_date`, `post_content`, `post_titile`, `post_status`, `post_password`, `post_guid`, `post_type`, `post_comment_status`, `post_comment_count`) VALUES('" . $dream[author] . "','" . $dream[date] . "','" . $dream[content] . "','" . $dream[title] . "','" . $dream[status] . "','" . $dream[password] . "','" . $dream[guid] . "','" . $dream[type] . "','" . $dream[commentstatus] . "','" . $dream[commentcount] . "')";
		//调试时用echo输出一下看看是否正确是一种常用的调试技巧
		if(!empty($dream) && $this -> dao -> query($sql)){
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
	
	function postMeta($meta){
		$sql="INSERT INTO `dreamdb`.`dream_posts_meta` (`dpmeta_id`, `post_id`, `meta_key`, `meta_value`) VALUES (NULL, '".$meta[post_id]."', '".$meta[key]."', '".$meta[value]."');";
		if(!empty($dream) && $this -> dao -> query($sql)){
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
	
	function updateMeta($meta){
		$sql="UPDATE`dreamdb`.`dream_posts_meta`SET`meta_value`='".$meta[value]."' WHERE`dream_posts_meta`.`post_id`='".$meta[post_id]."' and `dream_posts_meta`.`meta_key`='".$meta[key]."'";
		if(!empty($dream) && $this -> dao -> query($sql)){
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
	
	
	function postDreamImg($dream,$imgs) {//插入一条新梦想
		$sql = "INSERT INTO `dream_posts` (`post_author`, `post_date`, `post_content`, `post_titile`, `post_imgs`, `post_status`, `post_password`, `post_guid`, `post_type`, `post_comment_status`, `post_comment_count`) VALUES('" . $dream[author] . "','" . $dream[date] . "','" . $dream[content] . "','" . $dream[title] . "','".$imgs."','" . $dream[status] . "','" . $dream[password] . "','" . $dream[guid] . "','" . $dream[type] . "','" . $dream[commentstatus] . "','" . $dream[commentcount] . "')";		//调试时用echo输出一下看看是否正确是一种常用的调试技巧
		if(!empty($dream) && $this -> dao -> query($sql)){
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
	
	function getRandomDream($page,$count){
		$index=($page-1)*$count;
		$sql=" select * from table_name order by rand() limit ".$index.",".$count."";
		$this->dao->query($sql);
		$dreams=array();
		$author=array();
		$user=new UserModel($this->dao);
		while($dream=$this->dao->getResult()){
			
			$userInfo=$user->getUserInfo($dream["post_author"]);
			$dream["post_author"]=$userInfo;
			$dream["metas"]=$this->getDreamMetaByID($dream["ID"]);
			//var_dump($dream["post_author"]);
			array_push($dreams,$dream);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$dreams;
		return $json_output;
	}
	
	function getDreamWithAuthor($page,$count){
		$index=($page-1)*$count;
		$sql="select * from dream_posts where post_type='0' order by post_date DESC limit ".$index.",".$count."";
		$this->dao->query($sql);
		$dreams=array();
		$author=array();
		$user=new UserModel($this->dao);
		while($dream=$this->dao->getResult()){
			
			$userInfo=$user->getUserInfo($dream["post_author"]);
			$dream["post_author"]=$userInfo;
			$dream["metas"]=$this->getDreamMetaByID($dream["ID"]);
			//var_dump($dream["post_author"]);
			array_push($dreams,$dream);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$dreams;
		return $json_output;
	}
	
	function getDreamMetaByID($id){
		$sql="SELECT * FROM dream_posts_meta where post_id='".$id."'";
		$this->dao->fetch($sql);
		$metas=array();
		while($meta=$this->dao->getRow()){
			array_push($metas,$meta);
		}
		return $metas;
	}
	
	function getDreamByAuthor($id){
		$sql="select * from dream_posts where post_author='".$id."' and post_type='0' order by post_date DESC";
		$this -> dao -> query($sql);
		$dreams=array();
		$user=new UserModel($this->dao);
		while($dream=$this->dao->getResult()){
			
			$userInfo=$user->getUserInfo($dream["post_author"]);
			$dream["post_author"]=$userInfo;
			$dream["metas"]=$this->getDreamMetaByID($dream["ID"]);
			//var_dump($dream["post_author"]);
			array_push($dreams,$dream);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$dreams;
		return $json_output;
	}
	
	function getDreamByID($id){
		$sql="select * from dream_posts where ID='".$id."'";
		$this->dao->query($sql);
		$dreams=array();
		$author=array();
		$user=new UserModel($this->dao);
		while($dream=$this->dao->getResult()){
			
			$userInfo=$user->getUserInfo($dream["post_author"]);
			$dream["post_author"]=$userInfo;
			$dream["metas"]=$this->getDreamMetaByID($dream["ID"]);
			//var_dump($dream["post_author"]);
			array_push($dreams,$dream);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$dreams;
		return $json_output;
	}

	function deleteDream($id) {//删除一条梦想，$id是该条梦想的id
		$sql = "DELETE FROM `dreamdb`.`dream_posts` WHERE `dream_post`.`ID`=" . $id;
		$this -> dao -> fetch($sql);
	}

	function updateDream($dream, $id) {//更新一条梦想，$id是该梦想的id
		$sql = "UPDATE `dreamdb`.`dream_posts` SET `post_author`='" . $dream[author] . "',`post_date`='" . $dream[date] . "',`post_content`='" . $dream[content] . "',`post_titile`='" . $dream[title] . "',`post_status`='" . $dream[status] . "',`post_password`='" . $dream[password] . "',`post_guid`='" . $dream[guid] . "',`post_type`='" . $dream[type] . "',`post_comment_status`='" . $dream[commentstatus] . "',`post_comment_count`='" . $dream[commentcount] . "')";
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