package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Game game;

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        stage.getIcons().add(new Image("file:src/data/img/bull.png"));
        stage.setTitle("Bulls+Cows");
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
        game = new Game();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
