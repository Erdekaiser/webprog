var name = '';
var issuperuser= false;
var gameisrunning = false;
var isactiveplayer =false;
function anmeldenclick(event){
	if (!gameisrunning){
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
		isactiveplayer= true;
	}
}

function startenclick (event){

	if(issuperuser&&!gameisrunning){
		if(readyToSend){
			socket.send(JSON.stringify({typ:3, data:"Spiel beginnt"}));
		}
		//wird trotzdem noch angezeigt?
		document.getElementById("starten").setAttribute("style", "display: none");
	}
}

function spielwurdegestartet(){
	document.getElementById("quiz").setAttribute("style", "background-color: white");
	document.getElementById("Frage").setAttribute("style", "display: block");
	var fragen =document.getElementsByClassName("Antwort");

	gameisrunning = true;
	for (var i = 0; i <fragen.length; i++){
		fragen[i].setAttribute("style", "display: inline-block");
	}
	if(readyToSend){
		socket.send(JSON.stringify({typ:4, data:""}));
	}
}

function recv(message){
	var daten = JSON.parse(message.data);
	console.log(daten);
	
	switch(daten.typ){
	case 2:
		//aktuellen Katalog markieren
		updateFragenKatalog(daten.data);
		break;
	case 3:
		//Spiel vorbereiten
		spielwurdegestartet();
		break;
	case 5:
		//Frage + Antwortliste ist da.
		//Antwort (index) auf die Frage mit Typ 6 an den Server.
		break;
	case 7:
		//QuestionResult
		//Server gibt zurück ob Frage richtig beantwortet wurde.
		break;
	case 8:
		//Client hat GameOver erreicht (keine Fragen mehr übrig).
		break;
	case 9:
		//Alle Clients haben GameOver erreicht (keiner hat mehr fragen übrig).
		break;
	case 10:
		//Timeout einer Frage wurde erreicht, neue frage anfordern.
		//Falls keine fragen mehr da sind antwortet Server mit 8 bzw. 9.
		console.log("Question Timeout erreicht.");
		break;
	case 255:
		//Fehlermeldung kommt rein, muss angezeigt werden.
		break;
	default:
		alert("Ungültiger RFC Typ!");
	}
}

function close(){}
function error(){}

function spielerlistelistener(event){
	alert("Alive");
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
	alert("alive");
	if(issuperuser && !gameisrunning){
		
		if(readyToSend){
			socket.send(JSON.stringify({typ:2, data:event.target.id}));
		}
	}
}

function antwortclick(event){
	if (gameisrunning){
		if(readyToSend){
			socket.send(JSON.stringify({typ:6, data:event.target.id}));
			socket.send(JSON.stringify({typ:4, data:" "}));
		}
	}
}

//Ja ich weiß, kein CSS. tooo lazy. Aber sollte so ähnlich funktionieren, musst nur noch deine CSS Klassen reinbaun.
function updateFragenKatalog(name){
	var auswahlElements = document.getElementById("menubar").getElementsByTagName("div");
	var fetterCatalog	= "<b>" + name + "</b>";
	
	for (var i=0; i< auswahlElements.length; i++) {
		
		//Katalog schon fett?
		if(fetterCatalog == auswahlElements[i].innerHTML){
			return;
		}
		
		if (name== auswahlElements[i].innerHTML) {
			//Ausgewaehlter Katalog -> dann fett 
			auswahlElements[i].innerHTML = fetterCatalog;
		}
		else {
			//Nicht ausgewaehlter Katalog -> fett entfernen
			auswahlElements[i].innerHTML = auswahlElements[i].innerHTML.replace(/(<b>|<\/b>)/g, "");
		}	
	}
}

function neueFrage(json){ 
	var daten = JSON.parse(message.data);
	document.getElementById("Frage").innerHTML= daten.question;
	//document.
	
}