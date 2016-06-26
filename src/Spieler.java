import java.io.IOException;
import java.util.TimerTask;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.json.simple.JSONObject;

import de.fhwgt.quiz.application.Player;
import de.fhwgt.quiz.application.Question;
import de.fhwgt.quiz.application.Quiz;
import de.fhwgt.quiz.error.QuizError;

public class Spieler {
	private Player player;
	private QuizError error;
	
	public Spieler(String name){
		error = new QuizError();
		player = Quiz.getInstance().createPlayer(name, error);
	}
	
	public void entferneSpieler(){
		QuizError error = new QuizError();
		Quiz.getInstance().removePlayer(player, error);
	}
	
	public void catalogChange(String catalogName){
		QuizError error = new QuizError();
		Quiz.getInstance().changeCatalog(player, catalogName, error);
	}
	
	public void startGame(){
		QuizError error = new QuizError();
		Quiz.getInstance().startGame(player, error);
	}
	
	public Question getQuestion(final Session session) {
		QuizError error = new QuizError();
		TimerTask timeoutTask = new TimerTask() {

			@SuppressWarnings("unchecked")
			public void run() {
				//runnable f. Timeout
				JSONObject message = new JSONObject();
				message.put("typ", 10);
				try {
					session.getBasicRemote().sendObject(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EncodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		Question question = Quiz.getInstance().requestQuestion(player, timeoutTask, error);

		//Fehlerbehandlung
		//checkOperationError(error);

		return question;
	}
}
