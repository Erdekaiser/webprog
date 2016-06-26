
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

// Verwaltet eine threadsichere Liste von Socket-Verbindungen
public class ConnectionManager 
{   
	private static final ConcurrentHashMap<Session, Spieler> socketliste = new ConcurrentHashMap<Session, Spieler>();
	
	public static ConcurrentHashMap<Session, Spieler> getSocketliste(){
		return socketliste;
	}
	    
    //Verbindung hinzufügen
    public  static synchronized void addSession(Session session, Spieler spieler) {
    	socketliste.put(session, spieler);
    }
	
    // Verbindung entfernen
    public  static synchronized void SessionRemove(Session session) {
    		socketliste.remove(session);
    }
}
