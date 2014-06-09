package myPackage;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Person p1 = new Person();
		p1.setId(1);
		Person p2 = new Person();
		p2.setId(2);
		Person p3 = new Person();
		p3.setId(3);
		Person p4 = new Person();
		p4.setId(4);
		Person p5 = new Person();
		p5.setId(5);
		
		Cache cache = Cache.getInstance();
	
		
		cache.addItem("osoby1", p1);
		cache.addItem("osoby1", p2);
		cache.addItem("osoby2", p3);
		cache.addItem("osoby2", p4);
		cache.addItem("osoby2", p5);
		
		
		for(EntityObject obj: cache.getByName("osoby2"))
		{
			System.out.println(obj.getId());
		}
		
	}

}
