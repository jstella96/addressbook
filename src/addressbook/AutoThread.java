package addressbook;

public class AutoThread implements Runnable{

	String id;
	AddressbookLogic addressbookLogic;
	int time;
	
	public AutoThread(int time,String id,AddressbookLogic addressbookLogic) {
		this.addressbookLogic = addressbookLogic;
		this.id = id;
		this.time = time;
		
	}
	@Override
	public void run() {

		try {
			
			while(!Thread.currentThread().isInterrupted()) {
				
				
                Thread.sleep(time*60000);// 자동저장. 시간 바꾸게.
                //자동저장되면 이벤트창 3..2..1.. + 자동저장 수정도 할 수 있거, 시간 바꾸는거
                //위 321 사용해서 
                
				addressbookLogic.filesave(id);
                AddressbookApp.lastSaveDateString = addressbookLogic.putLastsSavedate(id);
                System.out.println("자동저장되었습니다.");
				
			}
			
		}catch (Exception e) {
			// x
		} finally {
			// x 
		}
		
		
		
		
		
		
	}



}
