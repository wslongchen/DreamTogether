<?php
require_once ('DataAccess.php');
require_once ('UserModel.php');
require_once ('DreamModel.php');
require_once ('UserController.php');
require_once ('DreamController.php');
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
			$controller -> listDreamWithAuthor();
		}
		if ($_GET["type"] === "loginUser") {
			$controller = new DreamController($dao);
			$controller -> login($_GET['name'], $_GET['password']);
		}
		if ($_GET["type"] === "getDreamByAuthor") {
			$controller = new DreamController($dao);
			$controller -> listDreamByAnthor($_GET['id']);
		}
		//$controller->output();
		break;
	case "delete" :
		//$controller=new deleteController($dao,$_GET["id"]);
		break;
	default :
		//$controller=new listController($dao);
		break;
	//默认为显示留言
}

?>