
import java.nio.CharBuffer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import de.fhwgt.quiz.application.*;
import de.fhwgt.quiz.loader.FilesystemLoader;
import de.fhwgt.quiz.loader.LoaderException;


/**
 * 
 * Klasse für die erzeugung eines Session Objektes.
 * Besteht aus einer Session ID und einem Spieler Objekt.
 * @author Fabian
 *
 */
@ServerEndpoint("/Socket") 
public class Websocket {
		
	
	private Spieler spieler;
	private Session session;
	
	//Wird aufgerufen sobald ein Client auf den Socket zugreift
	//Wir erhalten die Session vom Client und speichern sie in unserem sessions set
	@OnOpen
	public void opened(Session session){
		System.out.print("\nSession Open! " + session);
		//ConnectionManager.socketliste.put(session, null);
	}
	
	//Wird aufgerufen sobald ein CLient nicht mehr auf den Socket zugreift
	//Wir erhalten die Session vom Client und löschen sie aus unserem sessions set
	@OnClose
	public void closed(Session session){
		System.out.print("\nSession Close: " + session);
		//ConnectionManager.socketliste.remove(session);
	}
	
	//Wird aufgerufen sobald unser Server eine Nachricht erhält
	@OnMessage	
	public void receiveText(String message) throws JSONException{
		JSONObject jObject = new JSONObject(message);
		int typ;
		
		System.out.print("\nText angekommen!");
		System.out.print("message: " + message);

		typ = (int) jObject.get("typ");
		
		rfc(typ, jObject);
	}
	
	public void rfc(int typ, JSONObject jObject) throws JSONException{
		Object data = jObject.get("data");
		
		switch(typ){
		case 1:
			String spielername = data.toString();

			spielername = spielername.replaceAll("<[^>]*>", "");
			
			if(spielername.isEmpty()) {
				System.out.print("\nSpieler bereits angemeldet!");
				break;
			}
			
			try {
				spieler = new Spieler(spielername);
				System.out.print("\nSpieler " + spielername);
			} catch (Exception e) {
				System.out.print("\nSpieler konnte nicht angemeldet werden!");
			}

			//Wenn Nutzer spaeter dazu kommt
			if(Quiz.getInstance().getCurrentCatalog() != null) {
				//sendCatalogChange();
			}
			break;
		
		//case 2:
		// RFC ToDo
		default:
			System.err.print("RFC-Typ unbekannt.");
			break;	
			
		}
	}
	
	
}
