package com.arlania;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("all")
final class RSFrame extends JFrame {

	public static boolean destroy;
	
	
	public RSFrame(RSApplet rsapplet, int width, int height, boolean undecorative, boolean resizable) {
		rsApplet = rsapplet;
		setTitle(Configuration.CLIENT_NAME);
		setUndecorated(undecorative);
		setResizable(resizable);

	
		setVisible(true);
		Insets insets = this.getInsets();
		setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
		Client.getClient();
		setLocation((screenWidth - width) / 2, ((screenHeight - height) / 2) - screenHeight == Client.getMaxHeight() ? 0 : undecorative ? 0 : 70);

		requestFocus();
		toFront();
		this.setFocusTraversalKeysEnabled(false);
		setBackground(Color.BLACK);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we) { 
		        String options[] = {"Yes", "No"};
		        int userPrompt = JOptionPane.showOptionDialog(null, "Are you sure you wish to exit?", "Morytania", 
		        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options , options[1]);
		        if(userPrompt == JOptionPane.YES_OPTION) {
		        	//launchURL("http://Morytania.org/forums");
		        	destroy = true;
		            System.exit(0);
		        } else {
		        	destroy = false;
		        }
		    }
		});
		
	}
	
	public void launchURL(String url) {
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows")) {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else {
				String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++) {
					if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
						browser = browsers[count];
					}
				}
				if (browser == null) {
					throw new Exception("Could not find web browser");
				} else {
					Runtime.getRuntime().exec(new String[] { browser, url });
				}
			}
		} catch (Exception e) {
		
		}
	}
	
	public void setClientIcon() {
		Image img = Client.resourceLoader.getImage("icon");
		if(img == null)
			return;
		setIconImage(img);
	}

	
	public void mouseWheelMoved(MouseWheelEvent event) {
		rsApplet.mouseWheelMoved(event);
	}

	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		Insets insets = this.getInsets();
		g.translate(insets.left, insets.top);
		return g;
	}

	public int getFrameWidth() {
		Insets insets = this.getInsets();
		return getWidth() - (insets.left + insets.right);
	}

	public int getFrameHeight() {
		Insets insets = this.getInsets();
		return getHeight() - (insets.top + insets.bottom);
	}

	public void update(Graphics g) {
		rsApplet.update(g);
	}

	public void paint(Graphics g) {
		rsApplet.paint(g);
	}

	final RSApplet rsApplet;
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screenSize = toolkit.getScreenSize();
	int screenWidth = (int) screenSize.getWidth();
	int screenHeight = (int) screenSize.getHeight();
}