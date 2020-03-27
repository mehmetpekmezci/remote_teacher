var month = new Array();
month[0] = "January";
month[1] = "February";
month[2] = "March";
month[3] = "April";
month[4] = "May";
month[5] = "June";
month[6] = "July";
month[7] = "August";
month[8] = "September";
month[9] = "October";
month[10] = "November";
month[11] = "December";

var weekday = new Array(7);
weekday[0]=  "Sunday";
weekday[1] = "Monday";
weekday[2] = "Tuesday";
weekday[3] = "Wednesday";
weekday[4] = "Thursday";
weekday[5] = "Friday";
weekday[6] = "Saturday";



function loadAction(URL,div){
	load("php/"+URL+".php",'rightPanel');
	var divs=document.getElementById("directories").getElementsByTagName("div");
	for(var i = 0; i < divs.length;i++){
		divs[i].style.backgroundColor="#FFFFFF";
	}
	div.style.backgroundColor="#E6EBF6";

	if(URL=='logout'){
		window.location.href='./index.php';
	}
}



function popupScreen(URL,divID){

 if(document.getElementById(divID)==null){   
    var div=document.createElement("div");
    document.getElementsByTagName("body")[0].appendChild(div);
    div.style.position="absolute";
    div.style.top="30%";
    div.style.left="42%";
    div.width="300";
    div.height="120";
    div.align="center";
    div.id=divID;
    div.style.backgroundColor = 'white';
    div.style.pixelTop = (document.body.scrollTop + 50);
    div.style.display ='none';  
    div.style.border="2px solid blue";
    
 }
 load(URL,divID);
    
 }
