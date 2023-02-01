package photosPck;

import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Synch;
import model.Tag;
import model.User;

/**
 *  Steven Loporto
 *
 */
public class UserHomeScreenController {

	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	public ArrayList<User> userList;
	private User user;
	private ArrayList<Album> albumList;
	private Album currentAlbum;

	@FXML
	private TextField albumNameTextField;

	@FXML
	private Text welcomeBanner;

	// Adjust Type
	@FXML
	private ListView<Album> albumsListView;

	@FXML
	private CheckBox andCheckBox;

	@FXML
	private Button createAlbumButton;

	@FXML
	private Button deleteSelectedAlbumButton;

	@FXML
	private TextField endDateTextField;

	@FXML
	private Button exitButton;

	@FXML
	private Button logoutButton;

	@FXML
	private Button openSelectedAlbumButton;

	@FXML
	private CheckBox orCheckBox;

	@FXML
	private Button renameAlbumButton;

	@FXML
	private AnchorPane scenePane;

	@FXML
	private Button searchButton;

	@FXML
	private TextField startDateTextField;

	@FXML
	private TextField tag1TextField;

	@FXML
	private TextField tag2TextField;

	@FXML
	private TextField val1TextField;

	@FXML
	private TextField val2TextField;

	static User currentUser;

	/**
	 * Imports the data from the previous view. Used to set variables before
	 * initialize is called by the loader
	 * 
	 * @param u  the current user
	 * @param ul the full user list
	 */
	public void pipeline(User u, ArrayList<User> ul) {
		this.userList = Synch.userFileToList();
		for (User x : userList) {
			if (x.equals(u)) {
				user = x;
			}
		}
		albumList = user.getAlbumList();
		ObservableList<Album> albums = FXCollections.observableArrayList();
		for (Album a : albumList) {
			albums.add(a);
		}
		albumsListView.setItems(albums);
		albumsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Album>() {
			@Override
			public void changed(ObservableValue<? extends Album> arg0, Album arg1, Album arg2) {
				currentAlbum = albumsListView.getSelectionModel().getSelectedItem();
			}
		});
		welcomeBanner.setText("" + user + "'s Home Page");

	}

	/**
	 * @return an ArrayList of current Users
	 */
	public ArrayList<User> getUserList() {
		return userList;
	}

	/**
	 * Deletes the selected album
	 * 
	 * @param event delete button press
	 */
	@FXML
	public void deleteSelectedAlbum(ActionEvent event) {
		if (currentAlbum == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Album Selected");
			alert.setContentText("You Must Select an Album to Delete");
			alert.show();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Selected Album Confirmation");
		alert.setContentText("Are You sure you want to delete Album");

		if (alert.showAndWait().get() == ButtonType.OK) {
			user.deleteAlbum(currentAlbum);
			albumList = user.getAlbumList();
			ObservableList<Album> albums = FXCollections.observableArrayList();
			for (Album a : albumList) {
				albums.add(a);
			}
			albumsListView.setItems(albums);
			Synch.userListToFile(userList);

		}

	}

	/**
	 * Opens the selected album
	 * 
	 * @param event open button press
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void openSelectedAlbum(ActionEvent event) throws IOException {
		if (currentAlbum == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Album Selected");
			alert.setContentText("You Must Select an Album to Open");
			alert.show();
			return;
		}
		// Switches scene to albumView users

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OpenAlbum.fxml"));
		root = loader.load();

		OpenAlbumController openAlbumController = loader.getController();

		OpenAlbumController.currentAlbum = currentAlbum;
		openAlbumController.albumNameTextField.setText(currentAlbum.getName());
		openAlbumController.initalize();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(200);
		stage.setY(100);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * creates an album with the inputed name
	 * 
	 * @param event create button press
	 */
	@FXML
	public void createAlbum(ActionEvent event) {
		Album nw = new Album(null, albumNameTextField.getText());
		if (user.getAlbumList().contains(nw)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Name");
			alert.setContentText("An album already exists with that name");
			alert.show();
			return;
		}
		// Confirmation for create album
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Create Album Confirmation");
		alert.setContentText("Are You sure you want to Create Album");

		if (alert.showAndWait().get() == ButtonType.OK) {
			currentAlbum = null;

			user.addAlbum(nw);
			ObservableList<Album> albums = FXCollections.observableArrayList();
			for (Album a : albumList) {
				albums.add(a);
			}
			albumsListView.setItems(albums);
			albumNameTextField.setText("");
			Synch.userListToFile(userList);
		}

	}

	/**
	 * renames the selected album to the inputed name
	 * 
	 * @param event rename button press
	 */
	@FXML
	public void renameAlbum(ActionEvent event) {
		if (currentAlbum == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Album Selected");
			alert.setContentText("You Must Select an Album to Rename");
			alert.show();
			return;
		}
		if (user.getAlbumList().contains(new Album(null, albumNameTextField.getText()))) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Name");
			alert.setContentText("An album already exists with that name");
			alert.show();
			return;
		}
		// Confirmation for renaming album
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Rename Selected Album Confirmation");
		alert.setContentText("Are You sure you want to Rename Album");

		if (alert.showAndWait().get() == ButtonType.OK) {
			currentAlbum.setName(albumNameTextField.getText());
			ObservableList<Album> albums = FXCollections.observableArrayList();
			for (Album a : albumList) {
				albums.add(a);
			}
			albumsListView.setItems(albums);
			albumNameTextField.setText("");
			Synch.userListToFile(userList);
		}

	}

	/**
	 * Searches for photos meeting the given criteria
	 * 
	 * @param event search button press
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void search(ActionEvent event) throws IOException {
		// Switches scene to Log in
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SearchedPhotos.fxml"));
		root = loader.load();
		Tag tag1 = null, tag2 = null;
		SearchedPhotosController controller = loader.getController();
		controller.user = user;
		if (!tag1TextField.getText().equals("") && !val1TextField.getText().equals("")) {
			tag1 = new Tag(tag1TextField.getText(), val1TextField.getText());
		}
		if (!tag2TextField.getText().equals("") && !val2TextField.getText().equals(""))
			tag2 = new Tag(tag2TextField.getText(), val2TextField.getText());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		FileTime day1 = null, day2 = null;
		if (!startDateTextField.getText().equals("") && !startDateTextField.getText().equals("")) {
			try {
				day1 = FileTime.from(dateFormat.parse(startDateTextField.getText()).toInstant());
				day2 = FileTime.from(dateFormat.parse(endDateTextField.getText()).toInstant());
			} catch (ParseException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Date Not Formatted Correctly");
				alert.setContentText("Please try reformatting your date");
				alert.show();
				return;
			}
		}
		String operator = null;
		if (orCheckBox.isSelected()) {
			operator = "or";
		} else if (andCheckBox.isSelected()) {
			operator = "and";
		}
		controller.pipeline(tag1, tag2, day1, day2, operator);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(200);
		stage.setY(100);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * @param event and button press
	 */
	@FXML
	public void setAndValue(ActionEvent event) {
		orCheckBox.setSelected(false);
	}

	/**
	 * @param event or button press
	 */
	@FXML
	public void setOrValue(ActionEvent event) {
		andCheckBox.setSelected(false);
	}

	/**
	 * exits and terminates the program
	 * 
	 * @param event exit button press
	 */
	@FXML
	public void exit(ActionEvent event) {
		Synch.userListToFile(userList);
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();

	}

	/**
	 * Logs the user out and returns them to the login screen
	 * 
	 * @param event logout button press
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void logout(ActionEvent event) throws IOException {
		Synch.userListToFile(userList);
		// Switches scene to Log in
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(400);
		stage.setY(200);
		stage.setScene(scene);
		stage.show();

	}

}
