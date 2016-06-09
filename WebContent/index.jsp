<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="StyleSheet.css" />
		<script src="anmelden.js"></script>
    	<script src="bibliothek.js"></script>
		<title>Quiz</title>
	</head>
	<body>
		<div id="head">
        <div id="menubar">
        	<div class="menupoint" onclick="selected(0);"></div>
            <div class="menupoint" onclick="selected(1);"></div>
            <div class="menupoint" onclick="selected(2);"></div><br>
             <form method="post" name="servlet_init">
            	<b>Fragenkataloge:</b> <br>
            	<%
            		List<String> kataloge = (List<String>) request.getAttribute("kataloge");
            		if(kataloge.isEmpty()){
            	%>
            	<h2>Keine Fragenkataloge gefunden!</h2>
            	<% } else { %>
					<table>
					<%for (String katalogname : kataloge) { %>
						<tr><td><%= katalogname %></td></tr>
					<%} %>
					</table>
				<%} %>
				</select>
            </form>
        </div>
        <div id="punkte">
            <div id="anzeige">
                <b>Scoreboard:</b>
            </div>
            <div id="User"></div>
				<form method="post" name="servlet_init">
					<input type="text" name="player">
					<input type="submit" value="Anmelden">	
					<% 
						List<String> players = (List<String>) request.getAttribute("players");
						if(players.isEmpty()){
					%>
					<h2>Noch keine Spieler angemeldet.</h2>
					<% } else if(players.size() > 6) { %>
					<h2>Bereits 6 Spieler angemeldet.</h2>					
					<% } else { %>
					<table>
						<% for (String player : players) { %>
						<tr><td><%= player  %></td></tr>
						<% } %>
					</table>
					<% } %>
				</form>
            </div>
    </div>
    	<div id="quiz">
        	<div class =" hidden" id="Frage">Frage:</div> 
        	<div class="Antwort hidden">A</div> 
        	<div class="Antwort hidden">B</div> 
        	<div class="Antwort hidden">C</div> 
        	<div class="Antwort hidden">D</div> 
    	</div>
	</body>
</html>