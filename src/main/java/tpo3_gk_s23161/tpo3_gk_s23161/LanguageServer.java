package tpo3_gk_s23161.tpo3_gk_s23161;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LanguageServer {

    //languageServer's variables
    private static int serverPort = 0;
    private static int languageServerPort = 0;
    private static Map<String,String> dictionary = new HashMap<>();

    public static void main(String[] args) throws IOException {

        //read given arguments during server launch
        String languageCode = "";
        String fileWithTranslations = "";

        for (int i = 0; i < args.length; i++){
            switch (args[i]) {
                case "--languagePort", "-lp" -> languageServerPort = Integer.parseInt(args[++i]);
                case "--languageCode", "-lc" -> languageCode = args[++i];
                case "--serverPort", "-sp" -> serverPort = Integer.parseInt(args[++i]);
                case "--translationFilePath", "-tfp" -> fileWithTranslations = args[++i];
            }
        }
        //makes server visible to main server
        dictionary = fileReadAndPackToMap(fileWithTranslations);
        sendMessage("ADD_TO_LIST\n" + languageCode + "\n" + languageServerPort);

        ServerSocket languageServerSocket = new ServerSocket(languageServerPort);

        //server runs endlessly in purpose (task requirement)
        while (true){

            ExecutorService threadPool = Executors.newFixedThreadPool(20);
            Socket connectionSocket = languageServerSocket.accept();

            //submits to threadPool
            threadPool.submit(() -> {

                try {

                    BufferedReader comingCommand = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    String command = comingCommand.readLine();

                    //used switch in order to make new command cases implementation easier
                    switch (command) {
                        case "TRANSLATE" -> {
                            String line = comingCommand.readLine();
                            if (dictionary.containsKey(line)) {
                                String response = dictionary.get(line).toUpperCase(Locale.ROOT);
                                sendMessage(response, connectionSocket);
                            } else
                                sendMessage("WORLD NOT FOUND", connectionSocket);

                        }
                        case "TERMINATE" -> {
                            System.err.println("Given language is already being used!");
                            System.exit(0);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }
    //sends message to given socket
    private static void sendMessage(String message,Socket socket) throws IOException {

        PrintWriter sendingMes = new PrintWriter(socket.getOutputStream());

        sendingMes.println(message);

        sendingMes.close();
    }
    //sends first message to main server to make it visible to client
    private static void sendMessage(String message) throws IOException {

        Socket sendingSocket = new Socket("localhost",serverPort);

        PrintWriter sendingMes = new PrintWriter(sendingSocket.getOutputStream());
        sendingMes.println(message);

        sendingMes.close();
        sendingSocket.close();
    }
    //reads record from a file and packs it into map
    private static Map<String,String> fileReadAndPackToMap(String filePath) throws IOException {

        Map<String,String> result = new HashMap<>();

        File file = new File(filePath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = bufferedReader.readLine()) != null){
            String[] temp = line.split(":");
            result.put(temp[0],temp[1]);
        }

        return result;
    }
}
