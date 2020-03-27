<?php
require_once(dirname(__FILE__)."/header.php"); 
if (!isset($_SESSION['level'])||$_SESSION['level']!="0.manager"){
	exit;
}

if(isset($_GET['action']) && isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
	if( $_GET['action']=='updateView'){
?>	     
		<form method="get" name="updateForm"  id="updateFormId"  >
		<input type='hidden'  name='email'  value='<?php  echo $_GET['email']?>'/>
				                <table class="hovertable" width="100%"  >
			                		<tr><td align='right'>Email</td><td>:</td><td align='left'><?php  echo $_GET['email']?></td></tr>
				                    <tr><td align='right'>Real Name</td><td>:</td><td align='left'><input type="text"  name="realname"   value="<?php  echo $_GET['realname']?>"/></td></tr>
				                    <tr><td align='right'>Level</td><td>:</td><td align='left'><select name='level'>
				                                          <option value="2.student"  <?php if($_GET['level']=='2.student') echo 'selected';?>>Student</option>
				                                          <option value="1.teacher" <?php if($_GET['level']=='1.teacher') echo 'selected';?>>Teacher</option>
				                                          <option value="0.manager"  <?php if($_GET['level']=='0.manager') echo 'selected';?>>Manager</option>
				                                         </select></td></tr>
				                    <tr><td align='right'>Phone</td><td>:</td><td align='left'><input type="text"  name="phone"   value="<?php  echo $_GET['phone']?>"/></td></tr>
				                    <tr><td align='right'>Address</td><td>:</td><td align='left'><input type="text"  name="address"   value="<?php  echo $_GET['address']?>"/></td></tr>
				                    <tr><td align='right'>School Name</td><td>:</td><td align='left'><input type="text"  name="schoolname"   value="<?php  echo $_GET['schoolname']?>"/></td></tr>
				                    <tr><td align='right'>Country</td><td>:</td><td align='left'><input type="text"  name="country"   value="<?php  echo $_GET['country']?>"/></td></tr>
				                    <tr><td align='right'>City</td><td>:</td><td align='left'><input type="text"  name="city"   value="<?php  echo $_GET['city']?>"/></td></tr>
				                    <tr><td colspan="3"  align='center'><button  onclick="URL2='php/users.php?action=update&email='+this.form.email.value+'&realname='+this.form.realname.value+'&level='+this.form.level.value+'&phone='+this.form.phone.value+'&address='+this.form.address.value+'&schoolname='+this.form.schoolname.value+'&country='+this.form.country.value+'&city='+this.form.city.value;load(URL2,'rightPanel');document.getElementById('editUser').style.display='none';return false;">SAVE</button><button onclick="document.getElementById('editUser').style.display='none';return false;">CANCEL</button></td></tr>
				                  </table>
		</form>      			                		
<?php 	
       exit;
	}
	if( $_GET['action']=='updatePassword'){
?>	     
		<form method="get" name="updateForm"  id="updateFormId"  >
		<input type='hidden'  name='email'  value='<?php  echo $_GET['email']?>'/>
				                <table class="hovertable" width="100%"  >
			                		<tr><td align='right'>Email</td><td>:</td><td align='left'><?php  echo $_GET['email']?></td></tr>
				                    <tr><td align='right'>Password</td><td>:</td><td align='left'><input type="text"  name="password"   value=""/></td></tr>
				                    <tr><td colspan="3"  align='center'><button  onclick="URL2='php/users.php?action=updatep&email='+this.form.email.value+'&password='+this.form.password.value;load(URL2,'rightPanel');document.getElementById('editUser').style.display='none';return false;">SAVE</button><button onclick="document.getElementById('editUser').style.display='none';return false;">CANCEL</button></td></tr>
				                  </table>
		</form>      			                		
<?php 	
       exit;
	}
	if( $_GET['action']=='update'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("update users set realname='%s' , level='%s' , phone='%s' , address='%s', schoolname='%s' , country='%s' , city='%s'  where email ='%s'",mysql_escape_string($_GET['realname']),mysql_escape_string($_GET['level']),mysql_escape_string($_GET['phone']),mysql_escape_string($_GET['address']),mysql_escape_string($_GET['schoolname']),mysql_escape_string($_GET['country']),mysql_escape_string($_GET['city']),mysql_escape_string($_GET['email']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
    if( $_GET['action']=='updatep'){
    	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
    	mysql_select_db($dbname);
    	$query = sprintf("update users set password=md5('%s')  where email ='%s'",mysql_escape_string($_GET['password']),mysql_escape_string($_GET['email']));
    	mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
    	if( is_resource($conn)) {mysql_close($conn);}
    }
    
	if( $_GET['action']=='delete'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("delete from users  where email ='%s'",mysql_escape_string($_GET['email']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
	if( $_GET['action']=='add'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("insert into  users values('%s',md5('qwe123'),'%s','%s','%s','%s','%s','%s','%s')",mysql_escape_string($_GET['email']),mysql_escape_string($_GET['realname']),mysql_escape_string($_GET['level']),mysql_escape_string($_GET['phone']),mysql_escape_string($_GET['address']),mysql_escape_string($_GET['schoolname']),mysql_escape_string($_GET['country']),mysql_escape_string($_GET['city']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
}



?>		
                                               <form method="get"   name='addForm'   id='addFormId'>
			                <table class="hovertable" width="100%">
			                		<tr><th>Real Name</th><th>Email</th><th>Level</th><th>Phone</th><th>Address</th><th>School Name</th><th>Country</th><th>City</th><th></th></tr>
<?php
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select *  from users order by realname,email");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
  echo "<tr><td>".$row['realname']."</td><td>".$row['email']."</td><td>".$row['level']."</td><td>".$row['phone']."</td><td>".$row['address']."</td><td>".$row['schoolname']."</td><td>".$row['country']."</td><td>".$row['city']."</td>";
  
       echo "<td>
		                 <button  onclick='popupScreen(\"php/users.php?action=updatePassword&email=".$row['email']."\",\"editUser\");return false;'>Set Password</button>
		                 <button  onclick='popupScreen(\"php/users.php?action=updateView&email=".$row['email']."&realname=".$row['realname']."&level=".$row['level']."&phone=".$row['phone']."&address=".$row['address']."&schoolname=".$row['schoolname']."&country=".$row['country']."&city=".$row['city']."\",\"editUser\");return false;'>Edit</button>
			             <button  onclick=' if (confirm(\"Are you sure you want to delete\" ))load(\"php/users.php?action=delete&email=".$row['email']."\",\"rightPanel\");return false;'>Delete</button>
		</td>";
  }
  echo "</tr>";

if( is_resource($conn)) {mysql_close($conn);}
?>			         
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
			                		            <tr>
			                		                  <td><input type="text " name="realname"   size="10"/></td>
			                		                 <td><input type="text " name="email" size="10"/></td>
			                		                 <td><select name='level' ><option value="2.student"  >Student</option><option value="1.teacher" >Teacher</option><option value="0.manager"  >Manager</option></select></td>
				                                     <td><input type="text"  name="phone"  size="10"/></td>
			                		                  <td><input type="text"  name="address"  size="10"/></td>
			                		                  <td><input type="text"  name="schoolname"  size="10"/></td>
			                		                  <td><input type="text"  name="country"  size="10"/></td>
			                		                  <td><input type="text"  name="city"  size="10" /></td>
			                		                   <td><button  onclick="URL3='php/users.php?action=add&email='+this.form.email.value+'&realname='+this.form.realname.value+'&level='+this.form.level.value+'&phone='+this.form.phone.value+'&address='+this.form.address.value+'&schoolname='+this.form.schoolname.value+'&country='+this.form.country.value+'&city='+this.form.city.value;load(URL3,'rightPanel');">ADD</button></td>
			                		            </tr>
			                		            <?php  }?>
			                 </table>
			                 		            </form>