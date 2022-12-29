import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

public class Message implements Serializable {
    public InetAddress sender;
    private String text;
    private LocalDateTime dateTime;

    public Message(InetAddress sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public InetAddress getSender() {
        return sender;
    }

    public void setSender(InetAddress sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateTime() {
        dateTime = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "SimpleMessage{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

}
