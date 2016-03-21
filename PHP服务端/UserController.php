<?php 
class UserController{
	
	var $model;
	
	function __construct($dao){
		$this->model=new UserModel($dao);
	}
	
	function listUsers(){
		$this->model->listUser();
		$users=array();
		while($user=$this->model->getUser()){
			array_push($users,$user);
		}
		$json_output['ret']=0;
		$json_output['post']=$users;
		echo json_encode($json_output);
	}
	
	function postUser($user){
		$json_output=$this->model->postUser($user);
		echo json_encode($json_output);
	}
	
	function login($name,$pass){
		if($user=$this->model->loginUser($name,$pass)){
			$json_out['ret']=0;
			$json_out["user"]=$user;
		}
		else{
			$json_output['ret']=1;
			$json_output['post']="post failed!";	
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