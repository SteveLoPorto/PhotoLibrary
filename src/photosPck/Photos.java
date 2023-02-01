package photosPck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Photos extends Application {

	/**
	 *start the Login controller
	 */
	@Override
	public void start(Stage stage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

			stage.setOnCloseRequest(event -> exit(stage));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * prompts the user to confirm their desire to exit
	 * @param stage exit button pressed
	 */
	public void exit(Stage stage) {
		// This Works
		// Confirmation for logout user
		// Implement befor exit thing
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit User Confirmation");
		alert.setContentText("Are You sure you want to Exit user");

		if (alert.showAndWait().get() == ButtonType.OK) {
			stage.close();
		}

	}

	/**
	 * main method
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
