package photosPck;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Synch;
import model.User;

/**
 * Steven Loporto
 *
 */
public class AdminController {

	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	@FXML
	private Button addUserButton;

	@FXML
	private TextField addUserTextField;

	@FXML
	private Button deleteUserButton;

	@FXML
	private TextField deleteUserTextField;

	@FXML
	private Button listUsersButton;

	@FXML
	private Button exitButton;

	@FXML
	private Button logoutButton;

	@FXML
	private AnchorPane scenePane;

	private ArrayList<User> list;

	/**
	 * sets the views and other UI objects with the correct info
	 */
	@FXML
	public void initialize() {
		list = Synch.userFileToList();
	}

	/**
	 * adds the given user to the username database
	 * 
	 * @param event add button pressed
	 */
	public void addUser(ActionEvent event) {
		// Confirmation for adding user
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Adding User Confirmation");
		alert.setContentText("Are You sure you want to add user");

		if (alert.showAndWait().get() == ButtonType.OK) {
			User u = new User(addUserTextField.getText(), null);
			if (list.contains(u)) {
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.show();
				alert2.setTitle("duplicate User");
				alert2.setContentText("This user already exists");
				addUserTextField.setText("");
				return;
			} else {
				list.add(u);
				Synch.userListToFile(list);
				addUserTextField.setText("");

			}

		}

	}

	/**
	 * deletes the given user from the user database
	 * 
	 * @param event delete button pressed
	 */
	public void deleteUser(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete User Confirmation");
		alert.setContentText("Are You sure you want to delete user");

		if (alert.showAndWait().get() == ButtonType.OK) {
			User toBeDeleted = null;
			for (User u : list) {
				if (u.getUsername().equals(deleteUserTextField.getText())) {
					toBeDeleted = u;
				}
			}
			if (toBeDeleted != null)
				list.remove(toBeDeleted);
			Synch.userListToFile(list);
			deleteUserTextField.setText("");
		}

	}

	/**
	 * Lists the users in the database in a new screen
	 * 
	 * @param event list button pressed
	 * @throws IOException if the view is not found
	 */
	public void listUsers(ActionEvent event) throws IOException {

		// Switches scene to list users
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserList.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Logs the user out and takes them back to the home page
	 * 
	 * @param event logout button press
	 * @throws IOException if the view is not found
	 */
	public void logout(ActionEvent event) throws IOException {

		// Switches scene to Log in
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * exits and terminates the program
	 * 
	 * @param event exit button press
	 */
	public void exit(ActionEvent event) {
		// Implement befor exit thing
		Synch.userListToFile(list);
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();
	}
}
