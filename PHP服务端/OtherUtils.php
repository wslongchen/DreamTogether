<?php
class FileUitls {
	
	$base_path = "./uploads/";
	$type=array("jpg","gif","bmp","jpeg","png");//设置允许上传文件的类型
	
	//获取文件后缀名函数 
function fileext($filename) 
{ 
return substr(strrchr($filename, '.'), 1); 
} 
//生成随机文件名函数 
function random($length) 
{ 
$hash = 'CR-'; 
$chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz'; 
$max = strlen($chars) - 1; 
mt_srand((double)microtime() * 1000000); 
for($i = 0; $i < $length; $i++) 
{ 
$hash .= $chars[mt_rand(0, $max)]; 
} 
return $hash; 
} 

 
	
	function uploadfiles() {
//		//接收文件目录
//		$target_path = $base_path . basename($_FILES['uploadfile']['name']);
//		if (move_uploaded_file($_FILES['uploadfile']['tmp_name'], $target_path)) {
//			$array = array("code" => "1", "message" => $_FILES['uploadfile']['name']);
//			echo json_encode($array);
//		} else {
//			$array = array("code" => "0", "message" => "There was an error uploading the file, please try again!" . $_FILES['uploadfile']['error']);
//			echo json_encode($array);
//		}
//判断文件类型 
if(!in_array(strtolower(fileext($_FILES['uploadfile']['name'])),$type)) 
{ 
$text=implode(",",$type); 
echo "您只能上传以下类型文件: ".$text; 
} 
//生成目标文件的文件名 
else{ 
$filename=explode(".".$_FILES['uploadfile']['name']); 
do 
{ 
$filename[0]=random(10); //设置随机数长度 
$name=implode(".",$filename); 
//$name1=$name.".Mcncc"; 
$uploadfile=$base_path.$name; 
} 

while(file_exists($uploadfile)); 

if (move_uploaded_file($_FILES['uploadfile']['tmp_name'],$uploadfile)) 
{ 
if(is_uploaded_file($_FILES['uploadfile']['tmp_name'])) 
{ 

echo "上传失败!"; 
} 
else 
{//输出图片预览 
echo "您的文件已经上传完毕 上传图片预览:".$uploadfile; 
} 
} 

}
	}

}
?>