 <?php

require_once(dirname(__FILE__)."/header.php"); 


function getDateTimeSelectBox($selectedDate,$selectedTime){
	if($selectedDate==null || $selectedDate=="")$selectedDate=date("Y-m-d");
	if($selectedTime==null || $selectedTime=="")$selectedTime=date("h-i-sa");
	$dateSplitted=split("-",$selectedDate);
	
	$htmlOutput="<select name='course_start_date_year'>";
	for($i =2015;$i<2045;$i++){
		if($i==$dateSplitted[0])	$htmlOutput=$htmlOutput."<option selected >".$i."</option>";
		else 	$htmlOutput=$htmlOutput."<option>".$i."</option>";
	}
	$htmlOutput=$htmlOutput."</select>";

	$htmlOutput=$htmlOutput."<select name='course_start_date_month'>";
	for($i =1;$i<=12;$i++){
		$rep=$i;
		if($i<10)$rep="0".$i;
		if($rep==$dateSplitted[1] )	$htmlOutput=$htmlOutput."<option selected >".$rep."</option>";
		else 	$htmlOutput=$htmlOutput."<option>".$rep."</option>";
	}
	$htmlOutput=$htmlOutput."</select>";

	$htmlOutput=$htmlOutput."<select name='course_start_date_day'>";
	$dayLimit=31;
	if($dateSplitted[1] =="02"  ){
			$dayLimit=29;
	}else if ($dateSplitted[1] =="04" || $dateSplitted[1] =="06"|| $dateSplitted[1]=="09"||$dateSplitted[1]=="11"){
		$dayLimit=30;
	}
	for($i =1;$i<=$dayLimit;$i++){
		$rep=$i;
		if($i<10)$rep="0".$i;
		if($rep==$dateSplitted[2] )	$htmlOutput=$htmlOutput."<option selected >".$rep."</option>";
		else 	$htmlOutput=$htmlOutput."<option>".$rep."</option>";
	}
	$htmlOutput=$htmlOutput."</select>";

	$htmlOutput=$htmlOutput."@<select name='course_start_time'>";
	for($i =0;$i<=23;$i++){
		$rep=$i;
		if($i<10)$rep="0".$i;
		$time=$rep.":00";
		if($time==$selectedTime )	$htmlOutput=$htmlOutput."<option selected >".$time."</option>";
		else 	$htmlOutput=$htmlOutput."<option>".$time."</option>";
		$time=$rep.":30";
		if($time==$selectedTime )	$htmlOutput=$htmlOutput."<option selected >".$time."</option>";
		else 	$htmlOutput=$htmlOutput."<option>".$time."</option>";
	}
	$htmlOutput=$htmlOutput."</select>";
	
	return $htmlOutput;
}

function getStudentsSelectBox($selectedUser){
	if($selectedUser==null)$selectedUser="";
	global $dbhost, $dbuser, $dbpass,$dbname;
	$selectBox="<select name='attendee'>";
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf("SELECT * FROM  `users` where level='2.student'");
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		if($row['email']==$selectedUser){
			$selectBox=$selectBox."<option selected value='".$row['email']."'>".$row['realname']."</option>";
		}else{
			$selectBox=$selectBox."<option value='".$row['email']."'>".$row['realname']."</option>";
		}
	}
	$selectBox=$selectBox."</select>";
	if( is_resource($conn)) {mysql_close($conn);}
	return $selectBox;
}

