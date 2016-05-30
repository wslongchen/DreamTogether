<?php
class UserModel {
	var $dao;
	var $smtpserver = "smtp.qq.com";
	var $smtpserverport =25;
	var $smtpusermail = "1049058427@qq.com";
	var $smtp;
	var $smtpuser = "1049058427@qq.com";
	var $smtppass = "longchen520";

	function __construct($dao) {
		$this -> dao = $dao;
		$this->smtp = new smtp($this->smtpserver,$this->smtpserverport,true,$this->smtpuser,$this->smtppass);
		$this->smtp->debug = false;
	}

	function listUser() {
		$sql = "SELECT * FROM dream_users";
		$this -> dao -> fetch($sql);
	}

	function postUser($user) {
		$sql = "INSERT INTO `dream_users`(`user_login`, `user_pass`, `user_nickname`, `user_img`,`user_phone`,`user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `user_display_name`) VALUES ('" . $user['name'] . "','" . $user['pass'] . "','" . $user['nickname'] . "','" . $user['img'] . "','" . $user['phone'] . "','" . $user['email'] . "','" . $user['url'] . "','" . $user['registeredate'] . "','" . $user['activationkey'] . "','" . $user['status'] . "','" . $user['displayname'] . "')";
		
		if (!empty($user) && $this -> dao -> query($sql)) {
			$id=mysql_insert_id();
			$key=$this->random(5);
			$key=md5($key);
			$this->updateActivationKey($id, $key);
			$content="http://dream.mrpann.com/index.php?action=active&key=".$key."&type=user&id=".$id;
			$this->sendmail($user['email'] , $content);
			$json_out["ret"] = 0;
			$json_out["post"] = "post successed";
			return $json_out;
		} else {
			$json_out["ret"] = 1;
			$json_out["post"] = "post failed!";
			return $json_out;
		}
	}
	
	function random($length) {
	$hash = '';
	$chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz';
	$max = strlen($chars) - 1;
	mt_srand((double)microtime() * 1000000);
	for ($i = 0; $i < $length; $i++) {
		$hash .= $chars[mt_rand(0, $max)];
	}
	return $hash;
}
	
	function activationUser($id,$key){
		$sql="select * from `dream_users` where `ID`='".$id."' and `user_activation_key`='".$key."'";
		$this->dao->fetch($sql);
		if ($this ->dao -> getRow()) {
			$sql="UPDATE `dream_users` SET `user_status`='1' where `ID`='".$id."' and `user_activation_key`='".$key."'";
		if($this -> dao -> query($sql)){
			$k=$this->random(10);
			$k=md5($k);
			$this->updateActivationKey($id, $k);
			return "激活成功";
		}
		}else{
			return "激活失败，此激活链接已失效。";
		}
		
	}

	function checkActivation($id,$key){
		
	}
	
	function isHaveUser($user) {
		$sql = "SELECT * FROM `dream_users` WHERE `user_login`='" . $user['name'] . "'";
		$this -> dao -> fetch($sql);
		if ($this -> dao -> getRow()) {
			
			$json_out["ret"] = 1;
			$json_out["post"] = "账户名已存在！";
			return $json_out;
		} else {
			$sql = "SELECT * FROM `dream_users` WHERE `user_email`='" . $user['email'] . "'";
			$this -> dao -> fetch($sql);
			if ($this ->dao -> getRow()) {
				$json_out["ret"] = 1;
				$json_out["post"] = "邮箱已存在！";
				return $json_out;
			} else {
				$sql = "SELECT * FROM `dream_users` WHERE `user_phone`='" . $user['phone'] . "'";
				$this -> dao -> fetch($sql);
				if ($this -> dao -> getRow()) {
					$json_out["ret"] = 1;
					$json_out["post"] = "电话号码已存在！";
					return $json_out;
				} else {
					$json_out["ret"] = 0;
					$json_out["post"] = "验证成功！";
					return $json_out;
				}
			}
		}
	}

	function updateUserImg($file, $id) {
		$sql = "UPDATE `dream_users` SET `user_img`='" . $file . "' WHERE `ID`='" . $id . "'";
		$this -> dao -> fetch($sql);
	}

