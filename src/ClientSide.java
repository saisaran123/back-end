import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide
{

    public static void main(String[] args) throws IOException, InterruptedException {
        String hostName="localhost";
        int port=6553;
        Socket socket=new Socket(hostName,port);
        DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("hello Server");
    }
}