function getServersSelectBox($selectedServer){
	if($selectedServer==null)$selectedServer="";
	global $dbhost, $dbuser, $dbpass,$dbname;
	$selectBox="<select name='server'>";
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf("SELECT * FROM  `server`");
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		if($row['name']==$selectedServer){
			$selectBox=$selectBox."<option selected value='".$row['name']."'>".$row['name']."</option>";
		}else{
			$selectBox=$selectBox."<option value='".$row['name']."'>".$row['name']."</option>";
		}
	}
	$selectBox=$selectBox."</select>";
	if( is_resource($conn)) {mysql_close($conn);}
	return $selectBox;
}
function getLocationsSelectBox($selectedLocation){
	if($selectedLocation==null)$selectedLocation="";
	global $dbhost, $dbuser, $dbpass,$dbname;
	$selectBox="<select name='location'>";
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf("SELECT * FROM  `location`");
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		if($row['name']==$selectedLocation){
			$selectBox=$selectBox."<option selected value='".$row['name']."'>".$row['name']."</option>";
		}else{
			$selectBox=$selectBox."<option value='".$row['name']."'>".$row['name']."</option>";
		}
	}
	$selectBox=$selectBox."</select>";
	if( is_resource($conn)) {mysql_close($conn);}
	return $selectBox;
}
function getCoursesSelectBox($selectedCourseId){
	global $dbhost, $dbuser, $dbpass,$dbname;
	$teacherSelectBox="<select name='course_id'>";
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf("SELECT *,(select realname from users where email=available_courses.teacher) as realname FROM  `available_courses` ");
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		if($row['course_id']==$selectedCourseId){
			$teacherSelectBox=$teacherSelectBox."<option selected value='".$row['course_id']."'>".$row['category']."   ---   ".$row['realname']."   ---   ".$row['title']."</option>";
		}else{
			$teacherSelectBox=$teacherSelectBox."<option  value='".$row['course_id']."'>".$row['category']."   ---   ".$row['realname']."   ---   ".$row['title']."</option>";
		}
	}
	$teacherSelectBox=$teacherSelectBox."</select>";
	if( is_resource($conn)) {mysql_close($conn);}
	return $teacherSelectBox;
}



if(isset($_GET['action']) && isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
	
	if( $_GET['action']=='delete'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("delete from attendees  where session_id ='%s' and attendee='%s'",mysql_escape_string($_GET['session_id']),mysql_escape_string($_GET['attendee']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			
			$noMoreAttendeeExists=FALSE;
			$query = sprintf("select *  from attendees  where session_id ='%s'",mysql_escape_string($_GET['session_id']));
			$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if(! mysql_fetch_array($result, MYSQL_ASSOC)){
                              $noMoreAttendeeExists=TRUE;
			}	
			
			if($noMoreAttendeeExists){
				$query = sprintf("delete from schedule  where session_id ='%s'",mysql_escape_string($_GET['session_id']));
				mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
                                   
            }
			if( is_resource($conn)) {mysql_close($conn);}
    }
    
	if( $_GET['action']=='add'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$session_id="NO_ID";
			$sessionDate=$_GET['course_start_date_year'].'-'.$_GET['course_start_date_month'].'-'.$_GET['course_start_date_day'];
			$sessionTimeHourMinute=split(':',$_GET['course_start_time']);
			$sessionTimeHour=$sessionTimeHourMinute[0];
			$sessionTimeMinute=$sessionTimeHourMinute[1];
			$courseId=$_GET['course_id'];
			

			$query = sprintf("select session_id  from schedule  where course_id ='%s' and course_date='%s' and course_start_hour=%s and course_start_minute=%s",
					mysql_escape_string($courseId),
					mysql_escape_string($sessionDate),mysql_escape_string($sessionTimeHour),mysql_escape_string($sessionTimeMinute));
			$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if($row=mysql_fetch_array($result, MYSQL_ASSOC)){
				$session_id=$row['session_id'];
			}else{
				       		
				
				        $query = sprintf(" select * , (select realname from users where email=attendees.attendee) srealname,category,(select realname from users where email=available_courses.teacher)trealname,title from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date='%s'  and   available_courses.teacher=(select teacher from available_courses where course_id='%s')  and ( (course_start_hour*60+course_start_minute<%s*60+%s  and course_start_hour*60+course_start_minute+duration*60>%s*60+%s  ) or   (course_start_hour*60+course_start_minute>%s*60+%s  and course_start_hour*60+course_start_minute<%s*60+%s+%s*60  ) ) order by course_date desc, course_start_hour, course_start_minute ",mysql_escape_string($sessionDate),mysql_escape_string($courseId),mysql_escape_string($sessionTimeHour),mysql_escape_string($sessionTimeMinute),mysql_escape_string($sessionTimeHour),mysql_escape_string($sessionTimeMinute),mysql_escape_string($sessionTimeHour),mysql_escape_string($sessionTimeMinute),mysql_escape_string($sessionTimeHour),mysql_escape_string($sessionTimeMinute),mysql_escape_string($_GET['duration']));
				        $result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
				        	
			            if($row=mysql_fetch_array($result, MYSQL_ASSOC)){
				                   die($row['trealname']." already has schedule wtih ".$row['srealname']." at ".$row['course_start_hour'].":".$row['course_start_minute'].", category is ".$row['category']." and the title is ".$row['title']);
			            }
	
			            $session_id=md5(microtime());
				       $query = sprintf("INSERT INTO `schedule` VALUES ('%s','%s','%s','%s','%s',%s,%s,%s,0);",
						mysql_escape_string($session_id),mysql_escape_string($courseId),mysql_escape_string($_GET['server']),mysql_escape_string($_GET['location']),
						mysql_escape_string($sessionDate),mysql_escape_string($sessionTimeHour),mysql_escape_string($sessionTimeMinute),
						mysql_escape_string($_GET['duration']));
				mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			}

  		     $query = sprintf("INSERT INTO `attendees` VALUES ('%s','%s');",
  		     		mysql_escape_string($session_id),mysql_escape_string($_GET['attendee']));
		     mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
		     
		     
			
			if( is_resource($conn)) {mysql_close($conn);}
    
	}

    
    
}


?>		
                                               <form method="get"   name='addForm'   id='addFormId'>
			                <table class="hovertable" width="100%">
			                        
			                		<tr>
			                		<th></th>
			                		<th>Category</th>
			                		             <th>Teacher/Instructor</th><th>Title</th><th>Date@Time</th><th>Duration</th><th>Attendees</th><th>Server</th><th>Location</th>
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
			                		            <th></th>
			                		            <?php  }?>
			                		</tr>
<?php
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);

