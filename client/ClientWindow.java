package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import client.gui.*;

public class ClientWindow implements WindowListener, ActionCallback
{
	protected JFrame mainWindow;
	protected ClientLogonDialog loginDialog;
	protected ClientViewArea viewArea;
	
	public ClientWindow()
	{
		mainWindow = new JFrame("Sphereority");
		mainWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		mainWindow.addWindowListener(this);
		
		viewArea = new ClientViewArea(null);
		
		mainWindow.getContentPane().add(viewArea, BorderLayout.CENTER);
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
		
		loginDialog = new ClientLogonDialog(mainWindow);
		if (!loginDialog.show())
		{
			System.out.println("User quit before logging into any server.");
			System.exit(0);
		}
	}
	
	public void actionCallback(InteractiveWidget source, int buttons)
	{
		
	}
	
	public void show()
	{
		mainWindow.setVisible(true);
	}
	
	public void hide()
	{
		mainWindow.setVisible(false);
	}
	
	public void windowClosing(WindowEvent e)
	{
		// TODO Perform any close operations we need to do here
		
	}
	
	public void windowActivated(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
	
	public static void main(String[] args)
	{
		ClientWindow win = new ClientWindow();
		win.show();
	}
}
