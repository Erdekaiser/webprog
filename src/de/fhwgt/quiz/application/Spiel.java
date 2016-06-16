package de.fhwgt.quiz.application;

import de.fhwgt.quiz.loader.*;
import de.fhwgt.quiz.application.*;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Spiel {
		private String XmlVerzeichnis = "/xml-dateien";
		private ArrayList<Player> Spielerliste;
		private ArrayList<String> spielernamensliste;
		private FilesystemLoader loader;
		private ArrayList<String> katalognamensliste;
		private Map<String, Catalog> Kataloge;
		
		
		public Spiel(){
			Spielerliste = new ArrayList<Player>();
			Kataloge = new HashMap<String, Catalog>();
			loader = new FilesystemLoader();
		}
		
		
		public ArrayList<String> getKatalogNameList(){
			loadCatalogs();
			katalognamensliste = new ArrayList<String>();
			for(String key : Kataloge.keySet()) {
				katalognamensliste.add(key);
			}
			return katalognamensliste;			
		}
		
		public ArrayList<String> getPlayerNameList(){
			spielernamensliste = new ArrayList<String>();
			for(Player Temp : Spielerliste){
				if(Temp.getName() != null){
					spielernamensliste.add(Temp.getName());
				}
			}			
			return spielernamensliste;
		}
		
		public void loadCatalogs(){
			try{
				Kataloge = loader.getCatalogs();
				}catch (Exception e){
					System.out.println(e.getMessage());
				}
		}	
		
		public void setPlayer(String name){
			Spielerliste.add(new Player(name));
		}
		
		public ArrayList<Player> getPlayerlist(){
			return Spielerliste;
		}
		
		public FilesystemLoader getFSLoader(){
			return this.loader;
		}
		
		public static void main(String[] args){
			new Spiel();
		}
}
