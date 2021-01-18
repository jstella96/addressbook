package addressbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.sampled.AudioInputStream;

public class AddressbookLogic {

	
	
	////<주소록 메인 부분_로직/메소드 >////
	
	//0]시작과 동시에 선언할 맵,리스트.
	Map<Character,List<Person>> initiaMap = new TreeMap<Character, List<Person>>();
	List<Person> personList = null;
	
	Scanner sc = new Scanner(System.in);
	
	//0]생성자_파일 불러오기
	public AddressbookLogic(String id){
		categoryfilecall(id);
		filecall(id);
	}
	
	//0]저장되어있는  연락처 갯수 구하기
	public int numberOfPerson() {
		int i = 0;
		Set<Character> keys = initiaMap.keySet();
		for(Character k : keys) {
			List<Person> values = initiaMap.get(k);
			Collections.sort(values);
			for(Person value : values) {
				i ++; // 오버라이딩. 	
			}
		}
		return i;
	}
	
	
	
	//메인 메소드 1] 메뉴 출력
	public void printMainMenu(String songName, String dataname, int minute) {
		
		//int 로 숫자 받아 온다. 
		//우선 
	
		String autosavestate = "OFF";
		
		if(minute == 0)
			autosavestate = "OFF";
		else 
			autosavestate = String.format("ON, %d분",minute) ;
		
			
		
		
		
		if(dataname.equals("1970년 01월01일 09시00분"))
			dataname= "";
		
		System.out.println("===========================메인 메뉴==========================");
		System.out.printf("|현재 재생중인 노래 : %-30s   ",songName);
		System.out.printf("\r\n|마지막 저장 시간    : %-20s     |자동저장 : %s",dataname,autosavestate);
		System.out.printf("\r\n|저장 된 연락처       : %d명	  |생성된 카테고리    : %d개 \r\n",numberOfPerson(),categoryMap.size());
		System.out.println("\r\n");
		System.out.println("1. 입력	 	    2. 전체출력		3. 검색[수정/삭제]\r\n");
		System.out.println("4. 카테고리 	    5. 노래변경		6. 노래종료\r\n");
		System.out.println("7. 상태 저장	    8. 자동저장 관리		9. 마이페이지		\r\n	");
		System.out.println("10. 연락처 txt형식으로 내보내기			11.연락처 불러오기\r\n");
		System.out.println("0. 프로그램 종료\r\n");
		System.out.println("");
		System.out.println("===========================================================");
		System.out.println("메인 메뉴 번호를 입력하세요.");
	} // printMainMenu()
	
	
	////메인 메소드 2] 연락처 입력
	public void setPerson() {
		String name;
		String tel;
		while(true){
		System.out.println("이름을 입력하세요.(필수)");
		name = sc.nextLine();
		if(name.isEmpty()) {
			System.out.println("빈칸은 입력되지 않아요.");
			continue;
		}
		char keyset = getFirstCharacter(name);
		if(keyset == '0') {
			System.out.println("이름은 한글과 영어로 입력해야합니다.");
			continue;
		}
		
		System.out.println("전화번호를 입력하세요.(필수)");
		tel = sc.nextLine();
		if(name.isEmpty() || tel.isEmpty()) {
			System.out.println("이름과 전화번호는 '필수'입력사항 입니다.");
			continue;
		}
		break;
		}
		System.out.println("나이를 입력하세요.(선택)");
		String age= sc.nextLine();
		System.out.println("주소를 입력하세요.(선택)");
		String addr= sc.nextLine();
	
		char key;
		key = getFirstCharacter(name);
		if(!initiaMap.containsKey(key)) { 
			personList = new ArrayList<Person>();	
		}
		else { 
			personList = initiaMap.get(key);		
		}
		
		personList.add(new Person(name,tel,age,addr));
		initiaMap.put(key,personList);
		System.out.printf("'%s'의 연락처가 저장되었습니다.\r\n",name);
		System.out.println();
		//전화번호는 중복저장이 안된다. 이거 추가하기. 
		
	}//setPerson()
	
	
	//메인 메소드 3] 전체 출력
	public void printPerson() {
		
		
		Set<Character> keys = initiaMap.keySet();
		for(Character k : keys) {
			System.out.println("\r\n'"+k+"'로 시작하는 명단");
			List<Person> values = initiaMap.get(k);
			Collections.sort(values);
			for(Person value : values) {
				System.out.print("        ");
				System.out.println(value); // 오버라이딩. 	
			}
		}
		System.out.println();System.out.println();
	}
	
