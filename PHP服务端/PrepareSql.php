<?php
class ReadSql {
    //数据库连接
    protected $connect = null;
    //数据库对象
    protected $db = null;
    //sql文件
    public $sqlFile = "";
    //sql语句集
    public $sqlArr = array();
    public function __construct($host, $user, $pw, $db_name) {
        $host = empty($host) ? C("DB_HOST") : $host;
        $user = empty($user) ? C("DB_USER") : $user;
        $pw = empty($pw) ? C("DB_PWD") : $pw;
        $db_name = empty($db_name) ? C("DB_NAME") : $db_name;
        //连接数据库
        $this->connect = mysql_connect($host, $user, $pw) or die("Could not connect: " . mysql_error());
        $this->db = mysql_select_db($db_name, $this->connect) or die("Yon can not select the table:" . mysql_error());
    }
    //导入sql文件
    public function Import($url) {
        $this->sqlFile = file_get_contents($url);
        if (!$this->sqlFile) {
            exit("打开文件错误");
        } else {
            $this->GetSqlArr();
            if ($this->Runsql()) {
                return true;
            }
        }
    }
    //获取sql语句数组
    public function GetSqlArr() {
        //去除注释
        $str = $this->sqlFile;
        $str = preg_replace('/--.*/i', '', $str);
        $str = preg_replace('/\/\*.*\*\/(\;)?/i', '', $str);
        //去除空格 创建数组
        $str = explode(";\n", $str);
        foreach ($str as $v) {
            $v = trim($v);
            if (empty($v)) {
                continue;
            } else {
                $this->sqlArr[] = $v;
            }
        }
    }
    //执行sql文件
    public function RunSql() {
        foreach ($this->sqlArr as $k => $v) {
            if (!mysql_query($v)) {
                exit("sql语句错误：第" . $k . "行" . mysql_error());
            }
        }
        return true;
    }
}
header("Content-type:text/html;charset=utf-8");
?>