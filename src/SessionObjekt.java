
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import de.fhwgt.quiz.application.*;
import de.fhwgt.quiz.loader.FilesystemLoader;
import de.fhwgt.quiz.loader.LoaderException;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSONObject;


/**
 * 
 * Klasse für die erzeugung eines Session Objektes.
 * Besteht aus einer Session ID und einem Spieler Objekt.
 * @author Fabian
 *
 */
@ServerEndpoint("/Socket") 
public class SessionObjekt {
		
	
	private Spieler spieler;
	private Session session;
	
	//Wird aufgerufen sobald ein Client auf den Socket zugreift
	//Wir erhalten die Session vom Client und speichern sie in unserem sessions set
	@OnOpen
	public void opened(Session session){
		System.out.print("Session Open!");
		//ConnectionManager.socketliste.put(session, null);
	}
	
	//Wird aufgerufen sobald ein CLient nicht mehr auf den Socket zugreift
	//Wir erhalten die Session vom Client und löschen sie aus unserem sessions set
	@OnClose
	public void closed(Session session){
		System.out.print("Session Close!");
		//ConnectionManager.socketliste.remove(session);
	}
	
	//Wird aufgerufen sobald unser Server eine Nachricht erhält
	@OnMessage	
	public void receiveText(String message){
		JSONObject recvJSONObject = null;
		int typ;
		
		recvJSONObject = (JSONObject) new JSONParser(message, null, false).parse();
		
		typ = (int) recvJSONObject.get("typ");
		
		rfc(typ, recvJSONObject);
	}
	
	public void rfc(int typ, JSONObject recvJSONObject){
		Object recvData = recvJSONObject.get("data");
		
		switch(typ){
		case 1:
			String playername = recvData.toString();
		
		
		}
	}
	
	
}
