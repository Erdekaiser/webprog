
document.addEventListener("DOMContentLoaded", init, false);

/**
 * Initialisiert alle, zu beginn nötigen, JavaFunktionen.
 * Wird aufgerufen sowie der komplette DOM-Baum der HTML-Datei geladen wurde. 
 */
function init(){
	initKataloge();
	initListener();
	initWebSocket();
}