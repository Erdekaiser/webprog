
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
		ConnectionManager.SessionRemove(session);
		
		if(spieler != null){
			spieler.entferneSpieler();
			spieler = null;
		}
	}
	
	//Wird aufgerufen sobald unser Server eine Nachricht erhält
	@OnMessage	
	public void receiveText(String message) throws JSONException, IOException, EncodeException{
		JSONObject jObject = new JSONObject(message);
		int typ;
		
		typ = (int) jObject.get("typ");
		
		rfc(typ, jObject);
	}
	
	public void rfc(int typ, JSONObject jObject) throws JSONException, IOException, EncodeException{
		Object data = jObject.get("data");
		
		switch(typ){
		//Spieler meldet sich an
		case 1:
			System.out.print("\nMessage Typ 1 [Login] angekommen!");
			String spielername = data.toString();

			spielername = spielername.replaceAll("<[^>]*>", "");
			
			if(spielername.isEmpty()) {
				System.out.print("\nSpieler bereits angemeldet!");
				break;
			}
			
			try {
				spieler = new Spieler(spielername);
				ConnectionManager.addSession(session, spieler);
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
			System.out.print("\nMessage Typ 2 angekommen: ");
			spieler.catalogChange((String)data);
			sendCatalogChange();
			break;
			
		//Starte Spiel
		case 3:
			System.out.print("\nMessage Typ 3 angekommen: ");
			spieler.startGame();
			System.out.print(session);
			sendStartGame();
			break;
			
		//QuestionRequest
		case 4:
			System.out.print("\nMessage Typ 4 [QuestionRequest] angekommen!");
			Question frage = spieler.getQuestion(this.session);
			if(frage != null){
				sendQuestion(frage);
				}else{
					if(spieler.setGameOver()){
						JSONObject gameOver = new JSONObject();
						gameOver.put("typ", 8);
						session.getBasicRemote().sendObject(gameOver.toString());
						System.out.print(gameOver + " versendet!");
					}else{
						JSONObject gameOverAll = new JSONObject();
						gameOverAll.put("typ", 9);
						broadcast(gameOverAll);
						System.out.print(gameOverAll + " versendet!");
					}
			}
			break;
		
		//QuestionAnswered
		case 6:
			System.out.print("\nMessage Typ 6 [QuestionAnswered] angekommen!");
			sendQuestionResult((long) data, spieler.setAnswer((long) data));
			break;
		//ErrorWarning	
		case 255:
			sendErrorWarning((String) data);
			break;
			
		default:
			System.err.print("RFC-Typ unbekannt.");
			break;
		}
	}
		
	public void broadcast(JSONObject message){
		for(Session session : ConnectionManager.getSocketliste().keySet()){
			System.out.print(session);
			session.getAsyncRemote().sendObject(message.toString());
			System.out.print("\nBROADCAST: " + message + " an " + session + " versendet!");
		}
	}
	
	private void sendCatalogChange() throws JSONException{
		JSONObject changeCatalog = new JSONObject();

		changeCatalog.put("typ", 2);
		changeCatalog.put("data", Quiz.getInstance().getCurrentCatalog().getName());
		System.out.print("\nMessage Typ 2 [CatalogChange] vorbereitet: ");

		broadcast(changeCatalog);
		System.out.print(changeCatalog + " versendet!");
	}
	
	private void sendStartGame() throws JSONException{
		JSONObject message = new JSONObject();

		message.put("typ", 3);

		broadcast(message);
		System.out.print(message + " versendet!");
	}
	
	private void sendQuestion(Question question) throws JSONException{
		JSONObject message = new JSONObject();
		JSONObject jquestion = new JSONObject();
				
		message.put("typ", 5);
		System.out.print("\nMessage Typ 5 [Question] vorbereitet: ");
		jquestion.put("question", question.getQuestion());
		jquestion.put("answerliste", question.getAnswerList());
		message.put("data", jquestion.toString() + "\n\n");
			
		broadcast(message);
		System.out.print(message + " versendet!");
	}
	
	private void sendQuestionResult(long gewaehlt, long richtig) throws JSONException, IOException, EncodeException{
		JSONObject message = new JSONObject();
		JSONObject janswer = new JSONObject();
		
		message.put("typ", 7);
		System.out.print("\nMessage Typ 7 [QuestionResult] vorbereitet: ");
		janswer.put("selected", gewaehlt);
		janswer.put("correct", richtig);
		
		message.put("data", janswer.toString() + "\n\n");
		
		session.getBasicRemote().sendObject(message.toString());
		System.out.print(message + " versendet!");
	}
	
	private void sendErrorWarning(String message) throws JSONException{
		broadcast(createError(message));
		System.out.print(message + " versendet!");
	}
	
	private void sendErrorWarning(String message, Session session) throws IOException, EncodeException, JSONException{
		session.getBasicRemote().sendObject(createError(message.toString()));
		System.out.print(message + " versendet!");
	}
	
	private JSONObject createError(String error) throws JSONException{
		int delimiterPosition = error.indexOf(".");
		long type = Long.parseLong(error.substring(0, delimiterPosition));
		String message = error.substring(delimiterPosition+1, error.length());
		
		JSONObject jerror = new JSONObject();
		
		jerror.put("typ", 255);
		jerror.put("data", message);
		jerror.put("err", type);
		System.out.print("\nMessage Typ 255 [ErrorWarning] vorbereitet: ");
		return jerror;
	}
}
