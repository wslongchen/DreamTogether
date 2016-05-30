<?php
require_once ('DataAccess.php');
require_once ('UserModel.php');
require_once ('DreamModel.php');
require_once ('UserController.php');
require_once ('DreamController.php');
require_once ('CommentModel.php');
require_once ('CommentController.php');
require_once "email.class.php";
$dao = new DataAccess('localhost', 'root', 'longchen', 'dreamdb');

//根据$_GET["action"]取值的不同调用不同的控制器子类
$action = $_GET["action"];
	
switch ($action) {
	case "post" ://postuser
		if ($_GET["type"] === "user") {
			if(signValidate($_POST["timestamp"], "postuser", $_POST["signkey"])){
				$controller = new UserController($dao);
			$controller -> postUser($_POST);
			}else{
				$json_out["ret"] = 1;
				$json_out["post"] = "sign验证错误，请检查后重试。";
				echo json_encode($json_out);
			}
			
		}//publishDream
		if ($_GET["type"] === "dream") {
			
			if(signValidate($_POST["timestamp"], "publishdream", $_POST["signkey"])){
				$controller = new DreamController($dao);
				$controller -> publishDream($_POST);
			}else{
				$json_out["ret"] = 1;
				$json_out["post"] = "sign验证错误，请检查后重试。";
				echo json_encode($json_out);
			}
		}
		if($_GET["type"]==="photo"){
			$up=new FileUitls();
			$up->uploadfiles();
		}//isHaveUser
		if ($_GET["type"] === "isHaveUser") {
			
			if(signValidate($_POST["timestamp"], "ishaveuser", $_POST["signkey"])){
				$controller = new UserController($dao);
				$controller -> isHaveUser($_POST);
			}else{
				$json_out["ret"] = 1;
				$json_out["post"] = "sign验证错误，请检查后重试。";
				echo json_encode($json_out);
			}
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
		//postcomment
		if($_GET["type"]==="postComment"){
			
			if(signValidate($_POST["timestamp"], "postcomment", $_POST["signkey"])){
				$controller = new CommentController($dao);
				$controller ->publishComment($_POST);
			}else{
				$json_out["ret"] = 1;
				$json_out["post"] = "sign验证错误，请检查后重试。";
				echo json_encode($json_out);
			}
		}
		
		//$controller=new deleteController($dao,$_GET["id"]);
		break;
	case "delete":
		if($_GET["type"]==="dream"){
			$controller = new DreamController($dao);
			$controller ->deleteDream($_GET["id"]);
			
		}
		break;
	case "update"://updateUserSign
		if($_GET["type"]==="sign"){
			
			if(signValidate($_POST["timestamp"], "postcomment", $_POST["signkey"])){
				$controller = new UserController($dao);
				$controller ->updateUserSign($_POST["sign"],$_POST["id"]);
			}else{
				$json_out["ret"] = 1;
				$json_out["post"] = "sign验证错误，请检查后重试。";
				echo json_encode($json_out);
			}
		}
		break;
	case "active":
		if($_GET["type"]==="user"){
			$controller = new UserController($dao);
			$controller ->activationUser($_GET["id"],$_GET["key"]);
		}
		break;
	case "email":
	
		
		break;
	default :
		//$controller=new listController($dao);
		break;
	//默认为显示留言
}

function signValidate($timestamp,$interfacename,$signkey){
		$sign="dream,.*-app".$timestamp.$interfacename;
		$sign=md5($sign);
		if($signkey===$sign){
			return true;
		}else{
			return false;
		}
	}


?>