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
    ServerSocket serverSocket; //слушает сообщения

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer (){
        try {
            while (!serverSocket.isClosed()){
               Socket socket = serverSocket.accept();
                System.out.println("Клиент подключен");
                Connection connection = new Connection(socket);

                Thread thread = new Thread(connection);
                thread.start();
            }
        }catch (IOException e){

        }

    }

    public void closeServer(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}