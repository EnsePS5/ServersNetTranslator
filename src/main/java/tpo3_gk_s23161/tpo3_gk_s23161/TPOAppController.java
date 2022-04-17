package tpo3_gk_s23161.tpo3_gk_s23161;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TPOAppController {

    @FXML
    private ChoiceBox<String> chooseLanguageBox;
    @FXML
    private Label textResultLabel;
    @FXML
    private TextField textFieldToTranslate;

    private static final int serverPort = 9999;
    private static final String serverIp = "localhost";

    public void translate() throws IOException {

        String toTranslate = textFieldToTranslate.getText();
        textResultLabel.setText(sendToServer(toTranslate));

    }
    private static String sendToServer(String message) throws IOException {

        Socket translationSocket = new Socket(serverIp,serverPort);

        PrintWriter printWriter = new PrintWriter(translationSocket.getOutputStream(),true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(translationSocket.getInputStream()));

        printWriter.println("TRANSLATE\n" + "\\TODO ADD CORRECT LANGUAGE CODE" + message);

        String result = bufferedReader.readLine();

        translationSocket.close();
        bufferedReader.close();

        return result;
    }
}