 <?php
require_once(dirname(__FILE__)."/php/header.php"); 
$selectedSection="availableCourses";
if (isset($_GET['section'])&&$_GET['section']!=""){
	$selectedSection=$_GET['section'];
}		
?>		
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>::  Remote Teacher  ::</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script language="JavaScript" type="text/javascript" src="javascript/network.js"></script>
<script language="JavaScript" type="text/javascript" src="javascript/index.js"></script>
<script>

var now=new Date(<?php echo round(microtime(true) * 1000) ;?>);
timeObject=now;

function increment(){
	timeObject = new Date(timeObject .getTime() + 1000);
	var y=timeObject.getFullYear();
	var mo=timeObject.getMonth();
	mo=month[mo];
	var d=timeObject.getDate();
	d=checkTime(d);
    var h=timeObject.getHours();
    var m=timeObject.getMinutes();
    var s=timeObject.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    if(document.getElementById('clock'))
	document.getElementById('clock').innerHTML =d+"  "+mo+"  "+y+"      "+ weekday[timeObject.getDay()]+"                       "+h+":"+m+":"+s;
	var t = setTimeout(function(){increment()},1000);
}
function checkTime(i) {
    if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}

increment();

</script>
</head>
<body onload="loadAction('<?php echo $selectedSection?>',document.getElementById('<?php echo $selectedSection?>Div'))">
<div align="center"> 
<table width="1200" align="center" id="anatablo" cellpadding="0" cellspacing="0" >
	<tr>
	   <td width="30%"  class="usttd"> <font size="4" color="#495088"> <div  id="realnameDiv" align="left"><?php  echo $_SESSION['realname'] ?></div></font></td>
		<td width="40%"   id="usttd"  valign="bottom" align="center"  >
			<font size="6" color="#495088">  Remote Teacher </font>
		</td>
	     <td width="30%"  class="usttd"> <font size="4" color="#495088"> <div  id="clock" align="right"></div></font></td>
	</tr>
 	<tr>
		<td width="100%" style="padding: 0px 0px 0px 0px;" colspan="3">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr> 
					<td  id="directories" class="index" width="15%" align="left">
						<div class="openindexitem" id="systemDescriptionDiv" ><a href="" onclick="loadAction('systemDescription',this.parentNode);return false;">System Description</a></div>
						<div class="openindexitem" id="availableCoursesDiv" ><a href="" onclick="loadAction('availableCourses',this.parentNode);return false;">Avaliable Courses</a></div>
						<div class="openindexitem" id="schedulesDiv"><a href=""  onclick="loadAction('schedules',this.parentNode);return false;">Schedules</a></div>
						<div class="openindexitem" id="sessionNotesDiv"><a href="" onclick="loadAction('sessionNotes',this.parentNode);return false;">Session Notes</a></div>
						<div class="openindexitem" id="locationsDiv"><a href="" onclick="loadAction('locations',this.parentNode);return false;">Locations</a></div>
<?php  if (isset($_SESSION['username'])){?>
            <?php  if (isset($_SESSION['level'])&&$_SESSION['level']=="0.manager"){?>
						<div class="openindexitem" id="serversDiv"><a href="" onclick="loadAction('servers',this.parentNode);return false;">Servers</a></div>
                        <div class="openindexitem" id="categoriesDiv"><a href="" onclick="loadAction('categories',this.parentNode);return false;">Categories</a></div>
            			<div class="openindexitem" id="usersDiv"><a href="" onclick="loadAction('users',this.parentNode);return false;">Users</a></div>
                        <?php }?>
						<div class="openindexitem" id="logoutDiv"><a href="" onclick="loadAction('logout',this.parentNode);return false;">Logout</a></div>
<?php }else {?>
						<div class="openindexitem" id="loginDiv"><a href="" onclick="url=window.location.href;url = url.replace(/^http:\/\//i, 'https://');url=url.replace(/index.php.*/i,'');window.location.href=url+'/index.php?section=login';return false;">Login</a></div>
<?php }?>
					</td>
					<td  id="rightPanel"  class="contentbody"  width="100%">				

					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

</div>


<div id="lutfenBekleyinizEkrani" style="position:absolute;z-index:5;top:30%;left:42%;visibility:hidden">
	<table bgcolor="white" border="1" bordercolor="#000000"cellpadding="0" cellspacing="0" height="100" width="150" id="Table1">
		<tr>
			<td  WIDTH="100%" HEIGHT="100%" bgcolor="white" ALIGN="CENTER" VALIGN="MIDDLE">
				<b id="lutfenBekleyiniz">Please Wait...</b>
			</td>
		</tr>
	</table>
</div>
</body>
</html>

