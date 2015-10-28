import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class TestSend {

	public static void main(String[] argv) throws Exception {

		DatagramSocket clientSocket = new DatagramSocket();
		//Replace ENTER IP HERE with the IP that the UDP server is running on
		InetAddress IPAddress = InetAddress.getByName("ENTER IP HERE");
		Scanner scanner = new Scanner(System.in);
		
		/*
		* Client sends commands to the server through sending Strings to the server.
		* Server has specific commands stored so it processes the commands that it receives and takes appropriate action.
		*/
		
		String input = "";
		while (!input.equals(":q")) {
			System.out.print("Command: ");
			input = scanner.nextLine();
			byte[] sendData = input.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);

			System.out.println("Sent: " + input);
		}

		clientSocket.close();
		scanner.close();

	}

}
