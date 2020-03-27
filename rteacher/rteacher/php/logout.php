<?php
session_start();


if (isset($_SESSION['username'])) {
	$_SESSION['username']='';
	$_SESSION['realname']='';
	unset($_SESSION['username']);
	unset($_SESSION['realname']);
	unset($_SESSION['level']);
}
header('Location: ./index.php');
//header("Location: http://".$_SERVER['HTTP_HOST']."/".$_SERVER['PHP_SELF']);

?>
