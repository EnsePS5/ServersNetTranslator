package tpo3_gk_s23161.tpo3_gk_s23161;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslatorMainServer {

    private static final int SERVER_PORT = 9999;


    public static void main(String[] args) throws IOException {

        Map<String, Integer> languageMap = new HashMap<>();
        ArrayList<String> mapKeys = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        while (true){

            ExecutorService threadPool = Executors.newFixedThreadPool(20);
            Socket connectionSocket = serverSocket.accept();

            final int clientPort = connectionSocket.getLocalPort();

            threadPool.submit(() -> {

                try {

                    BufferedReader comingCommand = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    String line;
                    String command = comingCommand.readLine();

                    switch (command){
                        case "GET_LANGUAGES":

                            StringBuilder response = new StringBuilder();
                            for (String mapKey : mapKeys)
                                response.append(mapKey).append("\n");

                            sendMessage(response.toString(),connectionSocket);
                            break;

                        case "TRANSLATE":

                            int portToTranslateFrom = languageMap.get(comingCommand.readLine());
                            Socket translationSocket = new Socket("localhost",portToTranslateFrom);
                            BufferedReader translationBufferedReader =
                                    new BufferedReader(new InputStreamReader(translationSocket.getInputStream()));

                            sendMessage(comingCommand.readLine(),"TRANSLATE",translationSocket);


                            line = translationBufferedReader.readLine();
                            sendMessage(line,connectionSocket);

                            break;

                        case "ADD_TO_LIST":

                            String line1;
                            while ((line = comingCommand.readLine()) != null && (line1 = comingCommand.readLine()) != null) {
                                mapKeys.add(line);
                                languageMap.put(line,Integer.parseInt(line1));

                                System.out.println(line);
                                System.out.println(line1);
                            }
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    private static void sendMessage(String message,Socket socket) throws IOException {

        PrintWriter sendingMes = new PrintWriter(socket.getOutputStream(),true);

        sendingMes.println(message);

        sendingMes.close();
    }

    private static void sendMessage(String message, String command, Socket socket) throws IOException {

        PrintWriter sendingMes = new PrintWriter(socket.getOutputStream(),true);

        sendingMes.println(command + "\n" + message);

        //sendingMes.close();

    }
}
