package server.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    private static ServerSocket server;

    public static void main(String[] args) throws IOException
    {
        try {
            server = new ServerSocket(8081);
            System.out.println("Сервер подключен, ожидает клиента...");
            while (true) {
                Socket socket = server.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                System.out.println("Клиент подключен!");
            }
        } catch (IOException e) {
            System.out.println("Что то пошло не так... Сервер закрыт.");
        } finally {
            server.close();
        }
    }
}
