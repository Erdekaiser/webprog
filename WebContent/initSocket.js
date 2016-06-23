//Listener f. Dokument registrieren

function initWebSocket()
{
	var url = "ws://localhost:8080/Quiz/Socket";
	
	alert("url: "+url);
	socket=new WebSocket(url);
	
	socket.onopen=rdytosend;
	socket.onmessage=recv;
	socket.onclose=close;
	socket.onerror=error;
}

function rdytosend(){
	readyToSend = true;
}