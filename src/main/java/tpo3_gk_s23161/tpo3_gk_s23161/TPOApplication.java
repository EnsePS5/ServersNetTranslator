package tpo3_gk_s23161.tpo3_gk_s23161;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TPOApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TPOApplication.class.getResource("TPOFX.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TPO-Translator");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}