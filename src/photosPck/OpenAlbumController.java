package photosPck;

import java.io.IOException;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Synch;
import model.Tag;
import model.User;

/**
 * Steven Loporto
 *
 */
public class OpenAlbumController {

	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	@FXML
	private Button addPhotoButton;

	@FXML
	private Button addTagsButton;

	@FXML
	private Text dateText;

	@FXML
	private ListView<Album> albumsListView;

	@FXML
	private Button changeViewButton;

	@FXML
	private Button clearTagButton;

	@FXML
	private Button copyPhotoButton;

	@FXML
	private ImageView currentPhotos;

	@FXML
	private Button deletePhotoButton;

	@FXML
	private Button exitButton;

	@FXML
	private Button homeButton;

	@FXML
	private Button logoutButton;

	@FXML
	private Button movePhotoButton;

	@FXML
	private Button editCaptionButton;

	@FXML
	private ListView<Photo> photosListView;

	@FXML
	private Button removeTagsButton;

	@FXML
	private AnchorPane scenePane;

	@FXML
	private TextField tagTextField;

	@FXML
	private ListView<Tag> tagsListView;

	@FXML
	private TextField valTextField;

	@FXML
	private TextField captionTextField;

	@FXML
	public Text albumNameTextField;

	// Needed variables
	static Album currentAlbum;
	static Album destinationAlbum;
	static Photo currentPhoto;
	static Tag currentTag;
	private ArrayList<Photo> photoList;
	private ArrayList<Album> albumList;
	private ArrayList<Tag> tagList;
	static User user;

	/**
	 * sets the views and other UI objects with the correct info
	 */
	public void initalize() {
		// album list
		ArrayList<User> ul = Synch.userFileToList();
		for (User u : ul) {
			if (u.equals(UserHomeScreenController.currentUser)) {
				user = u;
			}
		}
		albumList = user.getAlbumList();
		ObservableList<Album> albums = FXCollections.observableArrayList();
		for (Album a : albumList) {
			if (!a.equals(currentAlbum)) {
				albums.add(a);
			}

		}
		albumsListView.setItems(albums);

		albumsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Album>() {
			@Override
			public void changed(ObservableValue<? extends Album> arg0, Album arg1, Album arg2) {
				destinationAlbum = albumsListView.getSelectionModel().getSelectedItem();
			}
		});

		for (Album a : user.getAlbumList()) {
			if (a.equals(currentAlbum)) {
				currentAlbum = a;
			}
		}
		
		//System.out.println(currentAlbum);

		
		photoList = Synch.getPhotosForAlbum(currentAlbum);

		ObservableList<Photo> photos = FXCollections.observableArrayList();
		for (Photo a : photoList) {
			photos.add(a);

		}

		photosListView.setItems(photos);

		photosListView.setCellFactory(param -> new ListCell<Photo>() {
			private ImageView imageView = new ImageView();

			public void updateItem(Photo name, boolean empty) {
				super.updateItem(name, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					Image currPhotoImage = (name.getImage());
					imageView.setImage(currPhotoImage);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(100);
					imageView.setFitWidth(100);

					setText(name.toString());
					setGraphic(imageView);
				}
			}
		});

		photosListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
			@Override
			public void changed(ObservableValue<? extends Photo> arg0, Photo arg1, Photo arg2) {
				currentPhoto = photosListView.getSelectionModel().getSelectedItem();
				if (currentPhoto != null) {
					currentPhotos.setImage(currentPhoto.getImage());
					currentPhotos.setPreserveRatio(true);
					currentPhotos.setFitHeight(266);
					currentPhotos.setFitWidth(464);

					tagList = currentPhoto.getTags();
					ObservableList<Tag> tags = FXCollections.observableArrayList();
					for (Tag a : tagList) {
						tags.add(a);
					}

					tagsListView.setItems(tags);

					captionTextField.setText(currentPhoto.getCaption());

					dateText.setText(currentPhoto.getDate().toString());

					tagsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tag>() {
						@Override
						public void changed(ObservableValue<? extends Tag> arg0, Tag arg1, Tag arg2) {
							currentTag = tagsListView.getSelectionModel().getSelectedItem();
							if (currentTag != null) {
								tagTextField.setText(currentTag.getTag());
								valTextField.setText(currentTag.getValue());
							}
						}
					});
				} else {
					captionTextField.clear();
					tagsListView.setItems(null);
					currentPhotos.setImage(null);
				}
			}

		});

	}

	/**
	 * current album getter
	 * 
	 * @return the current album
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;

	}

	/**
	 * takes the user to a page to add a new photo to the album
	 * 
	 * @param event add photo button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void addPhoto(ActionEvent event) throws IOException {

		// Switches scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddPhoto.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setX(400);
		stage.setY(200);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * changes the caption to what is currently in the caption field
	 * 
	 * @param event edit button pressed
	 */
	@FXML
	public void editCaption(ActionEvent event) {

		// Switches scene
		currentPhoto.setCaption(captionTextField.getText());
		captionTextField.setText(currentPhoto.getCaption());
		for (Photo p : photoList) {
			if (p.equals(currentPhoto)) {
				p = currentPhoto;
			}
		}
		Synch.photoListToFile(photoList);

	}

	/**
	 * Allows the user to open a new page and see their photos sequentially
	 * 
	 * @param event view button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	public void changeView(ActionEvent event) throws IOException {

		// Switches scene
		if (currentAlbum.getPhotosList().isEmpty() == false) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SequenceView.fxml"));
			root = loader.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setX(400);
			stage.setY(200);
			stage.setScene(scene);
			stage.show();
		} else {

		}

	}

	/**
	 * removes the selected photo from the album
	 * 
	 * @param event delete button pressed
	 */
	@FXML
	public void deletePhoto(ActionEvent event) {

		// Confirmation for deleting photo
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Selected Photo Confirmation");
		alert.setContentText("Are you sure you want to delete photo.");

		if (alert.showAndWait().get() == ButtonType.OK) {
			// Implement Delete photo

			ArrayList<User> newUserList = Synch.userFileToList();
			for (User u : newUserList) {
				for (Album a : u.getAlbumList()) {
					if (u.getUsername().equals(OpenAlbumController.user.getUsername())
							&& a.getName().equals(OpenAlbumController.currentAlbum.getName())) {
						a.removePhotoFromList(currentPhoto);
					}
				}
			}
			Synch.userListToFile(newUserList);

			currentAlbum.removePhotoFromList(currentPhoto);
			currentPhoto = null;
			initalize();
		}
	}

	/**
	 * adds the inputted tag to the selected photo
	 * 
	 * @param event add tag button pressed
	 */
	@FXML
	public void addTag(ActionEvent event) {

		// Confirmation for adding tag
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Add Tag Confirmation");
		alert.setContentText("Are you sure you ant to adding photo.");

		if (alert.showAndWait().get() == ButtonType.OK) {
			// Implement add photo
			String tag = tagTextField.getText();
			String val = valTextField.getText();
			Tag addedTag = new Tag(tag, val);
			if(currentPhoto.getTags().contains(addedTag)) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Tag Duplicate");
				error.setContentText("Tag already exists in photo");
				error.show();
				return;
			}
			currentPhoto.addTag(addedTag);
			currentTag = null;
			for (Photo p : photoList) {
				if (p.equals(currentPhoto)) {
					p = currentPhoto;
				}
			}
			Synch.photoListToFile(photoList);
			tagTextField.clear();
			valTextField.clear();
			initalize();
		}

	}

	/**
	 * removes the selected tag from the selected photo
	 * 
	 * @param event remove button pressed
	 */
	@FXML
	public void removeTag(ActionEvent event) {

		// Confirmation for removing tag
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Removing Tag Confirmation");
		alert.setContentText("Are you sure you want to remove tag.");

		if (alert.showAndWait().get() == ButtonType.OK) {
			// Implement remove tag
			currentPhoto.getTags().remove(currentTag);
			currentTag = null;
			for (Photo p : photoList) {
				if (p.equals(currentPhoto)) {
					p = currentPhoto;
				}
			}
			Synch.photoListToFile(photoList);
			tagTextField.clear();
			valTextField.clear();
			initalize();
		}
		photoList.remove(currentPhoto);
		photoList.add(currentPhoto);

	}

	/**
	 * clears the tag text fields
	 * 
	 * @param event clear button pressed
	 */
	@FXML
	public void clearTag(ActionEvent event) {
		tagTextField.clear();
		valTextField.clear();
	}

	/**
	 * copies the selected photo to the selected album
	 * 
	 * @param event copy button pressed
	 */
	@FXML
	public void copyPhoto(ActionEvent event) {

		// Copy Photo
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Copy Photo Confirmation");
		alert.setContentText("Are you sure you want to copy photo.");

		if (alert.showAndWait().get() == ButtonType.OK && destinationAlbum != null) {
			// copy photo
			ArrayList<User> list = Synch.userFileToList();
			for (User u : list) {
				for (Album a : u.getAlbumList()) {
					if (u.getUsername().equals(user.getUsername()) && a.equals(destinationAlbum)) {
						boolean isPhotoAdded = a.addToPhotoList(currentPhoto);
						if (isPhotoAdded == false) {

							Alert alert2 = new Alert(AlertType.ERROR);
							alert2.setTitle("Destination already contains photo");
							alert2.setContentText("Destination album already has this photo");
							alert2.show();
							return;
						}
					}
				}
			}
			Synch.userListToFile(list);
			initalize();
		}

	}

	/**
	 * moves the selected photo to the selected album
	 * 
	 * @param event move button pressed
	 */
	@FXML
	public void movePhoto(ActionEvent event) {

		// move Photo
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Move Confirmation");
		alert.setContentText("Are you sure you want to move photo.");

		if (alert.showAndWait().get() == ButtonType.OK && destinationAlbum != null) {
			ArrayList<User> list = Synch.userFileToList();
			for (User u : list) {
				for (Album c : u.getAlbumList()) {
					for (Album d : u.getAlbumList()) {
						if (u.equals(user) && c.equals(currentAlbum) && d.equals(destinationAlbum)) {
							boolean isPhotoAdded = d.addToPhotoList(currentPhoto);
							if (isPhotoAdded == true) {

								c.removePhotoFromList(currentPhoto);
							} else {
								Alert alert2 = new Alert(AlertType.ERROR);
								alert2.setTitle("Destination already contains photo");
								alert2.setContentText("Destination album already has this photo");
								alert2.show();
								return;
							}

						}
					}
				}
			}
			Synch.userListToFile(list);
			initalize();
		}

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

}
