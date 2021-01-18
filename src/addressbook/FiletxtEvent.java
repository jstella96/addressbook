package addressbook;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import addressbook.FiletxtEvent.EventHandler;

public class FiletxtEvent extends JFrame{
	
	//[내부 익명 클래스]_이벤트 창_주소록에 저장된 연락처를 텍스트형식으로 반환한다.
	AddressbookLogic addressbookLogic;
	
	JButton button1,button2;
	
	EventHandler handler = new EventHandler();
	String id;
	String message;
	public FiletxtEvent(String id) {
		this.id =id;
		this.message = String.format("%s.txt로 요청하신 파일이 내보내졌어요.",id);
		addressbookLogic = new AddressbookLogic(id);
		setTitle("txt로 저장");
		setLayout(new FlowLayout());
		
		add(button1= new JButton("전체 정보 저장"));
		add(button2= new JButton("이름과 연락처만 저장"));
		
		
		addWindowListener(handler);
		button1.addActionListener(handler);
		button2.addActionListener(handler);

		pack();
		setVisible(true);
	}

	class EventHandler extends WindowAdapter implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == button1) {
				
				addressbookLogic.fileTxtOutput(true,String.format("%s%s",id,"_All"));
				JOptionPane.showMessageDialog(button1,message);
				
			}
			else if(e.getSource() == button2) {
				
				addressbookLogic.fileTxtOutput(false,String.format("%s%s",id,"_Part"));
				JOptionPane.showMessageDialog(button2,message);
			}
			
			
		}
		
	}

}