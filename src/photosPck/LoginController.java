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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Synch;
import model.User;

/**
 * Steven Loporto
 *
 */
public class LoginController {

	@FXML
	private Parent root;
	private Scene scene;

	private Stage stage;

	@FXML
	private AnchorPane scenePane;

	@FXML
	private Button loginButton;

	@FXML
	private Button exitButton;

	@FXML
	private TextField usernameTextField;

	static ArrayList<User> list;

	/**
	 * sets the views and other UI objects with the correct info
	 */
	@FXML
	public void initialize() {
		list = Synch.userFileToList();
		if (list.isEmpty()) {
			ArrayList<Photo> pl = new ArrayList<Photo>();
			for (int i = 1; i <= 5; i++) {
				pl.add(new Photo("data/stock" + i + ".JPG", null, ""));
			}
			Album a = new Album(pl, "stock");
			ArrayList<Album> l = new ArrayList<Album>();
			l.add(a);
			User u = new User("stock", l);
			list.add(u);
			Synch.photoListToFile(pl);
			Synch.userListToFile(list);
		}
	}

	/**
	 * attempts to log the user in with the given username
	 * 
	 * @param event login button pressed
	 * @throws IOException if the view is not found
	 */
	public void login(ActionEvent event) throws IOException {
		if (usernameTextField.getText().equals("admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin.fxml"));
			root = loader.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} else {
			boolean userExists = false;
			User foundUsername = null;
			for (User u : list) {
				if (u.getUsername().equals(usernameTextField.getText())) {
					foundUsername = u;
					userExists = true;
				}
			}
			if (userExists) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserHomeScreen.fxml"));
				root = loader.load();

				UserHomeScreenController userHomeController = loader.getController();
				userHomeController.pipeline(foundUsername, list);
				UserHomeScreenController.currentUser = foundUsername;

				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("User Not Found");
				alert.setContentText("The username entered is not listed as a current user");
				alert.show();
			}
		}

	}

	/**
	 * exits and terminates the program
	 * 
	 * @param event exit button press
	 */
	public void exit(ActionEvent event) {

		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();

	}

}
