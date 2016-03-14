<?php
class DreamController{
	var $json_output;
	var $model;
	
	function __construct($dao){
		$this->model=new DreamModel($dao);
		$json_output=array();
	}
	
	function publishDream($dream){
		if($this->model->postDream($dream))
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
	
	function listDream(){
		$this->model->listDreams();
		$dreams=array();
		while($dream=$this->model->getDream()){
			array_push($dreams,$dream);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$dreams;
	}
	
	function deleteDream($name){
		//$this->model->deleteDream($id);
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