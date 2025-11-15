import java.io.*;
import java.net.*;
import java.util.Scanner;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ChatClient
 * -----------
 * Connects to the ChatServer using a socket and allows the user to
 * send and receive messages in real-time. The program uses threads
 * so that incoming messages can be displayed while the user types.
 */
public class ChatClient {
    public static void main(String[] args) {
        try {
            // Connect to the server running on localhost (127.0.0.1) at port 5000
            Socket socket = new Socket("127.0.0.1", 5000);
            System.out.println("✅ Connected to chat server!");

            // Input stream: reads messages from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Output stream: sends messages to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Scanner: reads user input from the keyboard
            Scanner scanner = new Scanner(System.in);

            /**
             * 🧵 Thread for receiving messages
             * -------------------------------
             * This background thread continuously listens for messages
             * coming from the server and prints them to the console.
             * It runs independently from the main thread.
             */
            new Thread(() -> {
                String message;
                try {
                    // Keep reading messages from the server
                    while ((message = in.readLine()) != null) {
                        System.out.println("💬 " + message);
                    }
                } catch (IOException e) {
                    System.out.println("❌ Connection closed.");
                }
            }).start();

            /**
             * 💬 Main thread for sending messages
             * -----------------------------------
             * Continuously reads user input from the keyboard and
             * sends it to the server so that everyone can see it.
             */
            while (true) {
                System.out.print("You: ");
                String msg = scanner.nextLine();
                out.println(msg);
            }

        } catch (IOException e) {
            // If connection fails or is interrupted
            System.out.println("⚠️ Cannot connect to server: " + e.getMessage());
        }
    }
}
