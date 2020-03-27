<?php 
/*
php.ini içinde : file_uploads = On 
*/
require_once(dirname(__FILE__)."/header.php");

$validated=false;


## PROBLEM VALIDATION DA !!!  28.02.2016

if(isset($_GET['username']) && isset($_GET['sessionid']) ){
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
$query = sprintf(" select   attendee,teacher,schedule.session_id as session_id   from schedule left join available_courses on schedule.course_id=available_courses.course_id  left join attendees on schedule.session_id=attendees.session_id where course_date>=CURRENT_DATE()  and course_date<=CURRENT_DATE() order by schedule.session_id ");
$result=mysql_query($query, $conn) or die('Query failed. ' . mysql_error());
//$myfile = fopen("/var/tmp/all/testfile.txt", "w");
while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
	//fwrite($myfile,"session_id=".$row['session_id']."\n");
	//if(    ($_GET['username']==$row['teacher'] ||$_GET['username']==$row['attendee'] ) &&$_GET['sessionid']== $row['session_id']  ){
	if(  $_GET['sessionid']== $row['session_id']  ){
		$validated=true;
	}

}
//fclose($myfile);


if( is_resource($conn)) {mysql_close($conn);}
}


if($validated && isset($_FILES["fileToUpload"])){
	

	
$target_dir = "/var/www/html/rteacher/rteacher/images/";
$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
$uploadOk = 1;
$imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);
// Check if image file is a actual image or fake image
if(isset($_POST["submit"])) {
	$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
	if($check !== false) {
		echo "File is an image - " . $check["mime"] . ".";
		$uploadOk = 1;
	} else {
		echo "File is not an image.";
		$uploadOk = 0;
	}
}
// Check if file already exists
if (file_exists($target_file)) {
	echo "Sorry, file already exists.";
	$uploadOk = 0;
}
// Check file size
if ($_FILES["fileToUpload"]["size"] > 50000000) {
	echo "Sorry, your file is too large.";
	$uploadOk = 0;
}
// Allow certain file formats
if(strtolower($imageFileType) != "jpg" && strtolower($imageFileType) != "png" && strtolower($imageFileType) != "jpeg"
		&& strtolower($imageFileType) != "gif" ) {
			echo "Sorry, only JPG, JPEG, PNG & GIF files are allowed.";
			$uploadOk = 0;
		}
		// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {
			echo "Sorry, your file was not uploaded.";
			// if everything is ok, try to upload file
} else {
			if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
				echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
			} else {
				echo "Sorry, there was an error uploading your file.";
			}
}
}

/**
else{
?>
<!DOCTYPE html>
<html>
<body>

<form action="" method="post" enctype="multipart/form-data">
    <input type="file" name="fileToUpload" id="fileToUpload">
    <input type="submit" value="Upload Image" name="submit">
</form>

</body>
</html> 
<?php }

**/
?>