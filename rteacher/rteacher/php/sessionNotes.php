 <?php
require_once(dirname(__FILE__)."/header.php"); 
?>		
 <table class="hovertable" width="100%">
<tr><th>Category</th><th>Teacher/Instructor</th><th>Title</th><th>Date@Time</th><th>Duration</th><th>Attendees</th><th>Course Zip</th><th>Video Record</th></tr>
<?php

$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);


if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
     $query = sprintf(" select * , (select realname from users where email=attendees.attendee) srealname,category,(select realname from users where email=available_courses.teacher)trealname,title from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date<=CURRENT_DATE()  order by course_date desc, course_start_hour, course_start_minute ");
}else if(isset($_SESSION['level'])&&$_SESSION['level']=="1.teacher"){
	$query = sprintf(" select * , (select realname from users where email=attendees.attendee) srealname,category,(select realname from users where email=available_courses.teacher)trealname,title from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date<=CURRENT_DATE()  and  (attendees.attendee='%s' or available_courses.teacher='%s') order by course_date desc, course_start_hour, course_start_minute ",mysql_escape_string($_SESSION['email']),mysql_escape_string($_SESSION['email']));
}else if(isset($_SESSION['level'])&&$_SESSION['level']=="2.student"){
	$query = sprintf(" select * , (select realname from users where email=attendees.attendee) srealname,category,(select realname from users where email=available_courses.teacher)trealname,title from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date<=CURRENT_DATE()  and    attendees.attendee='%s'  order by course_date desc, course_start_hour, course_start_minute ",mysql_escape_string($_SESSION['email']));
}else{
	$query = sprintf(" select * , (select realname from users where email=attendees.attendee) srealname,category,(select realname from users where email=available_courses.teacher)trealname,title from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date<=CURRENT_DATE()  order by course_date desc, course_start_hour, course_start_minute LIMIT  10");
}
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
  echo "<tr><td>".$row['category']."</td><td>".$row['trealname']."</td><td>".$row['title']."</td><td>".$row['course_date']."@".$row['course_start_hour'].":".$row['course_start_minute']."</td><td>".$row['duration']."</td><td>".$row['srealname']."</td><td><a href='downloads/".$row['session_id']."/course.zip'  target='_blank'>ZIP File</a></td><td><a href='downloads/".$row['session_id']."/course.avi'  target='_blank'>Avi File</a></td></tr>";
  echo "</tr>";
}
if( is_resource($conn)) {mysql_close($conn);}
?>			         
 </table>
