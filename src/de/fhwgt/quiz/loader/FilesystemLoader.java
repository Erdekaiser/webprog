package de.fhwgt.quiz.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.fhwgt.quiz.application.Catalog;
import de.fhwgt.quiz.application.Question;

public class FilesystemLoader implements CatalogLoader {

    private File[] catalogDir;
    private final Map<String, Catalog> catalogs;
    private final String location;

    public FilesystemLoader(String location) {
    	catalogs = new HashMap<String, Catalog>();
        this.location = location;
    }

    @Override
    public Map<String, Catalog> getCatalogs() throws LoaderException {

        if (!catalogs.isEmpty()) {
            return catalogs;
        }

        // Construct URL for package location
        URL url = this.getClass().getClassLoader().getResource(location);

        File dir;
        try {
            // Make sure the Java package exists
            if (url != null) {
                dir = new File(url.toURI());
            } else {
                dir = new File("/");
            }
        } catch (URISyntaxException e) {
            // Try to load from the root of the classpath
            dir = new File("/");
        }

        // Add catalog files
        if (dir.exists() && dir.isDirectory()) {
            this.catalogDir = dir.listFiles(new CatalogFilter());
            for (File f : catalogDir) {
                catalogs.put(f.getName(),
                    new Catalog(f.getName(), new QuestionFileLoader(f)));
            }
        }

        return catalogs;
    }

    @Override
    public Catalog getCatalogByName(String name) throws LoaderException {
        if (catalogs.isEmpty()) {
            getCatalogs();
        }

        return this.catalogs.get(name);
    }

    /**
     * Filter class for selecting only files with a .XML extension.
     *
     * @author Simon Westphahl, edited by FH
     *
     */
    private class CatalogFilter implements FileFilter {

        /**
         * Accepts only files with a .XML extension.
         */
        @Override
        public boolean accept(File pathname) {
            if (pathname.isFile() && pathname.getName().endsWith(".xml"))
                return true;
            else
                return false;
        }

    }

    private class QuestionFileLoader implements QuestionLoader {

        private final File catalogFile;
        private final List <Question> questions = new ArrayList<Question>();
        Document doc = null;
		Question question;

        public QuestionFileLoader(File file) {
            catalogFile = file;
        }

        @Override
        public List<Question> getQuestions(Catalog catalog)
        	throws LoaderException{
        	
        	//Falls im Katalog Fragen vorhanden sind, gib fragen aus.
            if (!questions.isEmpty()) {
                return questions;
            }
        	          
            try {
    			// Das Dokument erstellen
    			SAXBuilder builder = new SAXBuilder();
    			doc = builder.build(catalogFile);
    			
    			//Fragenkatalog: Attribut: name
    			Element element = doc.getRootElement();
    			
    			List<Element> fragen = element.getChildren("frage");
    			List<Element> antworten = null;

    			for(int i = 0; i < fragen.size(); i++){
    				//Neues Fragen Objekt erzeugen
    				question = new Question(fragen.get(i).getChildText("fragestellung")); //Fragestellung der Frage hinzufügen
    				question.setTimeout(Long.parseLong(fragen.get(i).getChildText("timeout"))); //Timeout der Frage hinzufügen
    				
    				antworten = fragen.get(i).getChildren("antwort");
    				for(int j = 0; j < antworten.size(); j++){
    					if(fragen.get(i).getChildText("richtige_antwort") == antworten.get(j).getAttributeValue("ID")){
    						question.addAnswer(antworten.get(j).getText());
    					} else {
    						question.addBogusAnswer(antworten.get(j).getText());
    					}
    				}
    				
                    //Frage ist komplett wenn min 4 Fragen vorhanden sind sowie die richtige antwort vorhanden ist.
                    if (question.isComplete())
                        // Add some randomization
                        question.shuffleAnswers();
    					questions.add(question);

    			}

    		} catch (JDOMException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
			return questions;
        }

    }
}
