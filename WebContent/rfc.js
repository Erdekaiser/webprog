var name = '';
var issuperuser= false;
function anmeldenclick(event){
	name = window.document.getElementById("name").value;
	
	if(name == ''){
		alert("Bitte einen Benutzernamen eingeben!");
		return;
	} 

	if(readyToSend){
		socket.send(JSON.stringify({typ:1, data:name}));
	}

	document.getElementById("anzeige").innerHTML = "Warte auf weitere Spieler ...";
	document.getElementById("anmelden").setAttribute("style", "display: none");
	document.getElementById("name").setAttribute("style", "display: none");
	
}

function startenclick (event){
	alert ("game start");
	if(readyToSend){
		socket.send(JSON.stringify({typ:3, data:"Spiel beginnt"}));
	}
	document.getElementById("starten").setAttribute("style", "display: none");
	spielwurdegestartet()
}

function spielwurdegestartet(){
	document.getElementById("quiz").setAttribute("style", "background-color: white");
	document.getElementById("Frage").setAttribute("style", "display: block");
	var fragen =document.getElementsByClassName("Antwort");
	for (var i = 0; i <fragen.length; i++){
		fragen[i].setAttribute("style", "display: inline-block");
	}
}

function recv(){
	alert ("Nachricht empfangen");
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
    if(playerlist[playerlist.length-1].name== name && playerlist.length>=0){
    	issuperuser= true;
    	document.getElementById("starten").setAttribute("style", "display: block");
    }else{
    	issuperuser= false;
    	document.getElementById("starten").setAttribute("style", "display: none");
    }
}


function katalogLaden(event){
	if(issuperuser){
		if(readyToSend){
			socket.send(JSON.stringify({typ:2, data:event.target.Id}));
		}
		document.getElementsByClassName("active")[0].className="Kataloge";
		event.target.className = "Kataloge active";
		
	}
		
}

function antwortclick(event){
	alert("Frage:"+ event.target.Id +"wurde geclicked")
}
