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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Photo;
import model.Tag;

/**
 * Steven Loporto
 *
 */
public class SequenceViewController {

	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	@FXML
	private Button exitButton;

	@FXML
	private Button homeButton;

	@FXML
	private Text captionText;

	@FXML
	private Text dateText;

	@FXML
	private Button logoutButton;

	@FXML
	private Button nextPhotoButton;

	@FXML
	private ImageView photoImageView;

	@FXML
	private Button previousPhotoButton;

	@FXML
	private ListView<Tag> tagsListView;

	@FXML
	private AnchorPane scenePane;

	@FXML
	private Button changeViewButton;

	public Photo currentPhoto = OpenAlbumController.currentPhoto;

	public Tag currentTag;

	public ArrayList<Tag> tagList;

	/**
	 * Sets the current photo that is being looked at
	 * 
	 * @param currentPhoto the photo to be set
	 */
	public void setPhoto(Photo currentPhoto) {
		this.currentPhoto = currentPhoto;
	}

	/**
	 * sets the views and other UI objects with the correct info
	 */
	@FXML
	public void initialize() {
		if (currentPhoto == null) {
			currentPhoto = OpenAlbumController.currentAlbum.getPhotosList().get(0);
		}
		photoImageView.setImage(currentPhoto.getImage());
		captionText.setText(currentPhoto.getCaption());
		tagList = currentPhoto.getTags();
		ObservableList<Tag> tags = FXCollections.observableArrayList();
		for (Tag a : tagList) {
			tags.add(a);
		}
		tagsListView.setItems(tags);

		captionText.setText(currentPhoto.getCaption());

		dateText.setText(currentPhoto.getDate().toString());

	}

	/**
	 * Advance to the next photo
	 * 
	 * @param event next button pressed
	 */
	@FXML
	public void goToNextPhoto(ActionEvent event) {
		int currentIndex = OpenAlbumController.currentAlbum.getPhotosList().indexOf(currentPhoto);
		if ((currentIndex + 1) <= (OpenAlbumController.currentAlbum.getPhotosList().size() - 1)
				&& OpenAlbumController.currentAlbum.getPhotosList().get(currentIndex + 1) != null) {
			currentPhoto = OpenAlbumController.currentAlbum.getPhotosList().get(currentIndex + 1);
			initialize();
		}

	}

	/**
	 * Go back to the previous photo
	 * 
	 * @param event previous photo pressed
	 */
	@FXML
	public void goToPreviousPhoto(ActionEvent event) {
		int currentIndex = OpenAlbumController.currentAlbum.getPhotosList().indexOf(currentPhoto);
		if (currentIndex - 1 >= 0 && OpenAlbumController.currentAlbum.getPhotosList().get(currentIndex - 1) != null) {
			currentPhoto = OpenAlbumController.currentAlbum.getPhotosList().get(currentIndex - 1);
			initialize();
		}
	}

	/**
	 * Go back to the opened album screen
	 * 
	 * @param event back button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void changeView(ActionEvent event) throws IOException {

		// Switches scene to albumView users

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OpenAlbum.fxml"));
		root = loader.load();

		OpenAlbumController openAlbumController = loader.getController();
		openAlbumController.initalize();
		openAlbumController.albumNameTextField.setText(openAlbumController.getCurrentAlbum().getName());

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(200);
		stage.setY(100);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Takes the user back to home screen and keeps them logged in
	 * 
	 * @param event home button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void goToUserHomeScreen(ActionEvent event) throws IOException {

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
