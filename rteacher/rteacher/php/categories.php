<?php
require_once(dirname(__FILE__)."/header.php"); 

if(isset($_GET['action']) && isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
  
	if( $_GET['action']=='delete'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("delete from category  where name ='%s'",mysql_escape_string($_GET['category']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
	if( $_GET['action']=='add'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("insert into  category values('%s')",mysql_escape_string($_GET['category']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
}



?>		
<form method="get"   name='addForm'   id='addFormId'>
			                <table class="hovertable" width="100%">
			                		<tr><th>Category</th><th></th></tr>
<?php
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select *  from category order by name");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
  echo "<tr><td>".$row['name']."</td>";
  
       echo "<td>
		                 <button  onclick=' if (confirm(\"Are you sure you want to delete\" ))load(\"php/categories.php?action=delete&category=".$row['name']."\",\"rightPanel\");return false;'>Delete</button>
		</td>";
  }
  echo "</tr>";

if( is_resource($conn)) {mysql_close($conn);}
?>			         
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
			                		            <tr>
			                		                  <td><input type="text " name="category"   size="10"/></td>
			                		                   <td><button  onclick="URL3='php/categories.php?action=add&category='+this.form.category.value;load(URL3,'rightPanel');">ADD</button></td>
			                		            </tr>
			                		            <?php  }?>
			                 </table>
			                 		            </form>