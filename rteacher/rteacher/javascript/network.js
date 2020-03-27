var ie = (typeof window.ActiveXObject != 'undefined');
var moz = (typeof document.implementation != 'undefined') &&
  (typeof document.implementation.createDocument != 'undefined');

function load(URL,htmlElementId){
document.getElementById(htmlElementId).innerHTML=null;	
 var x;
 if (document.getElementById){
	x = (ie) ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest();
  }
  if (x){
	x.onreadystatechange = function(){
                 if(x.readyState != 4) return; 
				if (x.status == 200){
					
					document.getElementById(htmlElementId).innerHTML=x.responseText;
					document.getElementById(htmlElementId).style.display='block';
				}
				undoWait();
	};
	x.open("GET", URL, true);
	x.send(null);
	doWait();
  }

}


function doWait(){
     document.getElementById('lutfenBekleyinizEkrani').style.pixelTop = (document.body.scrollTop + 50);
     document.getElementById('lutfenBekleyinizEkrani').style.visibility="visible";
     var  message='Please Wait...';
     document.getElementById('lutfenBekleyiniz').removeChild(document.getElementById('lutfenBekleyiniz').firstChild);
     document.getElementById('lutfenBekleyiniz').appendChild(document.createTextNode(message));
     document.onmousedown = function(e){
	alert(message);
	return false;
     };
}	

function undoWait(){
	document.getElementById('lutfenBekleyinizEkrani').style.visibility="hidden";
	document.onmousedown 	= function(e){
	  try{
	    mouseSelect(e);
	  }catch(e){}
	};	
}
