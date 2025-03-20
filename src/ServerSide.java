import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide
{
    static PrintStream s = System.out;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(6553);
        System.out.println("Server Listening...");
        Socket whoConnected=serverSocket.accept();
        s.println("Connected Succesfully...");
        DataInputStream dataInputStream=new DataInputStream(whoConnected.getInputStream());
        String msg=dataInputStream.readUTF();

        s.println(msg);
    }
}
