package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import singletonConnection.SingletonConnection;

import java.net.URL;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try(Connection conn = SingletonConnection.getCon();) {
            URL fxmlLocation = getClass().getResource("/application/Login.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML file not found at the specified location.");
            }
            Parent root = FXMLLoader.load(fxmlLocation);
            primaryStage.setTitle("Login and Signup");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (RuntimeException e) {
            System.err.println("Runtime Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception occurred while loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
