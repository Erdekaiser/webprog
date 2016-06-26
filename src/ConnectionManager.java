
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

// Verwaltet eine threadsichere Liste von Socket-Verbindungen
public class ConnectionManager 
{   
	private static final ConcurrentHashMap<Session, Spieler> socketliste = new ConcurrentHashMap<Session, Spieler>();
	
	public static ConcurrentHashMap<Session, Spieler> getSocketliste(){
		return socketliste;
	}
	
    // Anzahl der Verbindungen besorgen
    public  static synchronized int SessionCount() {
    	return socketliste.size();
    }
    

    
	// Liste für Web-Socket-Sessions
	//public static final Set<Session> socketliste = Collections.synchronizedSet(new HashSet<Session>());
	// Synchronisierte Zugriffe auf die Liste
	//public  static synchronized String outputAllSessions(){ return socketliste.toString(); }  
	// Verbindung an der Position i holen
   

    // Verbindung hinzufÃ¼gen
    //public  static synchronized void addSession(Session session) 
    //{ socketliste.add(session);    }
    // Verbindung entfernen
    //public  static synchronized void SessionRemove(Session session) { socketliste.remove(session);}
}
