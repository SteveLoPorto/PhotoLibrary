package photosPck;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Synch;
import model.User;

/**
 *  Steven Loporto
 *
 */
public class UserListController {
	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	@FXML
	private AnchorPane scenePane;

	@FXML
	private Button backButton;

	@FXML
	private Button exitButton;

	@FXML
	private Button logoutButton;

	@FXML
	// Edit Type
	private ListView<String> usersListView;

	/**
	 * initialize the variables and FXML objects
	 */
	@FXML
	public void initialize() {
		ArrayList<User> l = Synch.userFileToList();
		ObservableList<String> users = FXCollections.observableArrayList();
		for (User u : l) {
			users.add(u.getUsername());
		}
		usersListView.setItems(users);
	}

	/**
	 * Takes the user back to the admin home page
	 * 
	 * @param event back button press
	 * @throws IOException if the view is not found
	 */
	@FXML
	void goBackToAdmin(ActionEvent event) throws IOException {

		// Switches scene to Admin in
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin.fxml"));
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
	@FXML
	void logout(ActionEvent event) throws IOException {
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
	@FXML
	void exit(ActionEvent event) {
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();
	}

}
