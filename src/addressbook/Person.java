package addressbook;

import java.io.Serializable;


public class Person implements Serializable, Comparable<Person> {
    //빌더 패턴[x]_실패.
	
	//필수 항목 : 이름, 전화번호
	protected String name;
	protected String tel;
	//선택 항목 : 나이, 주소
	protected String age = "*비어있음";
	protected String addr= "*비어있음";
	
	
	public Person(String name , String tel, String age, String addr) {
		
		
		this.name = name;
		this.tel = tel;
		if(!age.isEmpty()) 
		this.age = age;
		if(!addr.isEmpty()) 
		this.addr = addr;
		
		
	}
	public int compareTo(Person target) {
			
		return this.name.compareTo(target.name);
	
		
	}	
	

	@Override
	public String toString() {
		return  String.format("이름 :%-15s 연락처 :%-15s 나이 :%-15s 주소 :%-10s", name,tel,age,addr);
	}
	
	//txt형식 내보내기 할 때 일부만 내보내기 하기 위해서. 메소드 하나 생성.
	public String toString2() {
		return  String.format("이름 :%-12s 연락처 :%-12s ", name,tel);
	}



}//class person 
