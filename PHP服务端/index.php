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
		break;
	case "list" :
		if($_GET["type"]==="user"){
			$controller = new UserController($dao);
			$controller -> listUsers();
		}
		if($_GET["type"]==="dream"){
			$controller = new DreamController($dao);
		$controller -> listDream();
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