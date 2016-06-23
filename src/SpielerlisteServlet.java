import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.fhwgt.quiz.application.Player;
import de.fhwgt.quiz.application.Quiz;

@WebServlet("/SpielerlisteServlet")
public class SpielerlisteServlet  extends HttpServlet {

	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("pragma", "no-cache,no-store");  
		response.setHeader("cache-control", "no-cache,no-store,max-age=0,max-stale=0");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/event-stream");
		sendPlayerlist(response);

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	@SuppressWarnings("unchecked")
	private void sendPlayerlist(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();

		while(true){

			JSONArray plJSONArray 	= new JSONArray();
			Iterator<Player> iterator = Quiz.getInstance().getPlayerList().iterator();

			while(iterator.hasNext()) {
				Player player = iterator.next();
				JSONObject pJSONObject = new JSONObject();

				pJSONObject.put("name", 	player.getName());
				pJSONObject.put("score", 	player.getScore());
				plJSONArray.add(pJSONObject);
			}

			writer.write("event: Spielerliste\n");
			writer.write("data: " + plJSONArray.toString() + "\n\n");
			writer.flush();
			
			Quiz.getInstance().waitPlayerChange();
		}
	}

}