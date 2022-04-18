package tpo3_gk_s23161.tpo3_gk_s23161;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LanguageServer {

    private static int serverPort = 0;
    private static int languageServerPort = 0;
    private static Map<String,String> dictionary = new HashMap<>();

    public static void main(String[] args) throws IOException {

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
        dictionary = fileReadAndPackToMap(fileWithTranslations);
        sendMessage("ADD_TO_LIST\n" + languageCode + "\n" + languageServerPort);

        ServerSocket languageServerSocket = new ServerSocket(languageServerPort);

        while (true){

            ExecutorService threadPool = Executors.newFixedThreadPool(20);
            Socket connectionSocket = languageServerSocket.accept();

            threadPool.submit(() -> {

                try {

                    BufferedReader comingCommand = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    String command = comingCommand.readLine();

                    switch (command){
                        case "TRANSLATE":
                            String line = comingCommand.readLine();

                            if (dictionary.containsKey(line)) {
                                String response = dictionary.get(line);
                                sendMessage(response,connectionSocket);
                            }else
                                throw new Exception("Words is not found in dictionary");

                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }
    private static void sendMessage(String message,Socket socket) throws IOException {

        PrintWriter sendingMes = new PrintWriter(socket.getOutputStream());

        sendingMes.println(message);

        sendingMes.close();
    }
    private static void sendMessage(String message) throws IOException {

        Socket sendingSocket = new Socket("localhost",serverPort);

        PrintWriter sendingMes = new PrintWriter(sendingSocket.getOutputStream());
        sendingMes.println(message);

        sendingMes.close();
        sendingSocket.close();
    }
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
