

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fhwgt.quiz.application.*;
import de.fhwgt.quiz.loader.FilesystemLoader;
import de.fhwgt.quiz.loader.LoaderException;



//@WebServlet("/Kataloge")
public class KatalogServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		//FilesystemLoader loader = new FilesystemLoader();
		//Quiz.getInstance().initCatalogLoader(loader);
		
		response.setContentType("text/plain");
		
		ArrayList<String> katalognamen = new ArrayList<String>();
		
		Iterator<Entry<String, Catalog>> iterator;
		try {
			iterator = Quiz.getInstance().getCatalogList().entrySet().iterator();
			
			while(iterator.hasNext()){
				katalognamen.add(iterator.next().getKey());
			}
		} catch (LoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.write(katalognamen.toString().replaceAll("[\\[\\]]*", ""));
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
