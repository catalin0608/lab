import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        // definirea hostului și portului
        String hostName = "localhost";
        int portNumber = 1234;

        // crearea obiectului Socket și BufferedReader pentru a citi inputul utilizatorului
        Socket socket = new Socket(hostName, portNumber);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        // crearea obiectului PrintWriter pentru a trimite mesaje la server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // loop while care permite utilizatorului să introducă mai multe mesaje
        while (true) {
            // citirea mesajului de la utilizator
            System.out.print("Introduceti mesajul: ");
            String message = userInput.readLine();

            // trimiterea mesajului la server
            out.println(message);

            // așteptarea răspunsului de la server și afișarea acestuia
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Mesaj primit de la server: " + in.readLine());
        }
    }
}
