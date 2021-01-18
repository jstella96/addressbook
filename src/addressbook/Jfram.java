package addressbook;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class Jfram {
	//Jfram_ 현재 구현 X.. 
	public void JframLogin (String memberName) { 
		String memberN = String.format("주소록 프로그램이 종료됩니다.!\r\n %s님 안녕히가세요. ",memberName);
		JFrame jfm = new JFrame("종료");
		JLabel jl = new JLabel(memberN); 
		jfm.add(jl);
		
		jfm.setLocationRelativeTo(null);
		jfm.setSize(300, 130);
		jfm.setVisible(true);
		
	}

}
