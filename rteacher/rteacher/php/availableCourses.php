<?php
require_once(dirname(__FILE__)."/header.php"); 

function getCategorySelectBox($selectedCategory){
	global $dbhost, $dbuser, $dbpass,$dbname;
	$categorySelectBox="<select name='category'>";
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf("SELECT * FROM  `category`");
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		if($row['name']==$selectedCategory){
			$categorySelectBox=$categorySelectBox."<option selected >".$row['name']."</option>";
		}else{
			$categorySelectBox=$categorySelectBox."<option >".$row['name']."</option>";
		}
	}	
	$categorySelectBox=$categorySelectBox."</select>";
	if( is_resource($conn)) {mysql_close($conn);}
	return $categorySelectBox;
}

function getUsersSelectBox($selectedUser){
	global $dbhost, $dbuser, $dbpass,$dbname;
	$teacherSelectBox="<select name='teacher'>";
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
	mysql_select_db($dbname);
	$query = sprintf("SELECT * FROM  `users` where level='1.teacher'");
	$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		if($row['email']==$selectedUser){
			$teacherSelectBox=$teacherSelectBox."<option selected value='".$row['email']."'>".$row['realname']."</option>";
		}else{
			$teacherSelectBox=$teacherSelectBox."<option value='".$row['email']."'>".$row['realname']."</option>";
		}
	}
	$teacherSelectBox=$teacherSelectBox."</select>";
	if( is_resource($conn)) {mysql_close($conn);}
	return $teacherSelectBox;
}

if(isset($_GET['action']) && isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
	if( $_GET['action']=='updateView'){
		$categorySelectBox=getCategorySelectBox($_GET['category']);
		$teacherSelectBox=getUsersSelectBox($_GET['teacher']);
?>	     
		<form method="get" name="updateForm"  id="updateFormId"  >
		<input type='hidden'  name='course_id'  value='<?php  echo $_GET['course_id']?>'/>
				                <table class="hovertable" width="100%">
			                		<tr><td>Category</td><td>:</td><td><?php echo $categorySelectBox ?></td></tr>
				                    <tr><td>Teacher</td><td>:</td><td><?php echo $teacherSelectBox ?></td></tr>
				                    <tr><td>Title</td><td>:</td><td><input type="text"  name="title"   value="<?php  echo $_GET['title']?>"/></td></tr>
				                    <tr><td colspan="3"><button  onclick="URL2='php/availableCourses.php?action=update&course_id='+this.form.course_id.value+'&category='+this.form.category.value+'&teacher='+this.form.teacher.value+'&title='+this.form.title.value;load(URL2,'rightPanel');document.getElementById('editAvaliableCourse').style.display='none';return false;">SAVE</button><button onclick="document.getElementById('editAvaliableCourse').style.display='none';return false;">CANCEL</button></td></tr>
				                  </table>
		</form>		                    
				                			                		
<?php 	
       exit;
	}
	if( $_GET['action']=='update'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("update available_courses set title='%s' , teacher='%s' , category='%s' where course_id ='%s'",mysql_escape_string($_GET['title']),mysql_escape_string($_GET['teacher']),mysql_escape_string($_GET['category']),mysql_escape_string($_GET['course_id']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
	if( $_GET['action']=='delete'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			
			$query = sprintf("select * from schedule  where course_id ='%s'",mysql_escape_string($_GET['course_id']));
			$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if($row = mysql_fetch_array($result, MYSQL_ASSOC)){
                                echo "<table witdh='%100'><tr><td><font size='4' color='red'>Sorry I can not delete this course definition while there exists a schedule associated with it !</font></td></tr></table>";
           }else{
              $query = sprintf("delete from available_courses  where course_id ='%s'",mysql_escape_string($_GET['course_id']));
			  mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			}
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
	if( $_GET['action']=='add'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("insert into  available_courses  values (md5(CURRENT_TIMESTAMP()),'%s','%s','%s')",mysql_escape_string($_GET['teacher']),mysql_escape_string($_GET['category']),mysql_escape_string($_GET['title']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
}


?>		
                                               <form method="get"   name='addForm'   id='addFormId'>
			                <table class="hovertable" width="100%">
			                		<tr><th>Category</th><th>Teacher/Instructor</th><th>Title</th>
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
			                		            <th></th>
			                		            <?php  }?>
			                		</tr>
<?php
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select * ,(select realname from users where users.email=teacher) as realname from available_courses order by category,teacher,title");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
  echo "<tr><td>".$row['category']."</td><td>".$row['realname']."</td><td>".$row['title']."</td>";
  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
       echo "<td>
		                 <button  onclick='popupScreen(\"php/availableCourses.php?action=updateView&course_id=".$row['course_id']."&category=".$row['category']."&title=".$row['title']."&teacher=".$row['teacher']."\",\"editAvaliableCourse\");return false;'>Edit</button>
		                 <button  onclick=' if (confirm(\"Are you sure you want to delete\" ))load(\"php/availableCourses.php?action=delete&course_id=".$row['course_id']."\",\"rightPanel\");return false;'>Delete</button>
		</td>";
  }
  echo "</tr>";
}
if( is_resource($conn)) {mysql_close($conn);}
?>			         
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
			                		            	$categorySelectBox=getCategorySelectBox('');
			                		            	$teacherSelectBox=getUsersSelectBox('');
			                		            	?>

			                		            <tr>
			                		                  <td><?php echo $categorySelectBox ?></td>
				                                      <td><?php echo $teacherSelectBox ?></td>
				                                      <td><input type="text"  name="title"   value=""/></td>
				                                      <td><button  onclick="URL3='php/availableCourses.php?action=add&category='+this.form.category.value+'&teacher='+this.form.teacher.value+'&title='+this.form.title.value;load(URL3,'rightPanel');return false;">ADD</button></td>
			                		            </tr>
			                
			                		            <?php  }?>
			                 </table>
			                 		            </form>