	//메인 메소드 4] 검색(수정/삭제) _ 메소드가 매우 길다.추후에  수정, 검색 메소드로 따로 처리하기.
	public void searchPerson() {	
		
		char key;
		List<Person> values2 = new ArrayList<Person>(); // 일괄 이름 바꾸기. 
		List<Person> correctPersonList = new ArrayList<Person>();	
		values2=null;
		System.out.println("검색 할 이름을 입력해주세요, 메뉴로돌아가려면 '나가기!'를 입력하세요");
		while(true) {
			
			String searchName = sc.nextLine();
			if(searchName.isEmpty()) {
					System.out.println("빈칸은 입력되지 않아요.");
					continue;
			}
			key = getFirstCharacter(searchName);
			if(searchName.equals("나가기!")) 
				break;
			if(key == '0') {
				System.out.println("한글 혹은 영어로 입력해주세요.메뉴로돌아가려면 '나가기!'를 입력하세요");
				continue;
			}
			
			
			if(initiaMap.containsKey(key)) {
				values2 = initiaMap.get(key);
				for(Person value : values2) {
					if(value.name.length() >= searchName.length()) {
						String frontname = getFrontString(searchName, value.name);
							if(searchName.equals(frontname)) {
								correctPersonList.add(value); //수정 검색용 이다.
							}
						}
					}
				}//해당이름 옮겨담기 완료.
				
				
			if(correctPersonList.size()!=0)
				break;
			
			System.out.println("그런 이름은 없어요.다시입력하세요. ");
			
		}//while//검색끝나고 출력. 
		
		
		
		//출력하기
		for(Person person1 : correctPersonList) {
			System.out.printf("%2d. %s  \r\n",correctPersonList.indexOf(person1)+1,person1); 
		}
		
		
		//검색이 끝났다. 검색한 이름으로 수정or삭제
		String menuNember;
		
			System.out.println("-------------------------------------------------");
			System.out.println("| 수정  |  삭제   |  메뉴   |  *원하시는 메뉴를 한글로 입력하세요	 ");
			System.out.println("-------------------------------------------------");
			while(true) {
			menuNember = sc.nextLine();
			if((menuNember.equals("수정")||menuNember.equals("삭제")||menuNember.equals("메뉴"))) {
				break;
			}
			System.out.println("메뉴에 없는 문항입니다. 다시입력해주세요. ");
		}
		
		//수정
		if(menuNember.equals("수정")) {
			
			System.out.println("'수정'에 들어왔습니다. ");
			System.out.println("목록에서 수정 번호를 입력하세요.");
			int num;
			
			while(true) {
				try {
				num = Integer.parseInt(sc.nextLine());
					if(num <= correctPersonList.size())
						break;
					else 
						System.out.println("메뉴에 있는 숫자만 입력해주세요.");
				}catch (Exception e) {
					System.out.println("숫자만 입력해주세요");
				}
			}
			for(Person value : values2) {
				if(value.tel == correctPersonList.get(num-1).tel) {
					
					System.out.println("수정할 연락처를 입력하세요");
					value.tel = sc.nextLine();
					System.out.println("수정할 나이를 입력하세요");
					value.age = sc.nextLine();
					System.out.println("수정할 주소를 입력하세요");
					value.addr = sc.nextLine();

				}
				
			}
			
		initiaMap.put(key,values2);
		System.out.println(correctPersonList.get(num-1).name+"의 정보가 수정되었습니다.");
		}
		
		
		//삭제
		else if(menuNember.equals("삭제")) {// 같은 이름 처리. 
			
			System.out.println("삭제에 들어왔습니다. ");
			System.out.println("삭제할 번호를 입력하세요.");
			int num;
			while(true) {
				try {
				num = Integer.parseInt(sc.nextLine());
					if(num <= correctPersonList.size())
						break;
					else 
						System.out.println("메뉴에 있는 숫자만 입력해주세요.");
				}catch (Exception e) {
					System.out.println("숫자만 입력해주세요");
				}
			}
			
			int i=0;
			for(Person value : values2) {
				if(value.tel == correctPersonList.get(num-1).tel) {
					System.out.printf("이름 : %-5s 연락처: %-11s 의 정보를 삭제하였습니다.\r\n",value.name,value.tel);
					 i = values2.indexOf(value);
				}
			}
			values2.remove(i);
			initiaMap.put(key,values2);	
		}		

	}//searchPerson() 메소드 끝
		
	
	public int myPage(String name, String id) {
	System.out.println("---------------------------------");

	System.out.println("	  [마이페이지]");
	System.out.println();
	System.out.printf("       이름     : %-20s \r\n",name);
	System.out.printf("       아이디   : %-20s \r\n",id);
	System.out.println();
	System.out.println("  1. 패스워드 변경	 2.탈퇴	 3.나가기" );
	System.out.println();
	System.out.println("---------------------------------");
	
	int num=0;
	System.out.println("원하는 번호의 메뉴를 입력하세요.");
	Scanner sc = new Scanner(System.in);
	while(true) {
	try {
		num = Integer.parseInt(sc.nextLine());
	} catch (Exception e) {
		System.out.println("숫자만입력하세요");
	}
	if(num <= 3) break;
	else System.out.println("존재하는 번호만 입력하세요.");
	
	}return num;
	}
	
