<?php 
class UserModel{
	var $dao;
	
	function __construct($dao){
		$this->dao=$dao;
	}
	
	function listUser(){
		$sql="SELECT * FROM dream_users";
		$this->dao->fetch($sql);
	}
	
	function postUser($user){
		$sql="INSERT INTO `dream_users`(`user_login`, `user_pass`, `user_nickname`, `user_img`,`user_phone`,`user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `user_display_name`) VALUES ('".$user['name']."','".$user['pass']."','".$user['nickname']."','".$user['img']."','".$user['phone']."','".$user['email']."','".$user['url']."','".$user['registeredate']."','".$user['activationkey']."','".$user['status']."','".$user['displayname']."')";		
if(!empty($user) && $this->dao->query($sql)){
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
	
	function updateUser(){
		//$sql="UPDATE `dreamdb`.`dream_users` SET ";
	}
	
	function deleteUser($id){
		$sql="DELETE FROM `dreamdb`.`dream_users` WHERE `dream_users`.`ID`='".$id."'";
		if($this->dao->fetch($sql))
		{
			return true;
		}
		else{
			return false;
		}
	}
	
	function getUser() {
		if ($oneuser = $this -> dao -> getRow()) {
			return $oneuser;
		} else {
			return false;
		}
	}
	
	function getUserMeta($id){
		$sql="SELECT * FROM dream_users_meta where user_id='".$id."'";
		$this->dao->query($sql);
		if($meta=$this->dao->getResult()){
			return $meta;
		}
		else{
			return false;
		}
		
	}
	
	function getUserInfo($id){
		$sql="SELECT * FROM dream_users where ID='".$id."'";
		$this->dao->fetch($sql);
		if($user=$this->dao->getRow()){
			return $user;
		}
		else{
			return $id;
		}
	}
	
	function getUserInfoByID($id){
		$sql="SELECT * FROM dream_users where ID='".$id."'";
		$this->dao->fetch($sql);
		$users=array();
		if($user=$this->dao->getRow()){
			$json_out["ret"]=0;
			array_push($users,$user);
			$json_out["post"]=$users;
			return $json_out;
		}
		else{
			$json_out["ret"]=1;
			$json_out["post"]="request failed!";
			return $json_out;
		}
	}
	
	function loginUser($name,$pass){
		$sql="SELECT * FROM dream_users where user_login='".$name."' and user_pass='".$pass."'";
			$this->dao->fetch($sql);
			if($user=$this -> dao -> getRow()){
				$json_out["ret"]=0;
				$users=array();
				$metas=array();
				array_push($users,$user);
				$json_out["post"]=$users;
				while($meta=$this->getUserMeta()){
					array_push($metas,$meta);
				}
				$json_out["meta"]=$metas;
				return $json_out;
			}else{
				$json_out["ret"]=1;
				$json_out["post"]="login failed!";
				return $json_out;
			}
	}
	
	function updateUserImg($file){
		$sql="update dream_users set user_img='".$file."'";
		if($this->dao->query($sql)){
			$json_out["ret"]=0;
			$json_out["post"]="post successed";
			return $json_out;
		}else{
			$json_out["ret"]=1;
			$json_out["post"]="post failed!";
			return $json_out;
		}
	}
	
	function insertUserMeta($meta){
		$sql="INSERT INTO `dream_users_meta` VALUES ('"+$meta["key"]+"','"+$meta["value"]+"')";
		if(!empty($meta) && $this->dao->query($sql)){
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
	
	
}
?>