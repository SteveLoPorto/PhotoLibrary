package photosPck;

import java.io.File;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Synch;
import model.Tag;
import model.User;

/**
 *  Steven Loporto
 *
 */
public class AddPhotoController {

	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	@FXML
	private Button addPhotoButton;

	@FXML
	private Button addTagButton;

	@FXML
	private Button backToOpenAlbumButton;

	@FXML
	private TextField captionTextField;

	@FXML
	private TextField dateTextField;

	@FXML
	private Button exitButton;

	@FXML
	private Button homeButton;

	@FXML
	private Button logoutButton;

	@FXML
	private ImageView photoImageView;

	@FXML
	private AnchorPane scenePane;

	//@FXML
	//private TextField tagTextField;

	//@FXML
	//private ListView<Tag> tagsListView;

	//@FXML
	//private TextField valTextField;

	@FXML
	private Button addToAlbumButton;

	public Photo addedPhoto = null;

	public ArrayList<Tag> tagList = new ArrayList<Tag>();

	public User currentUser;

	/**
	 * Opens a page for the user to select a photo
	 * 
	 * @param event add button pressed
	 * @throws IOException if the file is not found
	 */
	@FXML
	public void addPhoto(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.setInitialDirectory(new File("data"));

		File file = fileChooser.showOpenDialog(stage);
		// If a photo gets added
		if (file != null) {
			String fileName = file.getName();

			if ((fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length())).toLowerCase().equals("bmp")
					|| (fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length())).toLowerCase()
							.equals("jpeg")
					|| (fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length())).toLowerCase()
							.equals("png")
					|| (fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length())).toLowerCase()
							.equals("gif")
					|| (fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length())).toLowerCase()
							.equals("jpg")) {
				String filePath = file.getPath();
				Image addedImage = new Image("file:" + filePath);
				photoImageView.setImage(addedImage);
				addedPhoto = new Photo(filePath, null, null);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong FileType");
				alert.setContentText("You Must Select an .jpeg, .jpg, .bmp, .gif, or .png ");
				alert.show();
				return;
			}
		}

	}

	/**
	 * Adds the given tag to the photo
	 * 
	 * @param event add tag button pressed
	 */
	/*
	@FXML
	public void addTag(ActionEvent event) {
		if (addedPhoto == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Photo");
			alert.setContentText("You Must Add a photo to set tags");
			alert.show();
			return;
		} else {
			String tag = tagTextField.getText();
			String val = valTextField.getText();
			Tag addedTag = new Tag(tag, val);
			tagList.add(addedTag);
			addedPhoto.addTag(tag, val);

			ObservableList<Tag> tags = FXCollections.observableArrayList();
			for (Tag a : tagList) {
				tags.add(a);
			}
			tagsListView.setItems(tags);
			tagTextField.clear();
			valTextField.clear();
		}
	}
	*/

	/**
	 * Adds the photo to the given album and returns the user to the album page
	 * 
	 * @param event submit button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void addNewPhotoToAlbum(ActionEvent event) throws IOException {

		Alert alert = null;
		boolean successfulAdd;
		boolean flag = false;
		if(addedPhoto== null) {
		 successfulAdd = false;
		 flag = true;
		}else {
		 successfulAdd = OpenAlbumController.currentAlbum.addToPhotoList(addedPhoto);
		 addedPhoto.setCaption(captionTextField.getText());
		}
		
		if (successfulAdd == false ) {
			if(flag == false) {
			 alert = new Alert(AlertType.ERROR);
			alert.setTitle("Duplcate Photo To album");
			alert.setContentText("You enter a duplicate photo to the same album");
			}else {
				 alert = new Alert(AlertType.ERROR);
				alert.setTitle("No photo selected");
				alert.setContentText("No photo selected");
			}
		}else {

		
		ArrayList<Photo> newList = Synch.photoFileToList();
		newList.add(addedPhoto);
		Synch.photoListToFile(newList);

		ArrayList<User> newUserList = Synch.userFileToList();
		for (User u : newUserList) {
			for (Album a : u.getAlbumList()) {
				if (u.getUsername().equals(OpenAlbumController.user.getUsername())
						&& a.getName().equals(OpenAlbumController.currentAlbum.getName())) {
					a.addToPhotoList(addedPhoto);
				}
			}
		}
		Synch.userListToFile(newUserList);
		
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OpenAlbum.fxml"));
		root = loader.load();

		OpenAlbumController openAlbumController = loader.getController();
		openAlbumController.albumNameTextField.setText(openAlbumController.getCurrentAlbum().getName());
		openAlbumController.initalize();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(200);
		stage.setY(100);
		stage.setScene(scene);
		stage.show();
		if(alert != null) {
			alert.show();
		}


	}

	/**
	 * Returns the user to the album without creating a new photo
	 * 
	 * @param event back button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void backToOpenAlbum(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OpenAlbum.fxml"));
		root = loader.load();

		OpenAlbumController openAlbumController = loader.getController();

		openAlbumController.albumNameTextField.setText(openAlbumController.getCurrentAlbum().getName());
		openAlbumController.initalize();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(200);
		stage.setY(100);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * returns the user home without adding the photo
	 * 
	 * @param event home button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void goToUserHomeScreen(ActionEvent event) throws IOException {

		// Switches scene to Log in
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserHomeScreen.fxml"));
		root = loader.load();

		UserHomeScreenController userHomeController = loader.getController();
		userHomeController.pipeline(UserHomeScreenController.currentUser, LoginController.list);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setX(400);
		stage.setY(200);
		stage.show();
	}

	/**
	 * Logs the user out and takes them back to the home page
	 * 
	 * @param event logout button press
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void logout(ActionEvent event) throws IOException {

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

	/**
	 * exits and terminates the program
	 * 
	 * @param event exit button press
	 */
	@FXML
	public void exit(ActionEvent event) {

		// Implement before exit thing
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();

	}

}
