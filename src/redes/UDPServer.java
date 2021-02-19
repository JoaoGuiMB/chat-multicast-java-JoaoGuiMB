package redes;

import models.Room;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {

	private static List<Room> rooms = new ArrayList<Room>();
	private static DatagramSocket aSocket = null;
	public static void main(String args[]) {

		rooms.add(new Room(1, "Projetos","229.5.2.1" ));
		rooms.add(new Room(2, "Requisitos","230.5.2.1" ));
		rooms.add(new Room(3, "Math","231.0.0.0" ));

		new Thread(() -> {
			try {
				String message;
				aSocket = new DatagramSocket(6789);

				System.out.println("Servidor: ouvindo porta UDP/6789.");
				byte[] buffer = new byte[1000];

				while (true) {
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);

					message = handleReceivedMessage(new String(request.getData()).trim());;

					System.out.println("Servidor: recebido \'" + message  + "\'.");

					byte[] responseData = message.getBytes();
					DatagramPacket reply = new DatagramPacket(responseData, responseData.length, request.getAddress(),
							request.getPort());
					responseData = null;
					aSocket.send(reply);

				}
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			}
		}).start();


	}

	public static String handleReceivedMessage(String message) {
		String[] splitedMessage = message.split(";");
		String option = splitedMessage[0];

		switch (option) {
			case "1":
				return createRoom(message);
			case "2":
				return listRooms(message);
			case "3":
				return getRoomIp(message);
			default:
				return "Fim";
		}

	}

	public static String createRoom(String message) {
		String roomName = message.split(";")[1];
		String lastIpAddress = rooms.get(rooms.size()-1).getIpAddress();
		String[] splitedIpAddress = lastIpAddress.split("\\.");
		int oldIpPart = Integer.parseInt(splitedIpAddress[0]);

		String newIp = String.valueOf(oldIpPart + 1) + ".1.1.5";

		rooms.add(new Room(rooms.size() + 1, roomName, newIp));
		System.out.println("Numéro de salas: " + rooms.size());

		return "Sala criada com sucesso";
	}

	public static String listRooms(String message) {
		String listRoomsData = "";
		for(int i = 0; i < rooms.size(); i++) {
			listRoomsData += rooms.get(i).toString();
		}
		return  listRoomsData;
	}

	public static String getRoomIp(String message) {
		String roomName = message.split(";")[1];
		String userName = message.split(";")[2];

		for(int i = 0; i < rooms.size(); i++) {
			System.out.println(roomName);
			if(roomName.equals(rooms.get(i).getName())) {
				rooms.get(i).addUser(userName);
				String returnRoom = rooms.get(i).getIpAddress() + rooms.get(i).getAllUsers();
				System.out.println(returnRoom);
				return returnRoom;
			}
		}
		return "Room not find";
	}
}
