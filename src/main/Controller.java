package main;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextField textField;
    public TextArea textArea;
    private boolean started;
    private double time;
    private int counter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) textField.setText(newValue.replaceAll("[^\\d]", ""));
            if (textField.getText().length() > 4) textField.setText(textField.getText().substring(0, 4));
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && textField.getText().length() == 4) {
                if (!started) {
                    started = true;
                    counter = 0;
                    time = System.nanoTime();
                }
                int guess = Integer.parseInt(textField.getText());
                int[] bnc = Main.game.getSolution(guess);
                if (bnc != null) {
                    counter++;
                    String append = String.format("%s |   %s                 | %s Bulls, %s Cows\n", counter, guess, bnc[0], bnc[1]);
                    textArea.appendText(append);
                    textField.setText("");
                    if (bnc[0] == 4) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("You did it!");
                        alert.setHeaderText("Your guess was correct");
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(
                                new Image("file:src/data/img/bull.png"));
                        double deltaTime = (System.nanoTime() - time) / Math.pow(10, 9);
                        String msg = String.format("Secret really was %s, and it took you %.1f seconds to guess it!",
                                guess, deltaTime);
                        alert.setContentText(msg);
                        alert.showAndWait();
                        restart(new ActionEvent());
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("You've messed up");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(
                            new Image("file:src/data/img/bull.png"));
                    alert.setContentText("All digits must be different and no 0s, remember?");
                    alert.showAndWait();

                }
            }
        });

    }

    public void restart(ActionEvent _) {
        textField.setText("");
        textArea.setText("");
        started = false;
        Main.game = new Game();
    }

    public void fail(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You gave up!");
        alert.setHeaderText("Better luck next time");
        alert.setContentText("Answer was: " + Main.game.showSolution());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image("file:src/data/img/bull.png"));
        alert.showAndWait();
        restart(actionEvent);
    }

    public void showAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hello!");
        alert.setHeaderText("Info");
        alert.setContentText("Created by Oleh Kuzan, Kyiv-Mohyla Academy, 2021");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image("file:src/data/img/bull.png"));
        alert.showAndWait();
        restart(actionEvent);
    }

    public void showRules(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rules");
        alert.setHeaderText("Here's how to play");
        alert.setContentText("You have to guess four-digit number, digits are all different. Computer will show your how many you've got in right position (bulls), and how many are displaced (cows). Good luck!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image("file:src/data/img/bull.png"));
        alert.showAndWait();
        restart(actionEvent);
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
