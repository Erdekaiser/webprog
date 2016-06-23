
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

// Verwaltet eine threadsichere Liste von Socket-Verbindungen
public class ConnectionManager 
{   
	public static final ConcurrentHashMap<Session, Spieler> socketliste = new ConcurrentHashMap<Session, Spieler>();
	// Liste für Web-Socket-Sessions
	//public static final Set<Session> socketliste = Collections.synchronizedSet(new HashSet<Session>());
	// Synchronisierte Zugriffe auf die Liste
	//public  static synchronized String outputAllSessions(){ return socketliste.toString(); }  
	// Verbindung an der Position i holen
    //public  static synchronized Session getSession(int i) { return socketliste.get(i);}
    // Anzahl der Verbindungen besorgen
    //public  static synchronized int SessionCount() { return socketliste.size();}
    // Verbindung hinzufÃ¼gen
    //public  static synchronized void addSession(Session session) 
    //{ socketliste.add(session);    }
    // Verbindung entfernen
    //public  static synchronized void SessionRemove(Session session) { socketliste.remove(session);}
}
