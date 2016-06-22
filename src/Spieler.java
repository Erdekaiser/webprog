import de.fhwgt.quiz.application.Player;
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
}
