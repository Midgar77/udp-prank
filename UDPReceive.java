import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class UDPReceive {

	private Robot robot;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int SCREEN_WIDTH = ((int)screenSize.getWidth());
	private final int SCREEN_HEIGHT = ((int)screenSize.getHeight());


	public UDPReceive(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}


	public static void main(String args[]) {
		UDPReceive receiver = new UDPReceive();
		try {
			//Assuming this port isn't already being used
			int port = 9876;

			// Create a socket to listen on the port.
			DatagramSocket dsocket = new DatagramSocket(port);

			// Create a buffer to read datagrams into. If a
			// packet is larger than this buffer, the
			// excess will simply be discarded!
			byte[] buffer = new byte[2048];

			// Create a packet to receive data into the buffer
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			String msg = "";
			// Now loop forever, waiting to receive packets and printing them.
			while (!msg.equalsIgnoreCase("exit")) {
				// Wait to receive a datagram
				dsocket.receive(packet);

				// Convert the contents to a string, and display them
				msg = new String(buffer, 0, packet.getLength());
				System.out.println(packet.getAddress().getHostName() + ": "
						+ msg);

				//Opens up a webpage with the href starting after the space after "web"
				//Example: web www.google.com  will open up a google webpage
				if(msg.contains("web"))
					receiver.openWebPage(msg.substring(4));
				//Makes the keyboard type the text that follows after the word "type"
				if(msg.contains("type"))
					receiver.typeString(msg.substring(5));

				switch(msg.toLowerCase()){
				//Exit command to stop receiving UDP packets. 
				//NOTE: This server cannot be re-ran from the client if this exit command is called.
				case "exit":
					dsocket.close();
					break;
				case "close window":
					receiver.closeWindow();
					break;
				case "move circle":
					receiver.moveMouseCircle(40, 10, 8);
					break;
				case "screenshot":
					receiver.screenCapture();
					break;
				case "windows":
					receiver.windowsButton();
					break;
				default:
					break;
				
				}




				// Reset the length of the packet before reusing it.
				packet.setLength(buffer.length);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}


	public void windowsButton(){
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
	}


	public void closeWindow(){
		robot.delay(2000);
		Point p = MouseInfo.getPointerInfo().getLocation();
		int currX = p.x;
		int currY = p.y;
		robot.mouseMove(SCREEN_WIDTH-20, 10);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.mouseMove(currX, currY);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}


	public void typeString(String s){

		byte[] bytes = s.getBytes();
		for (byte b : bytes) {
			int code = b;
			if (code > 96 && code < 123)
				code = code - 32;
			robot.delay(40);
			robot.keyPress(code);
			robot.keyRelease(code);
		}
	}


	public void openWebPage(String href){
		openWebURI(getURI(href));   
	}

	public void openWebURI(URI uri){
		try {
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public URI getURI(String text){
		try {
			return new URI(text);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public void screenCapture(){
		// capture the whole screen
		BufferedImage screencapture = robot.createScreenCapture(
				new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()) );

		// Save as JPEG
		int count = 1;
		File file = new File("c:/Users/IBM_ADMIN/Desktop/screencapture.jpg");
		while(file.exists()){
			count++;
			file = new File("c:/Users/IBM_ADMIN/Desktop/screencapture" + count + ".jpg");
		}
		try {
			ImageIO.write(screencapture, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//Speed > 10 is barely visible
	public void moveMouseCircle(int radius,int rotations,int speed){
		Point p = MouseInfo.getPointerInfo().getLocation();

		int x = p.x;
		int y = p.y;

		robot.mouseMove(x,y);

		for(double i = 0; i < Math.PI*rotations*2; i+=Math.PI/100){
			robot.mouseMove(x+(int)(Math.cos(i)*radius),y+(int)(Math.sin(i)*radius));
			robot.delay((int)(10.0/speed));
		}

	}

} 
