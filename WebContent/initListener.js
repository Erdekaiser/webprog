function initListener(){
	var anmelden = document.getElementById("anmelden");
	anmelden.addEventListener('click', anmeldenclick, false);
	
	var starten = document.getElementById("starten");
	starten.addEventListener('click', startenclick, false);
	
	
	
	spielerliste = new EventSource("SpielerlisteServlet");
	spielerliste.addEventListener('Spielerliste', spielerlistelistener, false);
}
