import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Connection <T extends Message> implements Runnable{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    DataInputStream dis;
    DataOutputStream dos;
    Socket s;
    boolean isloggedin;



    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(this.socket.getOutputStream());
        input = new ObjectInputStream(this.socket.getInputStream());
    }

    public void sendMessage (T message) throws IOException {
        message.setDateTime();
        message.setSender(socket.getInetAddress());
        output.writeObject(message);
        output.flush();

    }

    public T readMessage () throws IOException, ClassNotFoundException {
        return (T) input.readObject();
    }


    @Override
    public void run() {
        String received;
        while (true)
        {
            try
            {
                // receive the string
                received = dis.readUTF();

                System.out.println(received);

                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }

                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();

                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (Connection mc : Server.connectionList)
                {
                    // if the recipient is found, write on its
                    // output stream
                    if (socket.getInetAddress().equals(recipient) && mc.isloggedin==true)
                    {
                        mc.dos.writeUTF( MsgToSend);
                        break;
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}

