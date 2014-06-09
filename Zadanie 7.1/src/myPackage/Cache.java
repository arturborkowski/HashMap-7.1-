package myPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Cache {

	private Map<String, List<EntityObject>> items;  // hash-mapa, kt�ra wi��e klucz(String) z listami obiekt�w
	private Date refreshDate;	// data ostatniej aktualizacji danych
	private static Cache instance;		// referencja wskazuj�ca na instancj� obiektu klasy Cache
	private static Object token = new Object();		// token u�yty w zsynchronizowanym tworzeniu obiektu klasy
													// Cashe. (patrz ni�ej)
	
	
	// prywatny konstruktor, w kt�rym tworzymy hash-map� i ustawiamy aktualn� dat� za dat� ostatniego update'u
	private Cache() {
		items = new HashMap<String, List<EntityObject>>();
		refreshDate = new Date();
	}
	
	
	/*
	 * Metoda, kt�ra tworzy instancj� obiektu, czyli pozwala si� "dobra�" do klasy pomimo, �e jej 
	 * konstruktor jest prywatny. Przydaje si� to w momencie, gdy chcemy, aby tylko jeden obiekt istnia�
	 * w jednym czasie. S�u�y do tego instrukcja "synchronized(token){/*tu wpisujemy instrukcje, kt�re
	 * chcemy, aby si� wykona�y tylko jeden raz w danym czasie* /}"  
	 */
	public static Cache getInstance(){
		if(instance == null)
			synchronized (token) {
				
				if(instance==null)
					instance = new Cache();
			}
		return instance;
	}
	
	
	// dodajemy pozycj� do hashmapy
	public void addItem(String name, EntityObject entity){
		
		List<EntityObject>  tmpitems = items.get(name);
		if(tmpitems == null){
			tmpitems = new ArrayList<EntityObject>();
			items.put(name,  tmpitems);
		}
		tmpitems.add(entity);			
	}
	
	
	// zwraca wybrany obiekt. Wymaga klucza i id
	public EntityObject get(String name, int id){
			if(refreshDate.before(new Date())) refresh();
			List<EntityObject> tmpitems = items.get(name);
			if(tmpitems == null) return null;
			for(EntityObject item: tmpitems) {
				if(item.getId() == id)
					return item;
			}
			return null;
				
	}
			
			
	// zwraca list� obiekt�w znajduj�cych si� w hashmapie		
	public List<EntityObject> getAll(){
		List<EntityObject> result = new ArrayList<EntityObject>();
		for(String name:items.keySet()){
			List<EntityObject> tmp = items.get(name);
			result.addAll(tmp);
		}
		return result;
	}
	
	
	// od�wie�a dat� ostatniej aktualizacji
	public void refresh(){
		Date newDate = new Date();
		refreshDate = new Date(
				newDate.getDay() +1,
				newDate.getMonth(),
				newDate.getYear());
	}
	
	
	
	// usuwa pozycj� z mapy za pomoc� klucza i id
	public void remove(String name, int id){
		List<EntityObject>  tmpitems = items.get(name);
		if(tmpitems == null) return;
		for(EntityObject item:tmpitems){
			if(item.getId() == id) {
				tmpitems.remove(item);
				break;
			}
		}
	}
	
	
	// pobiera jedn� lub wi�cej pozycji z mapy, ktore pasuj� to klucza (name)
	public List<EntityObject> getByName(String name) {
		return items.get(name);
	}
	
	
	//czy�ci ca�� map�
	public void clear(){
		items.clear();
	}
	
}
