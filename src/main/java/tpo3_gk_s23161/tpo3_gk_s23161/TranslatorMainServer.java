package tpo3_gk_s23161.tpo3_gk_s23161;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslatorMainServer {

    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        while (true){

            ExecutorService threadPool = Executors.newFixedThreadPool(20);
            Socket connectionSocket = serverSocket.accept();

            threadPool.submit(() -> {

                Map<String, Integer> languageMap = new HashMap<>();

                try {

                    BufferedReader comingCommand = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    String line;
                    String command = comingCommand.readLine();

                    switch (command){
                        case "TRANSLATE":

                            break;
                        case "TRANSLATED":
                            while ((line = comingCommand.readLine()) != null){
                                System.out.println(line);
                            }
                            break;
                        case "ADD_TO_LIST":
                            String line1;
                            while ((line = comingCommand.readLine()) != null && (line1 = comingCommand.readLine()) != null) {
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

    public void addLanguage(){

    }
}
