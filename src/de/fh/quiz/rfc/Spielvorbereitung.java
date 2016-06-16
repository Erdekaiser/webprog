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
	private FilesystemLoader loader;
	private Map<String, Catalog> Kataloge;
	private ArrayList<String> katalognamensliste;
	
	Spielvorbereitung(){
		quiz.initCatalogLoader(loader);
		try {
			Kataloge = quiz.getCatalogList();
		} catch (LoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		katalognamensliste = new ArrayList<String>();
		for(String key : Kataloge.keySet()) {
			katalognamensliste.add(key);
		}
	}
		
	//In sessions werden alle Sessions gehalten welche von Clients ge�ffnet werden
	private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
		
	//Wird aufgerufen sobald ein Client auf den Socket zugreift
	//Wir erhalten die Session vom Client und speichern sie in unserem sessions set
	@OnOpen
	public void opened(Session session){
		sessions.add(session);
	}
	
	//Wird aufgerufen sobald ein CLient nicht mehr auf den Socket zugreift
	//Wir erhalten die Session vom Client und l�schen sie aus unserem sessions set
	@OnClose
	public void closed(Session session){
		sessions.remove(session);
	}
	
	//Wird aufgerufen sobald unser Server eine Nachricht erh�lt
	@OnMessage
	public void kataloge(Session session, String message){
		
		for(Session client : sessions){
			//Nachricht wird asyncron versendet
			client.getAsyncRemote().sendText(message);
		}
	}
}
