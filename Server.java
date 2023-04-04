import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 1234;
    public static List<ClientHandler> clients = new ArrayList<ClientHandler>();

    public static void main(String[] args) {
        try {
            // Crearea serverului pe un port specificat
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serverul s-a pornit cu portul " + PORT);

            while (true) {
                // Așteptăm conexiuni noi de la clienți și creăm un ClientHandler nou pentru fiecare
                Socket clientSocket = serverSocket.accept();
                System.out.println("Opa, un client nou conectat *BINE AI VENIT*");

                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                new Thread(client).start(); // pornim thread-ul clientului
            }
        } catch (IOException e) {
            System.out.println("Error while starting server: " + e.getMessage());
        }
    }

    // Aceasta este clasa ClientHandler care va fi utilizată pentru a gestiona fiecare client conectat
    public static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out; // pentru a trimite mesaje la client
        private BufferedReader in; // pentru a primi mesaje de la client

        // Constructorul ClientHandler care primește un socket de la client
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Inițializarea stream-urilor de I/O pentru citirea și scrierea datelor
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) { // citim mesajele de la client
                    System.out.println("Mesaj trimis de catre client -> " + inputLine);

                    // Transmitem mesajul primit la toți ceilalți clienți conectați la server
                    for (ClientHandler client : Server.clients) {
                        client.out.println(inputLine);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                // Închidem socket-ul clientului curent
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}
