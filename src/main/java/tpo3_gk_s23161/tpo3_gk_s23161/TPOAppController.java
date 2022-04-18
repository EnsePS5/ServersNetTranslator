package tpo3_gk_s23161.tpo3_gk_s23161;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class TPOAppController implements Initializable {

    @FXML
    private ChoiceBox<String> chooseLanguageBox;
    @FXML
    private Label textResultLabel;
    @FXML
    private TextField textFieldToTranslate;

    private static final int serverPort = 9999;
    private static final String serverIp = "localhost";

    private String currentLanguage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            String[] availableLanguages = sendToServer("", "GET_LANGUAGES").split(":");
            chooseLanguageBox.getItems().addAll(availableLanguages);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void translate() throws IOException {

        currentLanguage = chooseLanguageBox.getValue();

        String toTranslate = textFieldToTranslate.getText();
        textResultLabel.setText(sendToServer(toTranslate,"TRANSLATE"));

    }

    private String sendToServer(String message,String command) throws IOException {

        Socket translationSocket = new Socket(serverIp,serverPort);

        PrintWriter printWriter = new PrintWriter(translationSocket.getOutputStream(),true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(translationSocket.getInputStream()));

        printWriter.println(command + "\n" + currentLanguage + "\n" + message);

        StringBuilder result = new StringBuilder(bufferedReader.readLine());

        String line;

        while ((line = bufferedReader.readLine()) != null){
            result.append(line).append(":");
        }

        translationSocket.close();
        bufferedReader.close();

        return result.toString();
    }
}