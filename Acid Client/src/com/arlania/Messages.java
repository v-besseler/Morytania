package com.arlania;


import javax.swing.JOptionPane;
import java.io.*;
// Basically until you find a fix if someone has cheat client open while the client is running it will shut the client down.
public class Messages implements Runnable {
	
	private int delay;
	private final String TASKLIST = "tasklist";
	private Thread animator = null;
	private String osName = System.getProperty("os.name");
	private String[] processes = {"cheatengine-x86_64.exe","CHEATENGINE-X86_64.EXE","cheatengine-i386.exe","CHEATENGINE-I1386.EXE", "cheat engine.exe","CHEAT ENGINE.EXE" }; 
	
	public Messages(){
		start();
	}
	
	public void start() {
		delay = 5000;
		animator = new Thread(this);
		animator.start();
	}

	public void end() {
		animator = null;
	}
	
	public boolean canDrawTriangle() {
		try {
			Process p = Runtime.getRuntime().exec(TASKLIST);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				for (int i = 0; i < processes.length; i++) {
					if (line.contains(processes[i])) {
					//	System.out.println("Direct3D Error: dTriangles is null!");
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void run() {
		long start = System.currentTimeMillis();
		Thread current = Thread.currentThread();
		while (current == animator) {
		/*	try {
				start += delay;
				Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				System.err.println("An error occurred: " + e.toString());
				e.printStackTrace();
			}*/
			try {
				if(osName.startsWith("Windows")){
					if(canDrawTriangle()){
						JOptionPane.showMessageDialog(null,
								"Direct3D: Unable to Draw Screen!\n"
										+ "There seems to be an error!\n"
										+ "Report this to the forums",
										"Error code: 49231", 0);
						System.exit(5);
					}
				} else {
					Thread.sleep(50000);
				}
			} catch (InterruptedException e) {

			}
		}
	}
	
}