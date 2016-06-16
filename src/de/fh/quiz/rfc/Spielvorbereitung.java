package de.fh.quiz.rfc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import de.fhwgt.quiz.application.*;
import de.fhwgt.quiz.loader.FilesystemLoader;
import de.fhwgt.quiz.loader.LoaderException;


//Endpoint definieren, so kann vom Client der Socket angesprochen werden
@ServerEndpoint("/Socket") 
public class Spielvorbereitung {

	private Quiz quiz = Quiz.getInstance();
	
	
//	private FilesystemLoader loader;
//	private Map<String, Catalog> Kataloge;
//	private ArrayList<String> katalognamensliste;
//	
//	Spielvorbereitung(){
//		quiz.initCatalogLoader(loader);
//		try {
//			Kataloge = quiz.getCatalogList();
//		} catch (LoaderException e) {
//			e.printStackTrace();
//		}
//		katalognamensliste = new ArrayList<String>();
//		for(String key : Kataloge.keySet()) {
//			katalognamensliste.add(key);
//		}
//	}
		
		
	//Wird aufgerufen sobald ein Client auf den Socket zugreift
	//Wir erhalten die Session vom Client und speichern sie in unserem sessions set
	@OnOpen
	public void opened(Session session){
		ConnectionManager.socketliste.add(session);
	}
	
	//Wird aufgerufen sobald ein CLient nicht mehr auf den Socket zugreift
	//Wir erhalten die Session vom Client und löschen sie aus unserem sessions set
	@OnClose
	public void closed(Session session){
		ConnectionManager.socketliste.remove(session);
	}
	
	//Wird aufgerufen sobald unser Server eine Nachricht erhält
	@OnMessage	
	public void broadcast(String message){
		new Broadcast(message);
	}
	
	
}
