<?php
require_once ('DataAccess.php');
require_once ('UserModel.php');
require_once ('DreamModel.php');
require_once ('UserController.php');
require_once ('DreamController.php');
require_once ('CommentModel.php');
require_once ('CommentController.php');
$dao = new DataAccess('localhost', 'root', 'longchen', 'dreamdb');
//根据$_GET["action"]取值的不同调用不同的控制器子类
$action = $_GET["action"];
switch ($action) {
	case "post" :
		if ($_GET["type"] === "user") {
			$controller = new UserController($dao);
			//var_dump($_POST);
			$controller -> postUser($_POST);
		}
		if ($_GET["type"] === "dream") {
			$controller = new DreamController($dao);
			$controller -> publishDream($_POST);
		}
		if($_GET["type"]==="photo"){
			$up=new FileUitls();
			$up->uploadfiles();
		}
		break;
	case "list" :
		if ($_GET["type"] === "user") {
			$controller = new UserController($dao);
			$controller -> listUsers();
		}
		if ($_GET["type"] === "dream") {
			$controller = new DreamController($dao);
			$controller -> listDreamWithAuthor($_GET['page'],10);
		}
		if ($_GET["type"] === "loginUser") {
			$controller = new UserController($dao);
			$controller -> login($_GET['name'], $_GET['password']);
		}
		if($_GET["type"] === "getUserByID") {
			$controller = new UserController($dao);
			$controller -> getUserByID($_GET['id']);
		}
		if($_GET["type"] === "getDreamByID") {
					$controller = new DreamController($dao);
					$controller -> listDreamByID($_GET['id']);
				}
		if ($_GET["type"] === "getDreamByAuthor") {
			$controller = new DreamController($dao);
			$controller -> listDreamByAuthor($_GET['id']);
		}
		if ($_GET["type"] === "getRandomDream") {
			$controller = new DreamController($dao);
			$controller -> listRandomDreams(1,5);
		}
		//$controller->output();
		break;
	case "comment" :
		if($_GET["type"]==="getComment"){
			$controller = new CommentController($dao);
			$controller ->listCommentWithAuthor($_GET["id"]);
		}
		if($_GET["type"]==="postComment"){
			$controller = new CommentController($dao);
			$controller ->publishComment($_POST);
		}
		//$controller=new deleteController($dao,$_GET["id"]);
		break;
	case "delete":
		if($_GET["type"]==="dream"){
			$controller = new DreamController($dao);
			$controller ->deleteDream($_GET["id"]);
		}
		break;
	default :
		//$controller=new listController($dao);
		break;
	//默认为显示留言
}

?>