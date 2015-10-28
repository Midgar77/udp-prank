import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class TestSend {

	public static void main(String[] argv) throws Exception {

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("10.161.4.4");
		Scanner scanner = new Scanner(System.in);
		
		

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
