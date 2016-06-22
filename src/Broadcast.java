//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.websocket.Session;
//
//public class Broadcast extends Thread{
//		
//	public Broadcast (String message){
//		this.run(message);		
//	}
//	
//	public void run(String message){
//	Session s;
//		for (int i = 0; i < ConnectionManager.SessionCount(); i++) {
//			s =	ConnectionManager.getSession(i);
//			//Jan sag hier wird sogar ne NULL-Pointer Exception abgefangen
//			if(!(s instanceof Session)){
//				continue;
//			}
//			try {
//				s.getBasicRemote().sendText(message);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}	
//	}
//}
