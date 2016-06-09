package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.fhwgt.quiz.application.*;

@WebServlet("/servlet_init")
public class servlet_init extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	//Quiz benutzen!!!!!! Zwecks syncro.
	Spiel spiel = new Spiel();
	
	
	protected void anfrageVerarbeiten (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//HttpSession session = request.getSession();
		//session.setMaxInactiveInterval(10);
		
    	ArrayList<String> players = new ArrayList<String>();
    	ArrayList<String> kataloge = new ArrayList<String>();
    	
        String player = request.getParameter("player");
        if(players.size() < 6 && players.contains(player) == false){
        	spiel.setPlayer(player);
        }
        players = spiel.getPlayerNameList();
        request.setAttribute("players", players);

         
    	//Katalogliste aus Logik holen und an das request Objekt hängen
        kataloge = spiel.getKatalogNameList();
        request.setAttribute("kataloge", kataloge);
                
        
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
        requestDispatcher.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		anfrageVerarbeiten(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		anfrageVerarbeiten(request, response);
	}

}
