<?php

//header("Location: http://".$_SERVER['HTTP_HOST']."/".$_SERVER['PHP_SELF']);


require_once(dirname(__FILE__)."/header.php"); 

if(isset($_SESSION['username'])){
	echo "Successfuly  Logged In ....";
    exit;
}
if (isset($_POST['username']) && isset($_POST['password'])) {
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf(" select * from users where email='%s' and password=md5('%s')",mysql_escape_string($_POST['username']),mysql_escape_string($_POST['password']));
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	if($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		$_SESSION['username'] =$row['email'];
		$_SESSION['level'] =$row['level'];
		$_SESSION['email'] =$row['email'];
		$_SESSION['realname'] =$row['realname'];
	}
	if( is_resource($conn)) {mysql_close($conn);}
	
   if (isset($_SESSION['username'])) {
     header("Location: ../index.php");
   } else {
     $_SESSION['loginError'] = 'Sorry, Wrong Username or Password';
     header("Location: ../index.php?section=login");
   }
   exit;
}


?>


<!--  table width="100%" height="100%" -->
<table width="100%" >
<tr rowspan=2><td colspan=3></td></tr>
<tr><td></td>
<td>
<form method="post" name="frmLogin" id="frmLogin"  action="php/login.php">
<table width="400" border="0" align="center" cellpadding="2" cellspacing="2">
<tr>
<td width="150" colspan="3" align="center"><font color="#495088" size="6"> Login Page</font></td>
</tr>
<tr><td width="150" colspan="3" align="center">&nbsp;</td></tr>
<tr><td width="150" colspan="3" align="center">
<?php
if (isset($_SESSION['loginError']) && $_SESSION['loginError'] != '') {
?>
<p align="center"><strong><font size="4" color="red"><?php echo $_SESSION['loginError'] ; ?></font></strong></p>
<?php
$_SESSION['loginError'] ="";
}
?>
</td></tr>
<tr>
<td width="150">Email </td><td>:</td>
<td><input name="username" type="text" id="username"></td>
</tr>
<tr>
<td width="150">Password </td><td>:</td>
<td><input name="password" type="password" id="password"></td>
</tr>
<tr>
<td colspan="3" align="center"><input type="submit" value="Login"></td>
</tr>
</table>
</form>
</td>
<td></td></tr>
<tr rowspan=2><td colspan=3></td></tr>
</table>

