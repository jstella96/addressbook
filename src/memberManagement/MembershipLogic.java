package memberManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MembershipLogic {
	

	//1]싱글톤 :  유일 생성자,static 이므로 프로그램 생성하면서 생성.
	private static MembershipLogic instanceMembershipLogic = new MembershipLogic(); 
	
	private HashSet<Member> hashSetMember;
	public String loginId; 
	public String loginName;
	
	//2]싱글톤 : 생성자를 명시적으로 지정해줌으로, 디폴트 생성x, private이므로 외부에서 선언 불가. 
	private MembershipLogic () { 
			hashSetMember = new HashSet<Member>(); //멤버인스턴스를 저장 할 set 생성
			memberfileCall(); //이전에 저장한 파일 불러오기.
	}
	

	
	//3]싱글톤 : 외부에서 유일 인스턴트를 받기 위한 get() 함수
	public static MembershipLogic getMembershipLogic() {
		if(instanceMembershipLogic == null) {
			instanceMembershipLogic = new MembershipLogic();
		}
		
		return instanceMembershipLogic;
	}
	
	
	
	public void printSignUpMenu() {
		System.out.println("           < 주소록 프로그램 >                 ");
		System.out.println("===========================================");
		System.out.println("   1. 로그인      2. 회원가입      3. 관리자접속");
		System.out.println("===========================================");
	}
	
	
	public boolean seperateMainMenu(boolean ischeck) {
		Scanner sc = new Scanner(System.in);
		int menuNumber=-1;
		try {
			menuNumber =sc.nextInt();
		}catch(InputMismatchException e) {
			System.out.println("숫자만 입력 가능합니다.");
			return ischeck;
		}
		switch(menuNumber) {
		case 1:
			if(login() != null) {
				ischeck = false;
				return ischeck;
			}
			else {
				System.out.println("로그인 실패 : 회원정보가 없습니다.");	
				break;
			}
		case 2: 
			signUpForMembership();
			break;
		case 3: 
			printMember();
			break;
		default : 
			System.out.println("메뉴에 없는 번호입니다");
			break;
		}
	return ischeck;}
	
	
	//1]회원가입 메소드 .
	public void signUpForMembership() {
		Scanner sc = new Scanner(System.in);
		//Member형으로 인스턴스생성. 자료형 : HashSet *주의점 : hashCode와 equals 오버라이딩 해서 아이디 비교 하기. 
		String memberId;
		String memberPw; 
		String memberName;
		
		System.out.println("======================");
		System.out.println("               회원가입             ");
		System.out.println("======================");
		
		System.out.println("이름을 입력하세요  *한글로만 입력 가능합니다.(2-10글자)");
		while(true) {
			memberName = sc.nextLine();
			if(!(Pattern.matches("^[가-힣]*$",memberName)&&(memberName.length()<=10)&&(memberName.length()>=2))){
				System.out.println("다시 입력하세요.");
				continue;
			}
			else break;
		}//while
		
		
		System.out.println("아이디를 입력하세요 *영문과 숫자로만 입력 가능합니다.(8-15글자)\"");
		while(true) {
			memberId = sc.nextLine();
			if(!(Pattern.matches("^[a-zA-Z0-9]*$",memberId)&&(memberId.length() <=15)&&(memberId.length()>=8))){
				System.out.println("다시 입력하세요.");
				continue;
			}
			else { // 중복체크
				if(checkDuplicateID(memberId)) {
					System.out.println("중복체크 : 사용가능 합니다. ");
					break;
				}	
				else {
					System.out.println("중복체크 : 이미 있는 아이디 입니다. ");	
					continue;
					}
			}
		}//while
		

	
		System.out.println("비밀번호를 입력하세요 *숫자, 영문, 특수문자로 구성된 8자리이상 15자리 이하");
		while(true) {
			memberPw = sc.nextLine();
			
			String patternPw = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}";
			Matcher matcher = Pattern.compile(patternPw).matcher(memberPw);
			String patternPw2 = "(.)\\1\\1\\1";
			Matcher matcher2 = Pattern.compile(patternPw2).matcher(memberPw);
				
			if((matcher.matches())&&(!matcher2.find())&&(memberId.length() <=15)&&(memberId.length()>=8)){
					System.out.println("비밀번호 확인 : ");
					String checkmemberPw = sc.nextLine();
					if(checkmemberPw.equals(memberPw)) {
						hashSetMember.add(new Member(memberName,memberId,memberPw));
						//System.out.println("회원가입이 완료 되었습니다.");//계정은 만들어졌으나, 아직 폴더 생성전. 
						
						break;
					}
					else {
						System.out.println("두 비밀번호가 다릅니다.");
						System.out.println("비밀번호를 입력하세요 *숫자, 영문, 특수문자로 구성된 8자리이상 20자리 이하");
						continue;
					}//if-else
			}
			else {
					System.out.println("다시입력해주세요.");
					continue;
			}//if-else
		}//while
		memberfolderCreate(memberId);
		memberfileSave();
		System.out.println("회원가입이 완료 되었습니다.");
	}//SignUpForMembership()
	
	

	

	public void memberfolderCreate(String memberId) {
		
		String FolderName = String.format("src/filesave/member/%s",memberId);
		
		File Folder = new File(FolderName);

		// 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //폴더 생성합니다.
			    //System.out.println("회원가입이 완료 되었습니다.");//파일저장때 최종완료.
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}        
	         }else {
			System.out.println("경로 출돌 오류가 발생했습니다. 다시 회원가입을 진행해주세요."); 
		}

	}//memberfolderCreate()
	
	
	private void memberfileSave() {
		String pileName = "src/filesave/manager/memberInfo.dat";
		
		
		//객체 저장후에 들어온다.
		ObjectOutputStream out=null;
		try {
			out=new ObjectOutputStream(
					new FileOutputStream(pileName));
			
			out.writeObject(hashSetMember);
			
			
		}
		catch(IOException e) {
			System.out.println("회원가입시 오류. "+ e.getMessage());
		}
		finally {
			
				try {
					if(out !=null) out.close();
				}catch (IOException e) {}
		}
		
	}
	
	
	
	
	
	
	private void memberfileCall() {
		

		String pileName = "src/filesave/manager/memberInfo.dat";
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(pileName));  
			hashSetMember = (HashSet<Member>)ois.readObject();  		
			ois.close();
		}
		catch(Exception  e) {
			//파일이 존재하지 않는다.!
		}


	}//memberPileCall()
	

	
	public boolean checkDuplicateID(String memberId) {
		
		for(Member m : hashSetMember) {	
			if(memberId.equals(m.getMemberId())){	
				return false;
			}	
		}return true;
		
	}//CheckDuplicateID()
	
	
	
	public void printMember() {
		System.out.println("[ 회원 목록  ]");
		for(Member m : hashSetMember) {
	
			System.out.println(m);
			
		}
	}
	
	
	public void deleteMember(String id) {
		Member deleteMember =null;
		for(Member m : hashSetMember) {
			if(m.getMemberId().equals(id)){
				deleteMember =m;
			}
		}
	try {	
		hashSetMember.remove(deleteMember);
		deleteAllfile(id);
	}catch (Exception e) {
	}
	memberfileSave();
	System.out.println("탈퇴가 완료되었습니다... 시스템이 종료 됩니다..");
	System.exit(0);
	}
	
	
	
	public void deleteAllfile(String id) {
		
		String fileName = String.format("src/filesave/member/%s",id);
		File file = new File(fileName); 
		File[] tempFile = file.listFiles();
		if(tempFile.length >0)
			{ for (int i = 0; i < tempFile.length; i++) 
				{ if(tempFile[i].isFile()){ tempFile[i].delete(); }
		else{ 
			deleteAllfile(tempFile[i].getPath());
		} tempFile[i].delete(); } file.delete(); }
	}
		
		

	
	public void pwChange(String id) {

		for(Member m : hashSetMember) {
				if(m.getMemberId().equals(id)){
					m.setMemberPw();
				}
			
		}
		memberfileSave();
	}
	
	
	
	public String login() {
		Scanner sc = new Scanner(System.in);
		System.out.println("ID : ");
		String id = sc.nextLine();
		System.out.println("PW : ");
		String pw = sc.nextLine();
		for(Member m : hashSetMember) {
			if(id.equals(m.getMemberId())&&pw.equals(m.getMemberPw())){	
				loginId = id;
				loginName = m.getMemberName();
				return id;	//로그인성공이면 id값
			}	
		}return null;//로그인실패면 null값
	}




}//class
