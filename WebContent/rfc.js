var ready = false;
function anmeldenclick(event){
	var name = window.document.getElementById("name").value;
	
	if(name == ''){
		alert("Bitte einen Benutzernamen eingeben!");
		return;
	}
	
	if(readyToSend){
		socket.send(JSON.stringify({typ:1, data:name}));
	}
	
	document.getElementById("anzeige").innerHTML = "Warte auf weitere Spieler ...";
	document.getElementById("anmelden").style.visibility = "hidden";
	document.getElementById("name").style.visibility = "hidden";
	if (ready){
		document.getElementById("starten").setAttribute("style", "display: block");
	}
}

function recv(){
	//ToDo Client RFC
}

function close(){}
function error(){}

function spielerlistelistener(event){
	var playerlist = JSON.parse(event.data);
	var playerDiv = document.getElementById("User");
	var spieler;
	
	if (playerlist.toString() == ""){
		return;
	}
	
    document.getElementById("User").innerHTML = "";
    for (var i = 0; i <playerlist.toString().split(",").length; i++) {
        spieler = document.createElement("div");
        spieler.className = "spieler"
        spieler.innerHTML = "   <div class=\"name\">" + playerlist[i].name + "</div><div class=\"punkte\">"+playerlist[i].score+"</div>"
        document.getElementById("User").appendChild(spieler)
        spieler=null;
    }
    ready = playerlist.toString().split(",").length>=2;
    
}