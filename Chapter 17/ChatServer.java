import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * ChatServer
 * -----------
 * A simple multi-client chat server using sockets and threads.
 * Each connected client is handled by its own thread, allowing
 * multiple users to chat simultaneously.
 */
public class ChatServer {

    // The port number where the server listens for connections
    private static final int PORT = 5000;

    // A thread-safe set to store all connected clients' output streams
    private static Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws Exception {
        System.out.println("💬 Server running on port " + PORT);

        // Create a ServerSocket to listen for client connections
        ServerSocket serverSocket = new ServerSocket(PORT);

        // Infinite loop — keeps the server running and accepting new clients
        while (true) {
            // Wait (block) until a client connects
            Socket clientSocket = serverSocket.accept();

            // Confirmation message on the server console
            System.out.println("✅ New client connected!");

            // Create a new thread to handle this client’s communication
            new ClientHandler(clientSocket).start();
        }
    }

    /**
     * Inner class: ClientHandler
     * --------------------------
     * Handles communication between the server and one specific client.
     * Runs on its own thread so multiple clients can chat at the same time.
     */
    private static class ClientHandler extends Thread {
        private Socket socket;        // Client socket connection
        private PrintWriter out;      // Sends data to the client
        private BufferedReader in;    // Reads data from the client

        // Constructor: gets the socket for the connected client
        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Create input and output streams for the client
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Add this client’s output stream to the set
                clientWriters.add(out);

                String message;
                // Continuously read messages from this client
                while ((message = in.readLine()) != null) {
                    // Show message on the server console
                    System.out.println("📩 " + message);

                    // Send (broadcast) the message to all connected clients
                    for (PrintWriter writer : clientWriters) {
                        writer.println(message);
                    }
                }
            } catch (IOException e) {
                // If something goes wrong (client disconnected, etc.)
                System.out.println("⚠️ Client disconnected: " + e.getMessage());
            } finally {
                // Remove this client from the active set when they disconnect
                clientWriters.remove(out);

                // Close the socket safely
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }
}
