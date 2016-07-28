var issuperuser= false;
var gameisrunning = false;
var isactiveplayer =false;
var katalogausgewaelt= false;

//Funktion für den Anmelden Button
function anmeldenclick(event){
	if (!gameisrunning){
		var name = window.document.getElementById("name").value;
		
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

//Funktion für den Starten Button
function startenclick (event){
	
	if(katalogausgewaelt == true){
		if(issuperuser&&!gameisrunning){
			if(readyToSend){
				socket.send(JSON.stringify({typ:3, data:"Spiel beginnt"}));
			}
			document.getElementById("starten").setAttribute("style", "display: none");
		}
	}else{
		alert("Bitte Fragekataloge auswählen");
	}
}

//Funktion für den New Game Button
function nsclick(){
	location.reload(true);
}

//Funktion nac
function spielwurdegestartet(){
	document.getElementById("quiz").setAttribute("style", "background-color: white");
	document.getElementById("Frage").setAttribute("style", "display: block");
	var fragen =document.getElementsByClassName("Antwort");

	gameisrunning = true;
	for (var i = 0; i <fragen.length; i++){
		fragen[i].setAttribute("style", "display: inline-block");
	}
	neuefrageanfordern();
}

function recv(message){
	var daten = JSON.parse(message.data);
	console.log(daten);
	
	switch(daten.typ){
	case 1:
		i_am_a_superuser()
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
		neueFrage(daten.data)
		//Antwort (index) auf die Frage mit Typ 6 an den Server.
		break;
	case 7:
		//QuestionResult
		//Server gibt zurück ob Frage richtig beantwortet wurde.
		showCorrectAnswer(daten.data);
		break;
	case 8:
		//Client hat GameOver erreicht (keine Fragen mehr übrig).
		gameover();
		break;
	case 9:
		//Alle Clients haben GameOver erreicht (keiner hat mehr fragen übrig).
		gameoverall();
		break;
	case 10:
		//Timeout einer Frage wurde erreicht, neue frage anfordern.
		//Falls keine fragen mehr da sind antwortet Server mit 8 bzw. 9.
		console.log("Question Timeout erreicht.");
		neuefrageanfordern();
		break;
	case 11:
		i_am_a_superuser();
		break;
	case 255:
		//Fehlermeldung kommt rein, muss angezeigt werden.
		showerror(msg);
		break;
	default:
		alert("Ungültiger RFC Typ!");
	}
}

//Ungenutzt. Für Socket close/error
function close(){}
function error(){}

//Funktion für EventListener (SSE) der Spielerliste
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
    
    if(!gameisrunning){
    	showstart(playerlist.length>=2)
    }
}


function katalogLaden(event){
	if(issuperuser && !gameisrunning){
		
		if(readyToSend){
			socket.send(JSON.stringify({typ:2, data:event.target.id}));
			katalogausgewaelt=true;
		}
		
	}
}

function antwortclick(event){
	if (gameisrunning){
		if(readyToSend){
			socket.send(JSON.stringify({typ:6, data:parseInt(event.target.id)}));
		}
		//setTimeout(neuefrageanfordern(), 1000);
	}
}

function updateFragenKatalog(name){
	var active = document.getElementsByClassName("active");
	var activeneu = document.getElementById(name);
	
	if (activeneu!=null){
		
		for(var i=0;i<active.length;i++){
			active[i].className="Kataloge";
		}
		activeneu.className="Kataloge active";
	}
}

function neueFrage(json){ 
	var daten = JSON.parse(json);
	document.getElementById("Frage").innerHTML=daten["question"];
	var frage = daten["answerliste"];
	for(var i =0;i<4;i++){
		document.getElementById(i).innerHTML=frage[i];
	}
	
}

function gameover(){
	//alert("Game over");
	document.getElementById("Frage").innerHTML="<h2>Sie sind Fertig</h2><br>Bitte warten sie bis alle anderen Fertig sind";

	document.getElementById("quiz").setAttribute("style", "background-color: lightgrey");
	var fragen =document.getElementsByClassName("Antwort");

	for (var i = 0; i <fragen.length; i++){
		fragen[i].setAttribute("style", "display: none");
	}
}

function gameoverall(){
	//alert("Alle sind fertig");
	document.getElementById("quiz").setAttribute("style", "background-color: lightgrey");
	document.getElementById("Frage").innerHTML="<h2>Alle Spieler sind Fertig</h2><br>Bitte wählen Sie \"Neues Spiel\" aus um eine neue Runde zu starten.";
	document.getElementById("neuesspiel").setAttribute("style", "display: block");
	var fragen =document.getElementsByClassName("Antwort");

	for (var i = 0; i <fragen.length; i++){
		fragen[i].setAttribute("style", "display: none");
	}

	//gameisrunning = false;
	//showstart(true);
	//ggf. Spielende Nachricht anzeigen lassen + neuen Button der das Fenster neu lädt:
	//window.location.reload();
}

function showstart(ja){
	
	if(ja && issuperuser == true &&gameisrunning==false){
    	document.getElementById("starten").setAttribute("style", "display: block");
    }else if (!ja){
    	document.getElementById("starten").setAttribute("style", "display: none");
    }
}

function showerror (msg){
	alert("Es ist ein schwerwiegender Fehler aufgetreten");
	document.getElementById("Frage").innerHTML="<h2>Error</h2>"+msg;
	
	var fragen =document.getElementsByClassName("Antwort");

	for (var i = 0; i <fragen.length; i++){
		fragen[i].setAttribute("style", "display: none");
	}
}

function neuefrageanfordern(){
	
	
	if(readyToSend){
		socket.send(JSON.stringify({typ:4, data:" "}));
	}
	
	for(var i = 0; i < 4; i++){
		document.getElementById(i).style.backgroundColor = "dodgerblue";
	}
}

function i_am_a_superuser(){
		issuperuser=true;
}

function showCorrectAnswer(json){
	var antwort = JSON.parse(json);
	
	if(antwort.selected == antwort.correct){
		document.getElementById(antwort.selected).style.backgroundColor = "green";
	} else {
		document.getElementById(antwort.selected).style.backgroundColor = "red"
		document.getElementById(antwort.correct).style.backgroundColor = "green";
	}
	
	var fragen = document.getElementsByClassName("Antwort");
	for (var i =0; i< fragen.length ;i++){
		fragen[i].removeEventListener('click', antwortclick, false)
	}
	startTimer();
}

function startTimer () {
    setTimeout(stopTimer,3000);
}

function stopTimer () {
	neuefrageanfordern();
	var fragen = document.getElementsByClassName("Antwort");
	for (var i =0; i< fragen.length ;i++){
		fragen[i].addEventListener('click', antwortclick, false)
	}
}
