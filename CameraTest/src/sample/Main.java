package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Camera Test");
        primaryStage.setScene(new Scene(root, 2625, 775));
        primaryStage.show();
        /*stage.setOnCloseRequest(event -> {
            Controller.getVideoCapture().release();
            Controller.getScheduledExecutorService().shutdown();
        });*/


    }


    public static void main(String[] args) {
        launch(args);
    }
}
