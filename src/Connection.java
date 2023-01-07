import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable{

    public static ArrayList<Connection> connectionsArrayList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private  BufferedWriter bufferedWriter;
    private String clientUserName;

    public Connection (Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            connectionsArrayList.add(this);
            broadcastMessage("Сервер: " + clientUserName + " присоединился");
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage (messageFromClient);
            } catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage (String messageToSend){
        for (Connection connection : connectionsArrayList){
            try {
                if (!connection.clientUserName.equals(clientUserName)){
                    connection.bufferedWriter.write(messageToSend);
                    connection.bufferedWriter.newLine();
                    connection.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeConnection(){
        connectionsArrayList.remove(this);
        broadcastMessage("Сервер: "+ clientUserName + " отключился");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeConnection();
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}

