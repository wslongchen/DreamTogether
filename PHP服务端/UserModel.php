<?php 
class DreamModel{
	var $dao;
	
	function __construct($dao){
		$this->dao=$dao;
	}
	
	function listUser(){
		$sql="SELECT * FROM dream_users";
		$this->dao->fetch(sql);
	}
	
	function postUser($user){
		$sql="INSERT INTO `dreamdb`.`dream_users` VALUES ('".$user[name]."','".$user[pass]."','".$user[nickname]."','".$user[email]."','".$user[url]."','".$user[registeredate]."','".$user[activationkey]."','".$user[status]."','".$user[displayname]."')";
		if($this->dao->fetch(sql))
		{
			return true;
		}
		else{
			return false;
		}
	}
	
	function updateUser(){
		//$sql="UPDATE `dreamdb`.`dream_users` SET ";
	}
	
	function deleteUser($id){
		$sql="DELETE FROM `dreamdb`.`dream_users` WHERE `dream_users`.`ID`='".$id."'";
		if($this->dao->fetch(sql))
		{
			return true;
		}
		else{
			return false;
		}
	}
	
	function loginUser($name,$pass){
		$sql="SELECT * FROM dream_users where user_login='".$name."' and user_pass='".$pass."'";
		$this-dao->fetch(sql);
		if($user=$this->dao->fetch(sql)){
			return $user;
		}
		else{
			return false;
		}
	}
} 	
?>