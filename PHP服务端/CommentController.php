<?php
header("Content-type:text/html;charset=utf-8");
class CommentController{
	
	var $model;
	
	function __construct($dao){
		$this->model=new CommentModel($dao);
	}
	
	function publishComment($comment){
		$json_out=$this->model->postComment($comment);
		
		echo json_encode($json_out);
		
	}

	
	function listCommentWithAuthor($id){
		$json_out=$this->model->getCommentWithAuthor($id);
		echo json_encode($json_out);
	}
	
	function listCommentByAuthor($id){
		$json_out=$this->model->getCommentByAuthor($id);
		echo json_encode($json_out);
	}
}
 ?>