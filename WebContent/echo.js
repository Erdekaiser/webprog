/**
 * @author silvia
 */

 // Listener für das Dokument regsitrieren
 document.addEventListener('DOMContentLoaded', init, false);
 
 var socket;
 var bereitZumSenden=false;
 
 function init()
 {  // Listener registrieren fÃ¼r Buttons
	var go= window.document.getElementById("go");   
	go.addEventListener("click",send,false); 

	var url = "ws://localhost:8085/Quiz/Socket";
	alert("url= "+url);
	
	socket=new WebSocket(url);

	socket.onopen=sendenMoeglich;
	socket.onclose=Closing;
	socket.onerror=ErrorHandler;
	socket.onmessage=empfange;
 }

 
// Listener fÃ¼r den Button go
 function send(event)
 {  
 	var button = event.target;
 	var outmessage=window.document.getElementById("eingabetext").value; 
 	if ( bereitZumSenden == true)
 	    { alert("Jetzt senden");socket.send(outmessage);}
 	else alert("Server noch nicht bereit zum Empfangen. Bitte nochmals versuchen");
	 
	
 } 
 
 function sendenMoeglich(){ bereitZumSenden=true; alert("Senden ist möglich");}
 function ErrorHandler(event){ alert("Fehler bei den Websockets "+event.data);}
 function Closing(event){ alert("Websockets closing "+event.code);}
 
 function empfange(message)
 { 
 	var text = message.data;
 	//alert("emfange Daten vom Server: "+text);
 	// Textfeld zur Ausgabe des Echos vom Server
 	var output=window.document.getElementById("ausgabetext");
 	// Bereich zur Darstellung aller angemeldeten Clients
 	var session=window.document.getElementById("sessionID"); 
 	
 	// es kommt ein JSON-Array mit der Liste aller Clients
 	if (text.charAt(0) == "[")
 	{       // JSON-String in ein Array wandeln
 		    var clientliste = JSON.parse(text);
 		    
 		    output="";
 		    // Liste aller Clients anzeigen
 	        for ( i=0; i<clientliste.length; i++)  output = output+"client "+i+":"+clientliste[i]+"<br/>";
 	            session.innerHTML=output;
    }
 	// es kommt ein Echo 
 	else 	output.value=text;
	
 } 