
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import de.fhwgt.quiz.application.*;

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
		this.session = session;
	}
	
	//Wird aufgerufen sobald ein CLient nicht mehr auf den Socket zugreift
	//Wir erhalten die Session vom Client und löschen sie aus unserem sessions set
	@OnClose
	public void closed(Session session){
		System.out.print("\nSession Close: " + session);
		ConnectionManager.getSocketliste().remove(this.session);
		
		if(spieler != null){
			spieler.entferneSpieler();
			spieler = null;
		}
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
		//Spieler meldet sich an
		case 1:
			String spielername = data.toString();

			spielername = spielername.replaceAll("<[^>]*>", "");
			
			if(spielername.isEmpty()) {
				System.out.print("\nSpieler bereits angemeldet!");
				break;
			}
			
			try {
				spieler = new Spieler(spielername);
				ConnectionManager.getSocketliste().put(session, spieler);
				System.out.print("\nSpieler " + spielername);
			} catch (Exception e) {
				System.out.print("\nSpieler konnte nicht angemeldet werden!");
				break;
			}

			//Wenn Nutzer spaeter dazu kommt
			if(Quiz.getInstance().getCurrentCatalog() != null) {
				sendCatalogChange();
			}
			break;
			
		//CatalogChange
		case 2:
			spieler.catalogChange((String)data);
			sendCatalogChange();
			break;
			
		//Starte Spiel
		case 3:
			spieler.startGame();
			sendStartGame();
			break;
			
		//QuestionRequest
		case 4:
			Question frage = spieler.getQuestion(this.session);
			if(frage != null){
				sendQuestion(frage);
			}
			//Letzte Frage? Spiel vorbei?
		default:
			System.err.print("RFC-Typ unbekannt.");
			break;	
			
		}
	}
	
	public static void broadcast(JSONObject message){
		for(Session session : ConnectionManager.getSocketliste().keySet()){
			try {
				session.getBasicRemote().sendObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendCatalogChange(){
		JSONObject changeCatalog = new JSONObject();
		try {
			changeCatalog.put("typ", 2);
			changeCatalog.put("data", Quiz.getInstance().getCurrentCatalog().getName());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		broadcast(changeCatalog);	
	}
	
	public void sendStartGame(){
		JSONObject message = new JSONObject();
		try {
			message.put("typ", 3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		broadcast(message);
	}
	
	public void sendQuestion(Question question){
		JSONObject message = new JSONObject();
		JSONObject jquestion = new JSONObject();
	
		try {
			jquestion.put("question", question.getQuestion());
			jquestion.put("answerliste", question.getAnswerList());
			message.put("date", jquestion.toString() + "\n\n");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		broadcast(message);
		
	}
}
