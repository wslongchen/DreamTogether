<?php
require_once ('DataAccess.php');
require_once ('UserModel.php');
require_once ('DreamModel.php');
require_once ('UserController.php');
require_once ('DreamController.php');
$dao = new DataAccess('localhost', 'root', 'longchen', 'dreamdb');

$base_path = "./uploads/";
$type = array("jpg", "gif", "bmp", "jpeg", "png");
//设置允许上传文件的类型

$action = $_GET["action"];
$flag=true;

$files=array();
//for ($i = 0; $i < 4; $i++) {
//			$file = "file" . $i;
//			//判断文件类型
//			if (!in_array(strtolower(fileext($_FILES[$file]['name'])), $type)) {
//				$text = implode(",", $type);
//			} else{
//				$filename = explode("." . $_FILES[$file]['name']);
//				do {
//					$filename[0] = random(10);
//					//设置随机数长度
//					$name = implode(".", $filename);
//					//$name1=$name.".Mcncc";
//					$uploadfile = $base_path . $name . "." . strtolower(fileext($_FILES[$file]['name']));
//				} while(file_exists($uploadfile));
//
//				if (move_uploaded_file($_FILES[$file]['tmp_name'], $uploadfile)) {
//					if (is_uploaded_file($_FILES[$file]['tmp_name'])) {
//						echo "222";
//					} else {
//						$f="dream.mrpann.com".$uploadfile;
//						echo $f;
//						array_push($files,$f);
//					}
//				}
//			}
//		}
switch ($action) {
	case "postDreamImg" :
		for ($i = 0; $i < 9; $i++) {
			$file = "file" . $i;
			if(empty($_FILES[$file])){
				break;
			}
			//判断文件类型
			if (!in_array(strtolower(fileext($_FILES[$file]['name'])), $type)) {
				$text = implode(",", $type);
				$flag=false;echo "111";
				break;
			} else{
				$filename = explode("." . $_FILES[$file]['name']);
				do {
					$filename[0] = random(10);
					//设置随机数长度
					$name = implode(".", $filename);
					//$name1=$name.".Mcncc";
					$uploadfile = $base_path.$name ."." . strtolower(fileext($_FILES[$file]['name']));
				} while(file_exists($uploadfile));

				if (move_uploaded_file($_FILES[$file]['tmp_name'], $uploadfile)) {
					if (is_uploaded_file($_FILES[$file]['tmp_name'])) {
						$flag=false;echo "222";
					} else {
						$f="dream.mrpann.com/uploads/".$name ."." . strtolower(fileext($_FILES[$file]['name']));
						array_push($files,$f);
					}
				}
			}
		}
		if(!$flag){
			$array = array("ret" => "1", "post" => "上传图片失败！");
			echo json_encode($array);
		}else{
			
			$s2=implode(',',$files);
			$controller = new DreamController($dao);
			$controller -> publishDreamImg($_GET,$s2);
		}
		break;
case "postDream":
	$controller = new DreamController($dao);
	$controller -> publishDream($_POST);
	break;
case "updateUserImg":
	$target_path = $base_path . basename($_FILES['file0']['name']);
	$filename = explode("." . $_FILES['file0']['name']);
				do {
					$filename[0] = random(10);
					//设置随机数长度
					$name = implode(".", $filename);
					//$name1=$name.".Mcncc";
					$uploadfile = $base_path.$name ."." . strtolower(fileext($_FILES['file0']['name']));
				} while(file_exists($uploadfile));

				if (move_uploaded_file($_FILES['file0']['tmp_name'], $uploadfile)) {
					if (is_uploaded_file($_FILES['file0']['tmp_name'])) {
						$flag=false;echo "222";
					} else {
						$f="dream.mrpann.com/uploads/".$name ."." . strtolower(fileext($_FILES['file0']['name']));
						$controller=new UserController($dao);
						$controller->updateUserImg($f,$_GET["id"]);
						$array = array("ret" => "0", "post" => "修改成功！");
						echo json_encode($array);
					}
				}else{
					$array = array("ret" => "1", "post" => "修改失败！");
				echo json_encode($array);
				}
				
//		if (move_uploaded_file($_FILES['file0']['tmp_name'], $target_path)) {
//			$file=$_SERVER['SERVERNAME'].$base_path.$_FILES['file0']['name'];
//			$controller=new UserController($dao);
//			$controller->updateUserImg($file,$_GET["id"]);
//			$array = array("ret" => "0", "post" => "修改成功！");
//			echo json_encode($array);
//		} else {
//			$array = array("ret" => "1", "post" => "修改失败！");
//			echo json_encode($array);
//		}
	break;
		default:
			break;
}

//获取文件后缀名函数
function fileext($filename) {
	return substr(strrchr($filename, '.'), 1);
}

//生成随机文件名函数
function random($length) {
	$hash = 'CR-';
	$chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz';
	$max = strlen($chars) - 1;
	mt_srand((double)microtime() * 1000000);
	for ($i = 0; $i < $length; $i++) {
		$hash .= $chars[mt_rand(0, $max)];
	}
	return $hash;
}

//		//接收文件目录
//		$target_path = $base_path . basename($_FILES['uploadfile']['name']);
//		if (move_uploaded_file($_FILES['uploadfile']['tmp_name'], $target_path)) {
//			$array = array("code" => "1", "message" => $_FILES['uploadfile']['name']);
//			echo json_encode($array);
//		} else {
//			$array = array("code" => "0", "message" => "There was an error uploading the file, please try again!" . $_FILES['uploadfile']['error']);
//			echo json_encode($array);
//		}
?>