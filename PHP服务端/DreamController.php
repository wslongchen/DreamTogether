<?php
header("Content-type:text/html;charset=utf-8");
class DreamController{
	
	var $model;
	
	function __construct($dao){
		$this->model=new DreamModel($dao);
	}
	
	function publishDream($dream){
		$json_out=$this->model->postDream($dream);
		
		echo json_encode($json_out);
		
		
		
	}
	
	function listDream(){
		$this->model->listDreams();
		$dreams=array();
		while($dream=$this->model->getDream()){
			array_push($dreams,$dream);
		}
		$json_output["ret"]=0;
		$json_output["post"]=$dreams;
		echo json_encode($json_output);
	}
	
	function listDreamWithAuthor(){
		$json_out=$this->model->getDreamWithAuthor();
		echo json_encode($json_out);
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