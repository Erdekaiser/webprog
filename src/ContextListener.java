

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.fhwgt.quiz.application.Quiz;
import de.fhwgt.quiz.loader.FilesystemLoader;

public class ContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// derzeit unbenutzt
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			FilesystemLoader loader = new FilesystemLoader();
			Quiz.getInstance().initCatalogLoader(loader);			
		}catch(Exception e){
			System.err.print("Fehler im FilesystemLoader!");
			e.printStackTrace();
		}
	}

}
