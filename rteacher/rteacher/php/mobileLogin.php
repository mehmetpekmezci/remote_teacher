<?php
require_once(dirname(__FILE__)."/header.php"); 
if (isset($_GET['username']) && isset($_GET['password'])) {
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf(" select * from users where email='%s' and password=md5('%s')",mysql_escape_string($_GET['username']),mysql_escape_string($_GET['password']));
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	if($row = mysql_fetch_array($result, MYSQL_ASSOC)){
       echo "TRUE";
	}else{
		echo "FALSE";
	}
}
?>
