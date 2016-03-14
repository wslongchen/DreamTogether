<?php 
class UserController{
	
	var $model;
	var $json_output;
	
	function __construct($dao){
		$this->model=new UserModel($dao);
		$json_output=array();
	}
	
	function listUsers(){
		$this->model->listUser();
		$users=array();
		while($user=$this->model->getDream()){
			array_push($users,$user);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$users;
	}
	
	function postUser($user){
		if($this->model->postUser($user))
		{
			$json_out["ret"]=0;
			$json_out["post"]="post successed";
		}
		else
		{
			$json_output["ret"]=1;
			$json_output["post"]="post failed!";	
		}
		
	}
	
	function login($name,$pass){
		if($user=$this->model->loginUser($name,$pass)){
			$json_out["ret"]=0;
			$json_out["user"]=$user;
		}
		else{
			$json_output["ret"]=1;
			$json_output["post"]="post failed!";	
		}
	}
	
	function output(){
		return toJson($json_output);
	}
	
	function toJson($array) {
    		arrayRecursive($array, 'urlencode', true);
    		$json = json_encode($array);
    		return urldecode($json);
	}
	
	
}
	?>