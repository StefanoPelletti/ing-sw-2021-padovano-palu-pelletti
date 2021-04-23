package it.polimi.ingsw;

import java.net.*;
import java.io.*;
import java.util.*;

import it.polimi.ingsw.Networking.Message.*;

public class ClientApp {

    public static void main( String[] args ) {
        Socket socket;
        List<String> textList = new ArrayList<>();
        String text;
        Message message;
        OutputStream outputStream; // = socket.getOutputStream();
        ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
        InputStream inputStream;// = socket.getInputStream();
        ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);
        Scanner input = new Scanner(System.in);

        boolean quit = false;

        System.out.println("Welcome to MASTER OF RENAISSANCE\n\nWRITE A COMMAND: \n\"QUIT\", \n\"CREATE <ip> <port> <nickname> <capacity>\", \n\"JOIN <ip> <port> <nickname> <lobbyNumber>\"");
        while (!quit) {
            text = input.nextLine();

            textList.clear();
            textList = new ArrayList<>( (Arrays.asList(text.split("\\s+"))));

            switch(textList.get(0))
            {
                case "QUIT":
                    quit = true;
                    break;
                case "CREATE":   // CREATE localhost 43210 Tommaso 4
                    if ( !checkCreateCommand(textList) ) break;
                    try {
                        socket = new Socket(textList.get(1), Integer.parseInt(textList.get(2)));

                        outputStream = socket.getOutputStream();
                        objectOutputStream = new ObjectOutputStream(outputStream);
                        inputStream = socket.getInputStream();
                        objectInputStream = new ObjectInputStream(inputStream);

                        MSG_CREATE_LOBBY m = new MSG_CREATE_LOBBY(Integer.parseInt(textList.get(4)), textList.get(3));
                        System.out.println(m.getMessageType());
                        objectOutputStream.writeObject(m);
                        message = (Message) objectInputStream.readObject();
                        if(message.getMessageType()==MessageType.MSG_OK_CREATE)
                        {
                            MSG_OK_CREATE msg = (MSG_OK_CREATE) message;
                            System.out.println("Your lobby number is "+ msg.getLobbyNumber());
                            Thread.sleep(600000);
                        }
                        else if(message.getMessageType()==MessageType.MSG_ERROR)
                        {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            socket.close();
                            outputStream.close();
                            inputStream.close();
                            objectOutputStream.close();
                            objectInputStream.close();
                        }
                    }
                    catch (IOException | ClassNotFoundException | InterruptedException e)
                    {
                        System.out.println("CLIENT SOCKET ERROR: "+e.getMessage());
                        break;
                    }


                case "JOIN":   // JOIN localhost 1337 Tommaso 256
                    if ( ! checkJoinCommand(textList) ) break;
                    try {
                        socket = new Socket(textList.get(1), Integer.parseInt(textList.get(2)));

                        outputStream = socket.getOutputStream();
                        objectOutputStream = new ObjectOutputStream(outputStream);
                        inputStream = socket.getInputStream();
                        objectInputStream = new ObjectInputStream(inputStream);

                        objectOutputStream.writeObject(new MSG_JOIN_LOBBY(textList.get(3), Integer.parseInt(textList.get(4) )));
                        message = (Message) objectInputStream.readObject();
                        if(message.getMessageType()==MessageType.MSG_OK_JOIN)
                        {
                            MSG_OK_JOIN msg = (MSG_OK_JOIN) message;
                            System.out.println("Your assigned nickname is "+ msg.getAssignedNickname());
                            Thread.sleep(600000);
                        }
                        else if(message.getMessageType()==MessageType.MSG_ERROR)
                        {
                            MSG_ERROR msg = (MSG_ERROR) message;
                            System.out.println(msg.getErrorMessage());
                            socket.close();
                            outputStream.close();
                            inputStream.close();
                            objectOutputStream.close();
                            objectInputStream.close();
                        }
                    }
                    catch (IOException | ClassNotFoundException | InterruptedException e)
                    {
                        System.out.println("CLIENT SOCKET ERROR: "+e.getMessage());
                        break;
                    }
                    break;
            }


        }

    }

    static public boolean checkCreateCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println("mancano dei parametri!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) >= 65536) {
            System.out.println("il numero di porta è troppo potente!!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) <= 1023) {
            System.out.println("il numero di porta dev'essere maggiore di 1023!!");
            return false;
        }
        if (Integer.parseInt(textList.get(4)) > 4) {
            System.out.println("Il numero di giocatori deve essere minore di 5!!");
            return false;
        }
        if (Integer.parseInt(textList.get(4)) < 1)
        {
            System.out.println("il numero di giocatori dev'essere maggiore di 1!");
            return false;
        }
         return true;
    }

    static public boolean checkJoinCommand(List<String> textList) {
        if (textList.size() != 5) {
            System.out.println("mancano dei parametri!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) >= 65536) {
            System.out.println("il numero di porta è troppo potente!!");
            return false;
        }
        if (Integer.parseInt(textList.get(2)) <= 1023) {
            System.out.println("il numero di porta dev'essere maggiore di 1023!!");
            return false;
        }
        if (Integer.parseInt(textList.get(4)) <= -1 || Integer.parseInt(textList.get(4)) >=500) {
            System.out.println("Il numero di lobby dev'essere compreso tra 0 e 500!!");
            return false;
        }
        return true;
    }

}