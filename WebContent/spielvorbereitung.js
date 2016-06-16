//Listener f. Dokument registrieren
 document.addEventListener('DOMContentLoaded', init, false);

var socket;
var bereitZumSenden=false;

function init()
{
	var anmelden = window.document.getElementById("anmelden");
	anmelden.addEventListener("click",anmelden,false);
	
	var start = window.document.getElementyById("starten");
	start.addEventListener("click",start,false);
	
	var url = "ws://localhost:8085/Quiz/Echo";
	alert("url ="+url);
	
	socket=new WebSocket(url);
	
	socket.onopen=rdytosend;
	socket.onclose=close;
	socket.onerror=error;
	socket.onmessage=recv;
}

//Listener für Button anmelden
function anmelden(event){
	var button = event.target;
	var name = windows.document.getElementById("name").value;
	if (bereitZumSenden == true){
		alert("Jetzt senden"); socket.send(name);}
		else alert("Server noch nicht bereit zum Empfangen. Bitte nochmals versuchen");
}

function rdytosend(){bereitZumSenden=true; alert("Senden ist möglich");}
function close(event){alert("Wobsockets closing"+event.code);}
function error(event){alert("Fehler bei den  Websockets "+event.data);}

function recv(){}

function start(event){}

