package memberManagement;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Member implements Serializable  {
		
		//[1]멤버 변수로 회원 ID, PW, 이름 가진다. 
		//회원가입 시 정보는 외부에서 직접 접근 불가.
		//아이디,이름,가입 일자는 변경이 안되니. get메소드만을 선언하여 외부에서 값을 읽을 수 있도록 하고. 
		//비밀번호는 변경도 가능해야하니 get, set 메소드로 선언하고 값조건을 setMemberPw()에서 주도록 하자. 
		private String memberId;
		private String memberPw; 
		private String memberName;
		private String joindate;
		
	
		//[2] 생성자 만들기. 인스턴스화 할 때 ID, PW, 이름  3가지 인자 다 받아올 것.
		public Member(String memberName,String memberId, String memberPw) {
			super();
			this.memberId = memberId;
			this.memberPw = memberPw;
			this.memberName = memberName;
			DateFormat format = new SimpleDateFormat("yyyy.MM.dd 'T' HH:mm");
			this.joindate = format.format(Calendar.getInstance().getTime());
		}


		//[3] id,name,date_get() /pw_set() 메서드 선언.
	
		public String getMemberId() {
			return memberId;
		}

		public String getMemberName() {
			return memberName;
		}

		public String getJoindate() {
			return joindate;
		}
		
		public String getMemberPw() {
			return memberPw;
		}
		
		//[3-1]멤버 패스워드를 새로 생성할 때.set 안에서 비교.
		public void setMemberPw() {
			Scanner sc = new Scanner(System.in);
			String newPw;
			System.out.println("새로운 비밀번호를 입력하세요 *숫자, 영문, 특수문자로 구성된 8자리이상 15자리 이하");
			while(true) {
				newPw = sc.nextLine();
				
				String patternPw = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}";
				Matcher matcher = Pattern.compile(patternPw).matcher(newPw);
	
				if((matcher.matches())&&(newPw.length() <=15)&&(newPw.length()>=8)){
						System.out.println("비밀번호 확인 : ");
						String checkmemberPw = sc.nextLine();
						if(checkmemberPw.equals(newPw)) {
							this.memberPw = newPw;
							System.out.println("비밀번호가 변경되었습니다.");
							break;
						}
						else {
							System.out.println("두 비밀번호가 다릅니다.");
							System.out.println("비밀번호를 입력하세요 *숫자, 영문, 특수문자로 구성된 8자리이상 15자리 이하");
							continue;
						}//if-else
				}
				else {
						System.out.println("조건에 맞춰서 다시입력해주세요.");
						continue;
				}//if-else
			}//while

		}


		
		//[오버라이딩]equals를 재정의 했으니 hashCord또한 재정의. equals에서 비교 변수가 되었던 값으로 하는 것이 좋다.
		@Override
		public int hashCode() {
			
			return memberId.hashCode();
		}

		
		//[오버라이딩]멤버가 같다는 건 멤버 아이디가 같다는 것.
		@Override 
		public boolean equals(Object obj) {
			if(obj instanceof Member) {
				Member member = (Member)obj;
				if(this.memberId.equals(member.memberId)){
					return true;
				}
				else {
					return false;
				}
			}else return false;
		}

		
		//[오버라이딩]print 할 때 읽어올 string 문자열
		@Override
		public String toString() {
			return  String.format("회원아이디 : %-15s 회원이름  : %-15s 가입한 날짜 : %-10s", memberId,memberName,  joindate) ;
		}
		
}//class
