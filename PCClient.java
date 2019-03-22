package PC;

import java.io.*;
import java.net.*;

import bluetooth.EV3Server;

import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import assignment2.MappingOnRobot;

public class PCClient extends JFrame{
	public static void main(String[] args) throws UnknownHostException, IOException{

		  //assignment2.MappingOnRobot monitor = new MappingOnRobot();

		  //monitor.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		  String ip = "10.0.1.1";

		  
		  Socket sock = new Socket(ip, 1234);

		  InputStream inputStream = sock.getInputStream();
		  DataInputStream dataInputStream = new DataInputStream(inputStream);
		  
		  //String ip = "10.0.1.1";
			if (args.length > 0)
				ip = args[0];
			
			System.out.println("Connected");
			InputStream in = sock.getInputStream();
			DataInputStream dIn = new DataInputStream(in);
			String str = dIn.readUTF();
			System.out.println(str);
			sock.close();
	}
}
