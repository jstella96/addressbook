package addressbook;

import java.util.Scanner;

import memberManagement.MembershipLogic;

public class AddressbookApp {
	
	
	static String lastSaveDateString;

	public static void main(String[] args) {
		
		
	
		//0]음악 스레드 실행. 현재 5개. 
		int musicNumber = 1;
		Thread th = musicThread(musicNumber);

		//1]로그인 페이지_ MemberManagement 패키지.
		MembershipLogic logic = MembershipLogic.getMembershipLogic();
		
		
		boolean ischeck = true; //로그인 페이지 빠져나오는거 체크.
		while(ischeck) {
			logic.printSignUpMenu(); // 출력.
			ischeck = logic.seperateMainMenu(ischeck);
		}

		
		
		//2]주소록 매인 로직
		AddressbookLogic addressbookLogic = new AddressbookLogic(logic.loginId);
		//Jfram J = new Jfram();
		Scanner sc = new Scanner(System.in);
		System.out.println("로그인 성공! "+"'"+logic.loginName+"'"+"님 환영합니다.");
		
		//String id = logic.loginId;
		//String name = logic.loginName;
		String songName = addressbookLogic.putSongName(musicNumber);
		lastSaveDateString =addressbookLogic.putLastsSavedate(logic.loginId);
		
		//0]자동저장 스레드 실행. 기본 1분 
		int minute= 1;
		Thread th2 = autoSaveThread(minute,logic.loginId,addressbookLogic);
		
		
		//2-1]주소록 메뉴
		int mainManu = -1;
		while(true) {
			
			addressbookLogic.printMainMenu(songName,lastSaveDateString,minute);
			
			while(true) {
			try {
			mainManu = Integer.parseInt(sc.nextLine());
			break;
			}
			catch (Exception e) {
				System.out.println("숫자만 입력하세요");
			}}
			
			switch(mainManu) {
			case 1://입력
				addressbookLogic.setPerson();
				
				break;
			case 2://출력
				addressbookLogic.printPerson();
				break;
			case 3://검색_수정,삭제
				addressbookLogic.searchPerson();		
				break;
			case 4://카테고리 메뉴
				addressbookLogic.categorymain();
				
				break;
			case 5://노래변경
				musicNumber = addressbookLogic.musicThreadchange();
				songName = addressbookLogic.putSongName(musicNumber);
				if(th.isAlive()) th.interrupt();
				th = musicThread(musicNumber);
				break;
			case 6://노래종료
				if(th.isAlive()) th.interrupt();
				System.out.println("노래가 종료되었습니다.");
				break;
			case 7://현재 상태 저장,저장시 날짜도 저장 
				addressbookLogic.filesave(logic.loginId);
				lastSaveDateString = addressbookLogic.putLastsSavedate(logic.loginId);
				break;	
			case 8://자동저장 관리
				minute = addressbookLogic.autosave(minute);
				if(th2.isAlive()) th2.interrupt();
				if(minute != 0)
					th2 = autoSaveThread(minute,logic.loginId,addressbookLogic);
				System.out.println("변경되었습니다.");
				break;
			case 9: // 마이 페이지
				int a = addressbookLogic.myPage(logic.loginName,logic.loginId);
					switch (a) {
					case 1:
						logic.pwChange(logic.loginId);
						break;
					case 2:
						logic.deleteMember(logic.loginId);
						break;
	
					default:
						break;
					}
				break;	
			case 10://연락처 txt형식으로 내보내기! 이벤트 창 활용. 
				new FiletxtEvent(logic.loginId);
				break;
			case 11://내보낸 txt 파일 다른 아이디에서 불러오기
				addressbookLogic.FileTxtInput();
				break;	
			case 0://프로그램 종료
				//J.JframLogin (logic.loginName)_ 사용x 시간날 때 추가해보기.
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
				break;	
			default:
				System.out.println("메뉴에 없는 번호입니다");break;
			}		
		
		}
	
	}//main
	
	
	//0]스레드용 메소드
	public static Thread musicThread(int musicNumber){
		
		MusicThread daemon = new MusicThread(musicNumber);
		Thread th = new Thread(daemon);
		th.setName("노래 재생 스레드");
		th.setDaemon(true);
		th.start();//스레드 runnable상태로
		return th;
		
	}

	public static Thread autoSaveThread(int minute, String id, AddressbookLogic addressbookLogic){
		
		
		lastSaveDateString = addressbookLogic.putLastsSavedate(id);
		
		AutoThread daemon2 = new AutoThread(minute,id,addressbookLogic);
		Thread th2 = new Thread(daemon2);
		th2.setName("자동 저장 스레드");
		th2.setDaemon(true);
		th2.start();//스레드 runnable상태로
		
		return th2;
		
	}
	
}//class
