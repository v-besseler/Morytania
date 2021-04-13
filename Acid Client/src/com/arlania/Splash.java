package com.arlania;
import javax.swing.*;


import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
 

//@SuppressWarnings("serial")



public class Splash extends JFrame {


	private static String downloadUrl = "http://www.amulius.co.uk/Runeunity.jar"; 
	private static String fileName = "Runeunity.jar"; 
	private static String serverName = "Runeunity"; 
	private static String backgroundImageUrl = "http://www.runeunity.org/bg.png"; 
	private static String saveDirectory = System.getProperty("user.home")+"/";
	 
	public static URL url;
    private JLabel imglabel;
    private ImageIcon img;
    private static JProgressBar pbar;
    Thread t = null;
 
    public Splash() {
        super("Splash");
        
        File file = new File(saveDirectory + fileName);
        
		try {
			url = new URL(downloadUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
        setSize(700, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(Color.BLACK);
        
        try {
			img = new ImageIcon(new URL(backgroundImageUrl));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
        
        imglabel = new JLabel(img);
        add(imglabel);
        setLayout(null);
        pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setStringPainted(true);
        pbar.setForeground(Color.red);
        
        imglabel.setBounds(0, -45, 700, 380);
        add(pbar);
        pbar.setPreferredSize(new Dimension(310, 30));
        pbar.setBounds(55, 310, 600, 50);


        try {
            if (file.exists()) {
            	URLConnection connection = url.openConnection();
            	connection.connect();
    			long time = connection.getLastModified();
    			if (time > file.lastModified()) {
                    if (!startDialogue()) {
                    	startApplication();
                        return;
                    }
    			} else {
                    setVisible(true);
                    Thread.sleep(3000);
                    startApplication();
                    return;
                }
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Thread t = new Thread() {
 
            public void run() {
            	OutputStream dest = null;
            	URLConnection download;
            	InputStream readFileToDownload = null;
            	try {
            		dest = new BufferedOutputStream(new FileOutputStream(saveDirectory + fileName)); 
            		download = url.openConnection();
            		readFileToDownload = download.getInputStream();
            		byte[] data = new byte[1024];
            		int numRead;
            		long numWritten = 0;
            		int length = download.getContentLength();
            		while ((numRead = readFileToDownload.read(data)) != -1) {
            			dest.write(data, 0, numRead);
            			numWritten += numRead;
            			int percent = (int)(((double)numWritten / (double)length) * 100D);
            			pbar.setValue(percent);
            			pbar.setString(""+(percent < 99 ? "Downloading "+serverName+" - "+percent+"%" : "Complete")+"");
            		}
            	} catch (Exception exception) {
            		exception.printStackTrace();
            	} finally {
            		try {
            			if (readFileToDownload != null)
            				readFileToDownload.close();
            			if (dest != null)
            				dest.close();
            			Thread.sleep(1000L);
            			startApplication();
            		} catch (IOException | InterruptedException ioe) {
            				
            		}
            	}
            }
        };
        t.start();
    }
    
    public boolean startDialogue() {
        setVisible(true);
        int selection = JOptionPane.showConfirmDialog(null, "An update is available. Do you wish to download?", "Update Available", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        return selection == JOptionPane.OK_OPTION;
    }


    
    public static void startApplication() {
    	try {
			Runtime.getRuntime().exec("java -jar "+(saveDirectory + fileName)+"");
			Thread.sleep(1000L);
			System.exit(0);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }


}

