 <?php

require_once(dirname(__FILE__)."/header.php"); 
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select *   from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date>=CURRENT_DATE()  and course_date<=CURRENT_DATE() order by schedule.session_id ");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
 echo $row['session_id']."#".$row['teacher']."#".$row['attendee']."\n";
}
 if( is_resource($conn)) {mysql_close($conn);}
?>