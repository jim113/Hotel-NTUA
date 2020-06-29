import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javafx.scene.text.Text;

public class LiveDateSwing{

   public static String intToString(long num, int digits) {
    String output = Long.toString(num);
    while (output.length() < digits) output = "0" + output;
    return output;
	}
   
   
   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
		
	 private boolean flag = true;
   private Thread t;

    public LiveDateSwing(Text label) {
      Date start = getDate();
      System.out.println(simpleDateFormat.format(start));
      Runnable runnable = new Runnable() {

        @Override
        public void run() {
          while (flag) {
            Date date = getDate();
            
            long diff = (date.getTime() - start.getTime());
            long sec = diff / 1000 % 60;
            long minutes = diff / (60 * 1000) % 60;
            long hours = diff / (60 * 60 * 1000);
            
            label.setText("Total time: "+ intToString(hours,2) + ":" + intToString(minutes,2)+":"+intToString(sec,2));
            try {
              Thread.sleep(1000);
            }
            catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
    };

      t = new Thread(runnable);
    }
    public void start(){
      t.start();
    }
    public void stop(){
    	flag = false;
    }

    public static java.util.Date getDate() {
      java.util.Date date = new java.util.Date();
      return date;
    }
}
