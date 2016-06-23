function initListener(){
	var anmelden = document.getElementById("anmelden");
	anmelden.addEventListener('click', anmeldenclick, false);
		
	spielerliste = new EventSource("SpielerlisteServlet");
	spielerliste.addEventListener('Spielerliste', spielerlistelistener, false);
}
