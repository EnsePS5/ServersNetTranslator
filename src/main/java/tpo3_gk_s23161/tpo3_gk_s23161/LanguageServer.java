package tpo3_gk_s23161.tpo3_gk_s23161;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LanguageServer {

    private static int serverPort = 0;
    private static int languageServerPort = 0;

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
        sendMessage("ADD_TO_LIST\n" + languageCode + "\n" + languageServerPort);

        ServerSocket languageServerSocket = new ServerSocket(languageServerPort);


    }
    private static void sendMessage(String message) throws IOException {

        Socket sendingSocket = new Socket("localhost",serverPort);

        PrintWriter sendingMes = new PrintWriter(sendingSocket.getOutputStream());
        sendingMes.println(message);

        sendingMes.close();
        sendingSocket.close();
    }
}
