 <?php
 require_once(dirname(__FILE__)."/header.php");
 
 $validCredentials=false;
 if (isset($_GET['username']) && isset($_GET['password'])) {
 	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
 	mysql_select_db($dbname);
 	$query = sprintf(" select * from users where email='%s' and password=md5('%s')",mysql_escape_string($_GET['username']),mysql_escape_string($_GET['password']));
 	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
 	if($row = mysql_fetch_array($result, MYSQL_ASSOC)){
 		$validCredentials=true;
 	}
 }
 
 if( $validCredentials ==true){
 	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
 	mysql_select_db($dbname);
 	$query = sprintf(" select *    from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id left join server on server.name=schedule.server  where course_date>=CURRENT_DATE()  and course_date<=CURRENT_DATE() group by schedule.session_id order by schedule.course_start_hour  ");
 	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
 	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
 		if($row['teacher']==$_GET['username'] ||$row['attendee']==$_GET['username']) echo    $row['course_start_hour']."#".$row['session_id']."#".$row['teacher']."#".$row['attendee']."#".$row['ip']."\n";
 	}
 	if( is_resource($conn)) {mysql_close($conn);}
}
?>