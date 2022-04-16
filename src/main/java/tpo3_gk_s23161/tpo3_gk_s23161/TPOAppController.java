package tpo3_gk_s23161.tpo3_gk_s23161;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class TPOAppController {

    @FXML
    private ChoiceBox<String> chooseLanguageBox;
    @FXML
    private Label textResultLabel;
    @FXML
    private TextField textFieldToTranslate;

    public ArrayList<String> language = new ArrayList<>();
}