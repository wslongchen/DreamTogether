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
		$metas=array();
		while($meta=$this->model->getUserMeta()){
			array_push($metas,$meta);
		}
		$json_output["meta"]=$metas;
		echo json_encode($json_output);
	}
	
	function getUserByID($id){
		$json_out=$this->model->getUserInfoByID($id);
		echo json_encode($json_out);
	}
	
	function postUser($user){
		$json_output=$this->model->postUser($user);
		echo json_encode($json_output);
	}
	
	function login($name,$pass){
		$json_output=$this->model->loginUser($name,$pass);
		echo json_encode($json_output);
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