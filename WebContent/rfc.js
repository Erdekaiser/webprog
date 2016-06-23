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
}

function recv(){
	//ToDo Client RFC
}

function close(){}
function error(){}

//noch als Tabelle realisieren, sonst überschreibt sich immer der aktuelle User.
//div anzeige muss noch geupdated werden sowie das spiel startet.
function spielerlistelistener(event){
	var playerlist = JSON.parse(event.data);
	var playerDiv = document.getElementById("User");
	
	clearPlayerlist();
	
	if (playerlist.toString() == ""){
		return;
	}

	for(var i=0; i<playerlist.toString().split(",").length; i++){
		playerDiv.innerHTML = playerlist[i].name;
	}
}

function clearPlayerlist(){
	var playerDiv = document.getElementById("User");
	
	playerDiv.innerHTML = "";
}