<?php
require_once(dirname(__FILE__)."/header.php"); 
if(isset($_GET['action']) && isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
	if( $_GET['action']=='updateView'){
?>	     
		<form method="get" name="updateForm"  id="updateFormId"  >
		<input type='hidden'  name='location'  value='<?php  echo $_GET['location']?>'/>
				                <table class="hovertable" width="100%"  >
			                		<tr><td align='right'>Location</td><td>:</td><td align='left'><?php  echo $_GET['location']?></td></tr>
				                   <tr><td align='right'>Address</td><td>:</td><td align='left'><input type="text"  name="address"   value="<?php  echo $_GET['address']?>"/></td></tr>
				                    <tr><td align='right'>Map</td><td>:</td><td align='left'><input type="text"  name="map_url"   value="<?php  echo $_GET['map_url']?>"/></td></tr>
				                    <tr><td colspan="3"  align='center'><button  onclick="URL2='php/locations.php?action=update&location='+this.form.location.value+'&address='+this.form.address.value+'&map_url='+this.form.map_url.value;load(URL2,'rightPanel');document.getElementById('editLocation').style.display='none';return false;">SAVE</button><button onclick="document.getElementById('editLocation').style.display='none';return false;">CANCEL</button></td></tr>
				                  </table>
		</form>      			                		
<?php 	
       exit;
	}
	if( $_GET['action']=='update'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("update location set address='%s' ,map_url='%s'  where name ='%s'",mysql_escape_string($_GET['address']),mysql_escape_string($_GET['map_url']),mysql_escape_string($_GET['location']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	if( $_GET['action']=='delete'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("delete from location  where name ='%s'",mysql_escape_string($_GET['location']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
	if( $_GET['action']=='add'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("insert into  location values('%s','%s','%s')",mysql_escape_string($_GET['location']),mysql_escape_string($_GET['address']),mysql_escape_string($_GET['map_url']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
}

?>		
                                               <form method="get"   name='addForm'   id='addFormId'>
			                <table class="hovertable" width="100%">
			                		<tr><th>Location</th><th>Address</th><th>Map</th><th></th></tr>
<?php
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select *  from location order by name");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
     $tableRow="<tr><td>".$row['name']."</td><td>".$row['address']."</td><td><a target='_blank' href='".$row['map_url']."'>GOOGLE MAPS</a></td>".
       "<td>
		                 <button  onclick='popupScreen(\"php/locations.php?action=updateView&location=".$row['name']."&address=".$row['address']."&ip=".$row['ip']."&map_url=".$row['map_url']."\",\"editLocation\");return false;'>Edit</button>
			             <button  onclick=' if (confirm(\"Are you sure you want to delete\" ))load(\"php/locations.php?action=delete&location=".$row['name']."\",\"rightPanel\");return false;'>Delete</button>
		</td>";
}else{
    $tableRow="<tr><td>".$row['name']."</td><td>".$row['address']."</td><td><a target='_blank' href='".$row['map_url']."'>GOOGLE MAPS</a></td><td></td>";
}
  echo $tableRow;

  echo "</tr>";
}
if( is_resource($conn)) {mysql_close($conn);}
?>			         
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
			                		            <tr>
			                		                  <td><input type="text " name="location"   size="10"/></td>
			                		                 <td><input type="text " name="address" size="10"/></td>
			                		                  <td><input type="text"  name="map_url"  size="10"/></td>
			                		                  <td><button  onclick="URL3='php/locations.php?action=add&location='+this.form.location.value+'&address='+this.form.address.value+'&map_url='+this.form.map_url.value;load(URL3,'rightPanel');">ADD</button></td>
			                		            </tr>
			                		            <?php  }?>
			                 </table>
			                 		            </form>