	//메인 메소드 5] 파일 저장
	public void filesave(String id) {
		
	
		categoryfilesave(id);
		if(initiaMap.isEmpty()) {
			System.out.println("파일로 저장할 사람이 없어요");
			return;
		}
		
		String fileName = String.format("src/filesave/member/%s/address.dat",id);
		
		ObjectOutputStream out=null;
		try {
			out=new ObjectOutputStream(
					new FileOutputStream(fileName));
			
			out.writeObject(initiaMap);
			System.out.println("* 현재 상태 저장 완료!!!");
			
			
		}
		catch(IOException e) {
			System.out.println("파일 저장시 오류:"+e.getMessage());
		}
		finally {
			
				try {
					if(out !=null) out.close();
				}catch (IOException e) {}
		}
		

		
	}
		
	//메인 메소드 6] 파일 자동 불러오기
	public void filecall(String id) {
		
		String fileName = String.format("src/filesave/member/%s/address.dat",id);

		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fileName));
			initiaMap = (Map<Character,List<Person>>)ois.readObject();
			ois.close();
		}
		catch(Exception  e) {
			//System.out.println("파일이 존재하지 않아요:"+e.getMessage());
		}
	
	}//filecall() 
	
	
	//매인 메소드 7] 파일 txt로 구분해서 내보내기
	public void fileTxtOutput(boolean c,String id) { 
		//인트값 받아와서 2개로 내보내자.
		
		
		String filename = String.format("src/addressbook_txt/%s.txt",id);
		StringBuffer address = new StringBuffer() ;
		PrintWriter pw = null;
		// PrintWriter 을 사용한건 다양한 메서드 이용하려고, 여기서는 println. 중간에 buffer 스트림을 이용했으면 더 빠른 코드 생성 가능.
		try {
			pw = new PrintWriter(new FileWriter(filename),true);
		

		} catch (IOException e) {
			
			e.printStackTrace();
		}
		

		Set<Character> keys = initiaMap.keySet();
		for(Character k : keys) {
			List<Person> values = initiaMap.get(k);
			for(Person value : values) {
				  if(c){
					  
					  address.append(value.toString()); 
					  address.append("\r\n");
				  } 
				  else {
					  
					  address.append(value.toString2());
					  address.append("\r\n");
				  }
			}
		}
		
		pw.println(address);
		pw.close();	
		
		
		if(c) System.out.println("모든내용 저장이 완료되었습니다.");
		else System.out.println("이름과 전화번호 저장이 완료되었습니다.");
	}
	
	//매인 메소드 8] txt파일 가져와서 저장하기.
	public void FileTxtInput() {
		
		String idPath;
		
		System.out.println("받아오고 싶은 txt파일을 가진 'id'를 입력해 주세요. 기존에 저장된 파일이 있어야 실행 됩니다.");
		//아이디 받고, 파일 존재 확인. 
		while(true) {
		String callId = "";
		Scanner sc = new Scanner(System.in);
		callId = sc.nextLine();
		if(callId == "나가기!") {
			return;
		}
		idPath = String.format("src/addressbook_txt/%s_All.txt",callId);
		File fre = new File(idPath);
        if (!fre.isFile()) {
        	System.out.println("정보가 존재하지 않습니다. 메뉴로 돌아가시려면 나가기!를 입력해주세요");
        	}
        else {
        	break;
        	}
        
		}//while 
		
		int z=0;
		int i=0;
		StringBuffer txtLine = new StringBuffer();
	
	
		try {
			FileReader fr = new FileReader(idPath);
			
				while((i =fr.read()) != -1) {
					txtLine.append((char)i);	
					if(i == '\r' ) {
							if(txtLine.length()>2) {
								z++;	 
								String [] asdbe = txtLine.toString().split(":");
								
									
									char key;
									key = getFirstCharacter(asdbe[1].substring(0,asdbe[1].indexOf(" ")));
									if(!initiaMap.containsKey(key)) { 
										personList = new ArrayList<Person>();	
									}
									else { 
										personList = initiaMap.get(key);		
									}
									
									personList.add(new Person(asdbe[1].substring(0,asdbe[1].indexOf(" "))
															 ,asdbe[2].substring(0,asdbe[2].indexOf(" "))
															 ,asdbe[3].substring(0,asdbe[3].indexOf(" "))
															 ,asdbe[4].substring(0,asdbe[4].indexOf(" "))));
									initiaMap.put(key,personList);
									
									System.out.println(String.format("%s의 연락처가 저장 되었습니다.", asdbe[1].substring(0,asdbe[1].indexOf(" "))));
															
							}
							
							txtLine.setLength(0); //StringBuffer 초기화
						  }	
					}
				
			}catch (IOException e) {
				System.out.println("txt파일 불러오기가 실패했습니다");
				return;
			}
		
		System.out.println("불러오기 저장이 완료되었습니다. 저장된 연락처 갯수 : " + z);
		
	}
	
	
	
	
	//0]마지막 저장 날짜 가져오기
	public String putLastsSavedate(String id) {
		String fileTotal = String.format("src/filesave/member/%s/address.dat",id);
		File file = new File(fileTotal);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분");
		String lsd =  sf.format(new Date(file.lastModified()));
		
		return lsd;
	}
	
	
	//0] 스래드용 노래 변경 목록 번호 가져오기(1)
	
		public int musicThreadchange() {
			int num =1;

			System.out.println("----------------------------------------------------\r\n");
			System.out.println("		[재생가능한 노래 목록]");
			System.out.println("\r\n	1.오랜 날 오랜 밤_악동뮤지션\r\n"
							+ "	2.나의 옛날이야기_아이유 (IU)\r\n"
							+ "	3.봄을 그리다_어반자카파\r\n"
							+ "	4.모든 날, 모든 순간 (Every day, Every Moment)_폴킴  \r\n"
							+ "	5.How Far I'll Go_Aul\r\n");
			System.out.println();
			System.out.println("----------------------------------------------------");
			
			
			
			System.out.println("변경하고 싶은 노래의 번호를 입력해 주세요.");
			while(true) {
			try {
			num = Integer.parseInt(sc.nextLine());
			}
			catch(Exception e) {
				System.out.println("숫자로 입력해주세요.");
			}
			
			if(num <= 5) {
				return num;
			
			}
			else System.out.println("목차에 있는 번호만 입력해주세요.");
			}	
			
		}	
	
	
	
	
	//0]스래드용 노래 변경 목록 번호 가져오기(2)
	public String putSongName(int Number) {
		
		
		String songName="오랜 날 오랜 밤_악동뮤지션";
		
		
		switch (Number) {
		case 1:
			songName = "오랜 날 오랜 밤  -- 악동뮤지션";
			break;
		
		case 2:
			songName = "나의 옛날이야기 -- 아이유 (IU)";
			break;	
		case 3:
			songName = "봄을 그리다 -- 어반자카파";
			break;		
		case 4:
			songName = "모든 날, 모든 순간 (Every day, Every Moment) -- 폴킴";
			break;
		case 5:
			songName = "How Far I'll Go -- Auli'i Cravalho";
			break;		
		default:
			break;
		}
		
		
		return songName;
	}
	
	
	
	//0]자동저장용 출력과 변경
	public int autosave(int minute) {
		
		
		String autosavestate = String.format("ON, %d분",minute) ;
	
		
		System.out.println("---------------------------------");
		System.out.println("");
		System.out.println("	|현재 자동저장 상태|	\r\n");
		if(minute == 0)
			System.out.printf(" 	     OFF\r\n");
		else 
			System.out.printf(" 	   %s \r\n",autosavestate);
		System.out.println("\r\n---------------------------------");
		System.out.println("OFF 하고 싶으시면 '0'을, \r\n저장 간격을 바꾸고 싶으시면 원하시는 '분'을 숫자로 입력해 주세요.");
		
		while(true) {
		Scanner sc = new Scanner(System.in);
		try {
			minute = Integer.parseInt(sc.nextLine());
			break;
		}catch(Exception e) {
			System.out.println("숫자만 입력 해 주세요.");
			}
		}
		return minute;
	}
	
	
	////<카테고리용_로직/메소드 >////
	Map<String,List<Person>>categoryMap = new TreeMap<String,List<Person>>();
	List<Person> personList2 = null;
	
	//메인 메소드 1] 출력 _ 어마무시하게 길다, 추후에 메소드로 자르기
	public void categorymain() {
		
		//여기서 파일 읽어오기...? .. 
		
		
			int z=0; // 검색용 인덱스로 사용. 
			int i=0;
			String categoryName = null;
			Scanner sc = new Scanner(System.in); 
			
			while(true) {
				while(true) {
					System.out.println("----------------------------------------------------\r\n");
					System.out.println("		[카테고리 목록]");
					System.out.println();
					Set keys = categoryMap.keySet();
					i=0;
					for(Object key: keys) {
						i++;
						System.out.printf("%2d. %-7s",i,key);
						if(i%4==0)
							{System.out.println("\r\n");}
					}
					
					
					if(i==0) { 
						System.out.println("	*생성된 카테고리가 없어요.\r\n	  카테고리를 생성해서 연락처를 관리해보세요.");
						System.out.println();System.out.println();
						}
					
					System.out.println("\r\n");
					System.out.println("----------------------------------------------------");
					System.out.println("검색 혹은 추가 하고자하는 카테고리의 이름 입력해주세요.");
					System.out.println("삭제를 원할 시에는 '삭제!'를 입력해주세요.");
					System.out.println("'나가기!'를 입력하시면 메뉴로 돌아갑니다.");
					
					categoryName = sc.nextLine(); 
					if(categoryName.equals("나가기!"))break;
					
					
					if(categoryMap.containsKey(categoryName)) {
						System.out.println("이 값은 존재합니다  : " + categoryName);
						personList2 = categoryMap.get(categoryName); 
						try {
							if(!personList2.isEmpty()) {
								z=0;
								for(Person person : personList2) {
									z++;
									System.out.printf("%d] %s \r\n",z,person);					
								}	
								System.out.println("----------------------------------------------------");
								System.out.println("| 추가  |  삭제   |  나가기   |  *원하시는 메뉴를 한글로 입력하세요	 ");
								System.out.println("----------------------------------------------------\r\n");
								break;
							}
							else {
								System.out.println("해당목록이 비어있습니다.");
								System.out.println("-----------------------------------------");
								System.out.println("| 추가  |  나가기   |  *원하시는 메뉴를 한글로 입력하세요	 ");
								System.out.println("-----------------------------------------");
								break;
								
							}	
								
								
						} catch (NullPointerException e) {
							System.out.println("해당목록이 비어있습니다.");
							System.out.println("-----------------------------------------");
							System.out.println("| 추가  |  나가기   |  *원하시는 메뉴를 한글로 입력하세요	 ");
							System.out.println("-----------------------------------------");
							break;
						}
					
					}//카테고리 목록 들어가거나. 존재 x 시 메소드.	
					else if(categoryName.equals("삭제!")) {
						
						System.out.println("삭제할 카테고리의 이름을 입력하세요.");
						while(true) {
						categoryName = sc.nextLine(); 
						if(categoryMap.containsKey(categoryName)) {
							System.out.println(categoryName+"이 삭제 되었습니다.");
							categoryMap.remove(categoryName);
							break;		
						}
						else {
							System.out.println("카테고리 이름이 존재하지 않습니다. 다시 입력하세요.");
						}
						}//while
					}//else if(categoryName.equals("삭제!!"))
					else {
						System.out.println("이 값은 존재하지 않습니다. 새로생성하였습니다.  :" + categoryName);
						personList2 = new ArrayList<Person>();
						categoryMap.put(categoryName,personList2);
					} 
				
					
			}//2번째 while
		
			if(categoryName.equals("나가기!"))break;	
				
			String check = sc.nextLine(); 
			if(check.equals("추가")||check.equals("나가기")||check.equals("삭제")) {
				if(!(z == 0 & check.equals("삭제"))) {
					if(check.equals("추가")) {
						

						List<Person> valuesReturn =categorySearch();
						int num = 0;
						for(Person p : valuesReturn) {	
							num++;
							System.out.printf("%d ] %s",num,p);
							
						}
						
						
						if(valuesReturn.size() != 0) {
							
							System.out.println("\r\n추가할 사람의 목록번호를 입력하세요.");
							int number1;
							while(true) {
							try {
							number1 = Integer.parseInt(sc.nextLine()); 
							}catch (Exception e) {
								System.out.println("숫자만 입력해주세요");
								continue;
							}
							if(number1 == -1)break;
							if(num >= number1) {
								personList2.add(valuesReturn.get(number1-1));
								categoryMap.put(categoryName,personList2);
								break;
								
							}
							else {
								System.out.println("그런번호는 없습니다.'-1'입력하면 종료합니다. ");
								
							}
										
						 }
						
					   }			
						
					}
					else if(check.equals("삭제")){
						int number = 0;
						System.out.println("삭제할 대상의 번호를 입력하세요.");
						while(true) {
						
						try {
						number =Integer.parseInt(sc.nextLine());
						break;
						}
						 catch (NullPointerException e) {
							System.out.println("숫자만 입력 하세요. ");	 
						 }
						}
						
						if(number <= personList2.size()) {
							personList2.remove(number-1).toString();
							categoryMap.put(categoryName,personList2);
						
							
						}
						else
						{
						System.out.println("그런 번호는 없습니다.");	
						}
					}
					else { // 나가기. 
						System.out.println("메뉴로 돌아갑니다.");
					}
						

					
				}//z=0 & 나가기 비교 if 문 	
				else {
				System.out.println("그런 목차는 없어요");
				}
			}//3개비교 if문 
			else {
				System.out.println("그런 목차는 없어요");
			}	
			

		}// while()	
			
	}//categorymain()
	
	
	//카테고리 메인 메소드 2] 카테고리 검색
	public List<Person> categorySearch() {
			

			List<Person> valuesReturn = new ArrayList<Person>();
			List<Person> values2 = new ArrayList<Person>();
			
			
			
			while(true) {
			System.out.println("추가할 대상을 검색하세요.");
			String searchName = sc.nextLine();
			char key = getFirstCharacter(searchName);
			if(searchName.equals("나가기!")) 
				break;
			if(key == '0') {
				System.out.println("한글 혹은 영어로 입력해주세요.메뉴로돌아가려면 '나가기!'를 입력하세요");
				continue;
			}
			
			if(initiaMap.containsKey(key)) {
				values2 = initiaMap.get(key);
				for(Person vaule : values2) {
					if((vaule.name).equals(searchName)) {
						
						valuesReturn.add(vaule);
						
				
					}
				}return valuesReturn;//해당이름 옮겨담기 완료.
			}	
			System.out.println("그런 이름은 없어요.다시입력하세요.메뉴로돌아가려면 '나가기!'를 입력하세요 ");
			
		}return valuesReturn;
			

	}//categorySearch()
	
	
	
	
	//카테고리 메인 메소드 3]카테고리 파일 세이브
	public void categoryfilesave(String id) {
		
		/*//카테고리가 전체삭제 되는 경우 고려
		if(categoryMap.isEmpty()) {
			return;
		}
		*/
		String fileName = String.format("src/filesave/member/%s/category.dat",id);
		
		ObjectOutputStream out=null;
		try {
			out=new ObjectOutputStream(
					new FileOutputStream(fileName));
			
			out.writeObject(categoryMap);
			
			
		}
		catch(IOException e) {
			System.out.println("파일 저장시 오류:"+e.getMessage());
		}
		finally {
			
				try {
					if(out !=null) out.close();
				}catch (IOException e) {}
		}

		
		
		
	}
		
	//카테고리 메인 메소드 4]카테고리 저장 파일 불러오기_ 생성자에서 실행	
	public void categoryfilecall(String id) {
		
		String fileName = String.format("src/filesave/member/%s/category.dat",id);

		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fileName));
			categoryMap = (Map<String,List<Person>>)ois.readObject();
			ois.close();
		}
		catch(Exception  e) {
			//System.out.println("파일이 존재하지 않아요:"+e.getMessage());
		}
	
	}//filecall()
	
	
	
	//#단어 처리용 메소드#//
	
	//0]초성 자음, 알파벳 앞글자, 해당없음 : 0 
	public static char getFirstCharacter(String name){
	
		char initial=name.trim().toCharArray()[0];
		

		if(initial >='가' && initial < '나') return 'ㄱ';
		else if(initial >='나' && initial < '다') return 'ㄴ';
		else if(initial >='다' && initial < '라') return 'ㄷ';
		else if(initial >='라' && initial < '마') return 'ㄹ';
		else if(initial >='마' && initial < '바') return 'ㅁ';
		else if(initial >='바' && initial < '사') return 'ㅂ';
		else if(initial >='사' && initial < '아') return 'ㅅ';
		else if(initial >='아' && initial < '자') return 'ㅇ';
		else if(initial >='자' && initial < '차') return 'ㅈ';
		else if(initial >='차' && initial < '카') return 'ㅊ';
		else if(initial >='카' && initial < '타') return 'ㅋ';
		else if(initial >='타' && initial < '파') return 'ㅌ';
		else if(initial >='파' && initial < '하') return 'ㅍ';
		else if(initial >='하' && initial <= '힣') return 'ㅎ';
		
		
		else if(initial >='a' && initial <= 'z') return initial;
		else if(initial >='A' && initial <= 'Z') return initial;
		
		
		return '0';
	}
	
	//0] 앞글자에 해당되는 이름 전체 출력 위한 메소드
	//// 별별  -> 별별이, 별별생각, 별난학생 ...등  다 출력 될 수 있도록. 
	public static String getFrontString(String serchname, String checkname) {
			
		StringBuffer frontNameBuffer = new StringBuffer(checkname);

		int i = serchname.length();
		String frontName;
		
		frontName=frontNameBuffer.substring(0,i);
		
		
		//System.out.println(i+frontName);
		return frontName;
	}
	
}
