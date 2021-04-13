package com.arlania;

import javax.swing.*;
import com.arlania.Splash;
 
 /**
  * @author Pwnhub
  */
public class Launcher {
	
	//This will just launch your launcher. So for now, run your client via this class as the main construction, alright Aj?
    public static void main(String[] args) {
    	 final Splash s = new Splash();
        s.setVisible(true);
         SwingUtilities.invokeLater(new Runnable(){
             public void run()  {
                  s.setVisible(true);
             }
         });
    }


}