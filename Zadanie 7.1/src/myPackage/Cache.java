package myPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Cache {

	private Map<String, List<EntityObject>> items;  // hash-mapa, która wi¹¿e klucz(String) z listami obiektów
	private Date refreshDate;	// data ostatniej aktualizacji danych
	private static Cache instance;		// referencja wskazuj¹ca na instancjê obiektu klasy Cache
	private static Object token = new Object();		// token u¿yty w zsynchronizowanym tworzeniu obiektu klasy
													// Cashe. (patrz ni¿ej)
	
	
	// prywatny konstruktor, w którym tworzymy hash-mapê i ustawiamy aktualn¹ datê za datê ostatniego update'u
	private Cache() {
		items = new HashMap<String, List<EntityObject>>();
		refreshDate = new Date();
	}
	
	
	/*
	 * Metoda, która tworzy instancjê obiektu, czyli pozwala siê "dobraæ" do klasy pomimo, ¿e jej 
	 * konstruktor jest prywatny. Przydaje siê to w momencie, gdy chcemy, aby tylko jeden obiekt istnia³
	 * w jednym czasie. S³u¿y do tego instrukcja "synchronized(token){/*tu wpisujemy instrukcje, które
	 * chcemy, aby siê wykona³y tylko jeden raz w danym czasie* /}"  
	 */
	public static Cache getInstance(){
		if(instance == null)
			synchronized (token) {
				
				if(instance==null)
					instance = new Cache();
			}
		return instance;
	}
	
	
	// dodajemy pozycjê do hashmapy
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
			
			
	// zwraca listê obiektów znajduj¹cych siê w hashmapie		
	public List<EntityObject> getAll(){
		List<EntityObject> result = new ArrayList<EntityObject>();
		for(String name:items.keySet()){
			List<EntityObject> tmp = items.get(name);
			result.addAll(tmp);
		}
		return result;
	}
	
	
	// odœwie¿a datê ostatniej aktualizacji
	public void refresh(){
		Date newDate = new Date();
		refreshDate = new Date(
				newDate.getDay() +1,
				newDate.getMonth(),
				newDate.getYear());
	}
	
	
	
	// usuwa pozycjê z mapy za pomoc¹ klucza i id
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
	
	
	// pobiera jedn¹ lub wiêcej pozycji z mapy, ktore pasuj¹ to klucza (name)
	public List<EntityObject> getByName(String name) {
		return items.get(name);
	}
	
	
	//czyœci ca³¹ mapê
	public void clear(){
		items.clear();
	}
	
}
