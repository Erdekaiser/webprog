var request;

//listener für event!! und senden!!
function katalogeLaden(){
	request = new XMLHttpRequest();
	request.open("POST", "Katalog", true);
	if(request.readyState == 4 && request.status == 200){
		alert("Antwort vom Server vollständig erhalten\n Server Antwort: "+request.responseText+" \nstatus server "+request.status);
		katalogSenden();
	}
}

function katalogSenden()
{    
	// var textfelder=window.document.getElementsByName("menubar");
     if ( request.readyState == 4 && request.status == 200)
	 {
		      var kataloge = JSON.parse(request.responseText);
	 	        
	 	      var katalog;
	 	      document.getElementById("menubar").innerHTML = "";//<div id="menubar">innerhtml</div>
	 	      for (var i = 0; i < kataloge.length; i++) {
	 	          katalog = document.createElement("div");
	 	          katalog.className = "katalog"
	 	          katalog.innerHTML = "   <div class=\"name\">" + user[i] + "</div><div class=\"punkte\">0</div>"
	 	      }      
	 }
}