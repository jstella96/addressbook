package addressbook;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicThread implements Runnable {
	//[스레드 _ 노래 5곡 재생,정지 용.]
	
	
	File file1 = new File("src/filesave/song/오랜 날 오랜 밤_악동뮤지션.wav");
	File file2 = new File("src/filesave/song/나의 옛날이야기_아이유 (IU).wav");
	File file3 = new File("src/filesave/song/봄을 그리다_어반자카파.wav");
	File file4 = new File("src/filesave/song/모든 날, 모든 순간 (Every day, Every Moment)_폴킴.wav");
	File file5 = new File("src/filesave/song/How Far I'll Go_Auli'i Cravalho.wav");
	AudioInputStream stream = null;
	Clip clip = null;
	int i = 1;
	
	public MusicThread(int i) {
		
		this.i = i;
		
	}
	
	
    public void run() {

		    	
    	try {
    			Sound(i);
        		while(!Thread.currentThread().isInterrupted()){
        		
            }
        		   
        } catch(Exception e) {
            
        }finally {
          
            clip.close(); 
        }
   

    }
    
     public void Sound(int i) {
    	
    	
    	switch (i) {
		case 1:
			try {

	    		AudioInputStream stream = AudioSystem.getAudioInputStream(file1);
	    		clip = AudioSystem.getClip();
	            clip.open(stream);
	            clip.start();
	            
	    	} catch (Exception e) {

	    		e.printStackTrace();
	    	}
			
			break;
		case 2:
			
			try {

	    		AudioInputStream stream = AudioSystem.getAudioInputStream(file2);
	    		clip = AudioSystem.getClip();
	            clip.open(stream);
	            clip.start();
	            
	    	} catch (Exception e) {

	    		e.printStackTrace();
	    	}
			
			
			break;
		case 3:
	
			try {

	    		AudioInputStream stream = AudioSystem.getAudioInputStream(file3);
	    		clip = AudioSystem.getClip();
	            clip.open(stream);
	            clip.start();
	            
	    	} catch (Exception e) {

	    		e.printStackTrace();
	    	}

			break;	
		case 4:
			
			try {

	    		AudioInputStream stream = AudioSystem.getAudioInputStream(file4);
	    		clip = AudioSystem.getClip();
	            clip.open(stream);
	            clip.start();
	            
	    	} catch (Exception e) {

	    		e.printStackTrace();
	    	}
			
			break;	
		case 5:
			
			try {

	    		AudioInputStream stream = AudioSystem.getAudioInputStream(file5);
	    		clip = AudioSystem.getClip();
	            clip.open(stream);
	            clip.start();
	            
	    	} catch (Exception e) {

	    		e.printStackTrace();
	    	}
			break;		
		default:
			break;
		}
	
    		
    }
    

}//class
