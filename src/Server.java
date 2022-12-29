import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Server class
class Server {
    // основной поток, поток отправитель сообщений (один), по одному потоку на каждое подключение
    // добавить коллекцию подключений типа коннекшн
    //должна быть блокирующая очередь для сообщений
    //потоки удобнее написать во внутреннем классе
    static List<Connection> connectionList = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);


            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept(); //s

                // Displaying that new client is connected
                // to server
                System.out.println("Новый клиент подключен"
                        + client.getInetAddress()
                        .getHostAddress());

                DataInputStream dis = new DataInputStream(client.getInputStream());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());

                // create a new thread object
                Connection <Message> clientSock = new Connection<>(client);
                // This thread will handle the client
                // separately
                Thread t = new Thread(clientSock);
                connectionList.add(clientSock);

                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}