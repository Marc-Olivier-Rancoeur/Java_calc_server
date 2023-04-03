import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Skeleton {
    private final int port;

    public Skeleton(int port){ // Constructeur de la classe
        this.port = port; // définition du port de service par le constructeur
    }

    public void main() throws IOException, ClassNotFoundException { // Fonction pricipale gérant le serveur
        System.out.println("Start server on port : " + port);
        // attributs privés nécessaires au fonctionnement
        ServerSocket serverSocket = new ServerSocket(port);
        while(true){

            System.out.println("Waiting for request");
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message)inputStream.readObject();
            System.out.println("Message received !");

            if(message.getCommande().equalsIgnoreCase("exit")) {
                System.out.println("Closing server");
                inputStream.close();
                socket.close();
                break;
            }

            String resultat;
            switch (message.getCommande()) {
                case "ADD" ->
                        resultat = Integer.toString(Calculatrice.ADD(message.getChiffre1(), message.getChiffre2()));
                case "SUB" ->
                        resultat = Integer.toString(Calculatrice.SUB(message.getChiffre1(), message.getChiffre2()));
                case "MUL" ->
                        resultat = Integer.toString(Calculatrice.MUL(message.getChiffre1(), message.getChiffre2()));
                case "DIV" ->
                        resultat = Float.toString(Calculatrice.DIV(message.getChiffre1(), message.getChiffre2()));
                default ->
                    resultat = "ERREUR";
            }

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("RES:" + resultat);
            inputStream.close();
            outputStream.close();
            socket.close();

        }
        serverSocket.close();
    }
}