	function updateSign($sign, $id) {
		$sql = "UPDATE `dream_users` SET `user_display_name`='" . $sign . "' WHERE `ID`='" . $id . "'";
		$this -> dao -> fetch($sql);
	}
	
	function sendmail($email,$content){
		$smtpemailto = $email;
		$mailtitle = "激活邮件";
		$mailcontent = "<h1>请点击此链接激活:</h1><a>".$content."</a>";
		$mailtype = "HTML";
		$name="梦想自留地";
		$this->smtp->sendmail($smtpemailto, $this->smtpusermail, $mailtitle, $mailcontent, $mailtype,$name);
	}

	function updateUser() {
		//$sql="UPDATE `dreamdb`.`dream_users` SET ";
	}

	function deleteUser($id) {
		$sql = "DELETE FROM `dreamdb`.`dream_users` WHERE `dream_users`.`ID`='" . $id . "'";
		if ($this -> dao -> fetch($sql)) {
			return true;
		} else {
			return false;
		}
	}
	
	function updateActivationKey($id,$key){
		$sql="UPDATE `dream_users` SET `user_activation_key`='".$key."' where `ID`='".$id."'";
		$this -> dao -> fetch($sql);
	}

	function getUser() {
		if ($oneuser = $this -> dao -> getRow()) {
			return $oneuser;
		} else {
			return false;
		}
	}

	function getUserMeta() {
		$sql = "SELECT * FROM dream_users_meta";
		$this -> dao -> query($sql);
		if ($meta = $this -> dao -> getResult()) {
			return $meta;
		} else {
			return false;
		}

	}

	function getUserMetaByUser($id) {
		$sql = "SELECT * FROM dream_users_meta where user_id='" . $id . "'";
		$this -> dao -> query($sql);
		$metas = array();
		while ($meta = $this -> dao -> getResult()) {
			array_push($metas, $meta);
		}
		return $metas;
	}

	function getUserInfo($id) {
		$sql = "SELECT * FROM dream_users where ID='" . $id . "'";
		$this -> dao -> fetch($sql);
		if ($user = $this -> dao -> getRow()) {
			return $user;
		} else {
			return $id;
		}
	}

	function getUserInfoByID($id) {
		$sql = "SELECT * FROM dream_users where ID='" . $id . "'";
		$this -> dao -> fetch($sql);
		$users = array();
		if ($user = $this -> dao -> getRow()) {
			$json_out["ret"] = 0;
			array_push($users, $user);
			$metas = $this -> getUserMetaByUser($user['ID']);
			$json_out["post"] = $users;
			$json_out["meta"] = $metas;
			return $json_out;
		} else {
			$json_out["ret"] = 1;
			$json_out["post"] = "request failed!";
			return $json_out;
		}
	}

	function loginUser($name, $pass) {
		$sql = "SELECT * FROM dream_users where user_login='" . $name . "' and user_pass='" . $pass . "'";
		$this -> dao -> fetch($sql);
		if ($user = $this -> dao -> getRow()) {
			$json_out["ret"] = 0;
			$users = array();
			$metas = $this -> getUserMetaByUser($user['ID']);
			array_push($users, $user);
			$json_out["post"] = $users;

			$json_out["meta"] = $metas;
			return $json_out;
		} else {
			$json_out["ret"] = 1;
			$json_out["post"] = "login failed!";
			return $json_out;
		}
	}

	function insertUserMeta($meta) {
		$sql = "INSERT INTO `dream_users_meta` VALUES ('" + $meta["key"] + "','" + $meta["value"] + "')";
		if (!empty($meta) && $this -> dao -> query($sql)) {
			$json_out["ret"] = 0;
			$json_out["post"] = "post successed";
			return $json_out;
		} else {
			$json_out["ret"] = 1;
			$json_out["post"] = "post failed!";
			return $json_out;
		}
	}
	
	function signValidate($timestamp,$interfacename,$signkey){
		$sign="sfd,.*-app".$timestamp.$interfacename;
		$sign=md5($sign);
		if($signkey===$sign){
			return true;
		}else{
			return false;
		}
	}

}
?>