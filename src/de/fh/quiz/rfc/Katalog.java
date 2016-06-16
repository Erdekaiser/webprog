package de.fh.quiz.rfc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fhwgt.quiz.application.*;
import de.fhwgt.quiz.loader.FilesystemLoader;
import de.fhwgt.quiz.loader.LoaderException;

import org.json.*;

@WebServlet("/Katalog")
public class Katalog extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private Quiz quiz;
	private FilesystemLoader loader;
	private Map<String, Catalog> Kataloge;
	private ArrayList<String> katalognamensliste;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		quiz = quiz.getInstance();
		//loader objekt null, muss initalisiert werden
		quiz.initCatalogLoader(loader);
		try {
			Kataloge = quiz.getCatalogList();
		} catch (LoaderException e) {
			e.printStackTrace();
		}
		katalognamensliste = new ArrayList<String>();
		for(String key : Kataloge.keySet()) {
			katalognamensliste.add(key);
		}
		JSONArray jsonArray = new JSONArray(katalognamensliste);
		//ist nix!!
		String xml = XML.toString(jsonArray);
		
		PrintWriter writer = response.getWriter();
		writer.print(xml);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
