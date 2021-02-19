package redes;

import models.Room;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UDPClient {

	private static Scanner sc = new Scanner(System.in);
	private static List<Room> rooms = new ArrayList<Room>();
	private final static int MULTCAST_PORT = 7896;

	static String userName = "";

	public static void main(String args[]) {

		//args = startServer userName

		if(args[0].equals("startServer")) {
			clientServer("2;");
		}
		userName = args[1];
		System.out.println("Olá " + userName  + " seja bem vindo!");
		menu(sc);
		// args fornecem a mensagem e o endere�o do servidor.

	}

	public static void clientServer(String data) {
		DatagramSocket aSocket = null;
		int serverPort = 6789;
		String ipAddress = "localhost";
		String message;

		try {
			aSocket = new DatagramSocket();
			byte[] m = data.getBytes();
			InetAddress aHost = InetAddress.getByName(ipAddress);
			DatagramPacket request = new DatagramPacket(m, data.length(), aHost, serverPort);


			aSocket.send(request);

			byte[] buffer = new byte[1000];

			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			aSocket.receive(reply);
			if(data.equals("2;")) {
				receiveRooms(new String(reply.getData()).trim());
				printRooms();
			}
			if(data.split(";")[0].equals("3")) {
				System.out.println(data.split(";")[0].equals("3"));
				enterRoom(new String(reply.getData()).trim());
			}

			System.out.println("Resposta: " + new String(reply.getData()).trim());

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	public static void menu(Scanner sc) {
		int option = 999;
		do {
			System.out.println("1 - Criar Sala");
			System.out.println("2 - Listar Salas");
			System.out.println("3 - Entrar em Sala");
			System.out.println("Digite a sua opção: ");
			option = Integer.parseInt(sc.nextLine());
			handleOption(option);
		} while (option != 0);

	}

	public static void handleOption(int option) {
		switch (option) {
			case 1:
				String optionDataCreate = createRoom(option);
				clientServer(optionDataCreate);
				break;
			case 2:
				String optionDataList = listRooms(option);
				clientServer(optionDataList);
				break;
			case 3:
				String optionDataJoin = chooseRoom(option);
				clientServer(optionDataJoin);
				break;
		}
	}

	public static String createRoom(int option) {
		System.out.println("Qual será o nome da sala? ");
		String roomName = sc.nextLine();
		return String.valueOf(option)+ ";" + roomName;
	}

	public static String listRooms(int option) {
		return String.valueOf(option) + ";";
	}

	public static void receiveRooms(String roomsData) {
		String[] splitedRooms = roomsData.split(";");

		for (int i=0; i < splitedRooms.length; i++) {
			String[] fields = splitedRooms[i].split(",");
			int id = 999;
			String name = "";
			String ip = "";
			for(int j = 0; j <fields.length; j ++) {
				String[] splitedField = fields[j].split("=");
				switch (splitedField[0]){
					case "id":
						id = Integer.parseInt(splitedField[1]);
						break;
					case "name":
						name = splitedField[1];
						break;
					case "ipAddress":
						ip = splitedField[1];
						break;
				}
			}
			Room room = new Room(id, name, ip);
			if(!rooms.contains(room))
				rooms.add(room);
		}
		/*
	for (int i =0; i < rooms.size(); i++) {
		System.out.println(rooms.get(i));
	}
	*/
	}

	public static void printRooms() {
		for(int i = 0; i < rooms.size(); i++) {
			System.out.println(rooms.get(i).getId() + " - " + rooms.get(i).getName());
		}
	}

	public static String chooseRoom(int option) {

		System.out.println("Digite o nome da sala que deseja entrar: ");
		String roomName = sc.nextLine();
		return String.valueOf(option) + ";" + roomName + ";" + userName;
	}

	public static void sendMessage( MulticastSocket mSocket, InetAddress ipAddress) {
		String userMessage = "";
		boolean leaveChat = false;
		do {
			String auxMessage = sc.nextLine();

			userMessage = userName + ";" + auxMessage;

			byte[] messageByte = userMessage.getBytes();
			DatagramPacket userSendMessage = new DatagramPacket(messageByte, messageByte.length, ipAddress, MULTCAST_PORT);
			try {
				mSocket.send(userSendMessage);
				System.out.println(auxMessage);


			} catch (IOException e) {
				e.printStackTrace();
			}
			if(auxMessage.equals(String.valueOf(0))) {
				leaveChat = true;
			}
			//System.out.println(auxMessage.split(";")[1]);

			userMessage = "";
		} while(!leaveChat);

	}

	public static void listenRoom(MulticastSocket mSocket) {
		new Thread(() -> {
			while(true) {
				byte[] buffer = new byte[1000];
				DatagramPacket receivedMessage = new DatagramPacket(buffer, buffer.length);
				try {
					mSocket.receive(receivedMessage);
					String userMessage = new String(receivedMessage.getData());
					String[] splitedMessage = userMessage.split(";");
					System.out.println(splitedMessage[0] + ": " + splitedMessage[1]);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void enterRoom(String roomMessage){

		String[] splitedMessage = roomMessage.split(";");
		String ipAddress = splitedMessage[0];

		String usersList = splitedMessage[1];
		try {
		    InetAddress roomGroup = InetAddress.getByName(ipAddress);
			MulticastSocket mSocket = new MulticastSocket(MULTCAST_PORT);
			mSocket.joinGroup(roomGroup);


			System.out.println("Bem vindo à sala " + userName + "!");
			System.out.println("Membros: " + usersList);
			System.out.println("Digite sua fala: ");
			listenRoom(mSocket);


			sendMessage(mSocket, roomGroup);


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
