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
		$sql="INSERT INTO `dream_users`(`user_login`, `user_pass`, `user_nickname`, `user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `user_display_name`) VALUES ('".$user['name']."','".$user['pass']."','".$user['nickname']."','".$user['email']."','".$user['url']."','".$user['registeredate']."','".$user['activationkey']."','".$user['status']."','".$user['displayname']."')";
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
	
	function loginUser($name,$pass){
//		$sql="SELECT * FROM dream_users where user_login='".$name."' and user_pass='".$pass."'";
//		$this-dao->fetch($sql);
//		if($user=$this->dao->fetch($sql)){
//			return $user;
//		}
//		else{
//			return false;
//		}
	}
} 	
?>