$query = sprintf(" select * , (select realname from users where email=attendees.attendee) srealname,(select ip  from server where name=schedule.server) as ip,category,(select realname from users where email=available_courses.teacher)trealname,title  from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date>=CURRENT_DATE()  order by course_date, course_start_hour, course_start_minute ");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
 echo "<tr>";
 if ($row['teacher']==$_SESSION['email'] || $row['attendee']==$_SESSION['email'] ){
         echo "<td>
		                 <button  onclick='window.open(\"http://".$host."/rteacher/jnlp/rteacher.jnlp.php?server_ip=".$row['ip']."&session_id=".$row['session_id']."&attendee=".$_SESSION['email']."&teacher=".$row['teacher']."\") ;return false;'>Join</button>
		</td>";
 
 }else{
     echo "<td></td>";
}
  
 echo "<td>".$row['category']."</td><td>".$row['trealname']."</td><td>".$row['title']."</td><td>".$row['course_date']."@".$row['course_start_hour'].":".$row['course_start_minute']."</td><td>".$row['duration']."</td><td>".$row['srealname']."</td><td>".$row['server']."</td><td>".$row['location']."</td>";
  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
       echo "<td>
		                 <button  onclick=' if (confirm(\"Are you sure you want to delete\" ))load(\"php/schedules.php?action=delete&session_id=".$row['session_id']."&attendee=".$row['attendee']."\",\"rightPanel\");return false;'>Delete</button>
		</td>";
  }
  echo "</tr>";
}
if( is_resource($conn)) {mysql_close($conn);}
?>			         
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){

			                		            	?>

			                		            <tr>
			                		                  <td colspan="4"><?php echo getCoursesSelectBox("") ?></td>
				                                      <td><?php echo  getDateTimeSelectBox() ?></td>
				                                      <td><input type="text"  name="duration"   value="" size="1"/></td>
				                                      <td><?php echo  getStudentsSelectBox()?></td>
				                                      <td><?php echo  getServersSelectBox()?></td>
				                                       <td><?php echo  getLocationsSelectBox()?></td>
				                                      				                                      
				                                      <td><button  onclick="URL3='php/schedules.php?action=add&course_id='+this.form.course_id.value+'&course_start_date_year='+this.form.course_start_date_year.value+'&course_start_date_month='+this.form.course_start_date_month.value+'&course_start_date_day='+this.form.course_start_date_day.value+'&course_start_time='+this.form.course_start_time.value+'&duration='+this.form.duration.value+'&attendee='+this.form.attendee.value+'&server='+this.form.server.value+'&location='+this.form.location.value;load(URL3,'rightPanel');return false;">ADD</button></td>
			                		            </tr>
			                
			                		            <?php  }?>
			                 </table>
			                 		            </form>