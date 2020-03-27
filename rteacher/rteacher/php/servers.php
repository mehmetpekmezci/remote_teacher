<?php
require_once(dirname(__FILE__)."/header.php"); 
if(isset($_GET['action']) && isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
	if( $_GET['action']=='updateView'){
?>	     
		<form method="get" name="updateForm"  id="updateFormId"  >
		<input type='hidden'  name='server'  value='<?php  echo $_GET['server']?>'/>
				                <table class="hovertable" width="100%"  >
			                		<tr><td align='right'>Server</td><td>:</td><td align='left'><?php  echo $_GET['server']?></td></tr>
				                    <tr><td align='right'>IP</td><td>:</td><td align='left'><input type="text"  name="ip"   value="<?php  echo $_GET['ip']?>"/></td></tr>
				                    <tr><td align='right'>Company</td><td>:</td><td align='left'><input type="text"  name="company"   value="<?php  echo $_GET['company']?>"/></td></tr>
				                    <tr><td colspan="3"  align='center'><button  onclick="URL2='php/servers.php?action=update&server='+this.form.server.value+'&company='+this.form.company.value+'&ip='+this.form.ip.value;load(URL2,'rightPanel');document.getElementById('editServer').style.display='none';return false;">SAVE</button><button onclick="document.getElementById('editServer').style.display='none';return false;">CANCEL</button></td></tr>
				                  </table>
		</form>      			                		
<?php 	
       exit;
	}
	if( $_GET['action']=='update'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("update server set  ip='%s' , company='%s'  where name ='%s'",mysql_escape_string($_GET['ip']),mysql_escape_string($_GET['company']),mysql_escape_string($_GET['server']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	if( $_GET['action']=='delete'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("delete from server  where name ='%s'",mysql_escape_string($_GET['server']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
	if( $_GET['action']=='add'){
			$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
			mysql_select_db($dbname);
			$query = sprintf("insert into  server values('%s','%s','%s')",mysql_escape_string($_GET['server']),mysql_escape_string($_GET['ip']),mysql_escape_string($_GET['company']));
			mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
			if( is_resource($conn)) {mysql_close($conn);}
    }
	
}

?>		
                                               <form method="get"   name='addForm'   id='addFormId'>
			                <table class="hovertable" width="100%">
			                		<tr><th>Server</th><th>IP</th><th>Company</th><th></th></tr>
<?php
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select *  from server order by name");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){
     $tableRow="<tr><td>".$row['name']."</td><td>".$row['ip']."</td><td>".$row['company']."</td>".
       "<td>
		                 <button  onclick='popupScreen(\"php/servers.php?action=updateView&server=".$row['name']."&ip=".$row['ip']."&company=".$row['company']."\",\"editServer\");return false;'>Edit</button>
			             <button  onclick=' if (confirm(\"Are you sure you want to delete\" ))load(\"php/servers.php?action=delete&server=".$row['name']."\",\"rightPanel\");return false;'>Delete</button>
		</td>";
}else{
    $tableRow="<tr><td>".$row['name']."</td><td>".$row['ip']."</td><td>".$row['company']."</td><td></td>";
}
  echo $tableRow;

  echo "</tr>";
}
if( is_resource($conn)) {mysql_close($conn);}
?>			         
			                		            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
			                		            <tr>
			                		                  <td><input type="text " name="server"   size="10"/></td>
			                		                 <td><input type="text"  name="ip"  size="10"/></td>
			                		                  <td><input type="text"  name="company"  size="10"/></td>
			                		                  <td><button  onclick="URL3='php/servers.php?action=add&server='+this.form.server.value+'&ip='+this.form.ip.value+'&company='+this.form.company.value;load(URL3,'rightPanel');">ADD</button></td>
			                		            </tr>
			                		            <?php  }?>
			                 </table>
			                 		            </form>