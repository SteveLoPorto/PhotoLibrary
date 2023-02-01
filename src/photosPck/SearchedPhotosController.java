package photosPck;

import java.io.IOException;
import java.nio.file.attribute.FileTime;
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
public class SearchedPhotosController {

	@FXML
	private Parent root;
	private Scene scene;
	private Stage stage;

	@FXML
	private Button addPhotoButton;

	@FXML
	private TextField newAlbumTextField;

	@FXML
	private Button addTagButton;

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
	private Button openPhotoButton;

	@FXML
	private ListView<Photo> photosListView;

	@FXML
	private Button removeTagButton;

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
	private TextField dateText;

	User user;
	static Photo currentPhoto = null;
	ArrayList<Tag> tagList;
	Tag currentTag;
	static ArrayList<Photo> photoList;
	static Tag tag1;
	static Tag tag2;
	static FileTime date1;
	static FileTime date2;
	static String operator;
	ObservableList<Photo> photos;
	Album destinationAlbum;

	/**
	 * Imports the data from the previous view. Used to set variables before
	 * initialize is called by the loader
	 * 
	 * @param tag1     1st tag
	 * @param tag2     2nd tag
	 * @param date1    1st date
	 * @param date2    2nd date
	 * @param operator and/or operator
	 */
	public void pipeline(Tag tag1, Tag tag2, FileTime date1, FileTime date2, String operator) {
		SearchedPhotosController.tag1 = tag1;
		SearchedPhotosController.tag2 = tag2;
		SearchedPhotosController.date1 = date1;
		SearchedPhotosController.date2 = date2;
		SearchedPhotosController.operator = operator;
		photoList = Synch.photoFileToList();
		photos = FXCollections.observableArrayList();
		for (Photo p : photoList) {
			boolean added = false;
			if (tag1 == null && tag2 == null && date1 != null && date2 != null) {
				if (p.getDate().toInstant().isAfter(date1.toInstant())
						&& date2.toInstant().isAfter(p.getDate().toInstant())) {
					added = true;
					photos.add(p);
				}
			} else {
				for (Tag t1 : p.getTags()) {
					if (tag1 != null && tag2 == null) {
						if (t1.equals(tag1)) {
							if (!added) {
								if (date1 != null && date2 != null) {
									if (p.getDate().toInstant().isAfter(date1.toInstant())
											&& date2.toInstant().isAfter(p.getDate().toInstant())) {
										added = true;
										photos.add(p);
									}
								} else {
									added = true;
									photos.add(p);
								}
							}
						}
					} else if (tag1 != null && tag2 != null) {
						for (Tag t2 : p.getTags()) {
							if (operator.equals("and")) {
								if (t1.equals(tag1) && t2.equals(tag2)) {
									if (!added) {
										if (date1 != null && date2 != null) {
											if (p.getDate().toInstant().isAfter(date1.toInstant())
													&& date2.toInstant().isAfter(p.getDate().toInstant())) {
												added = true;
												photos.add(p);
											}
										} else {
											added = true;
											photos.add(p);
										}
									}
								}
							} else if (operator.equals("or")) {
								if (t1.equals(tag1) || t2.equals(tag2)) {
									if (!added) {
										if (date1 != null && date2 != null) {
											if (p.getDate().toInstant().isAfter(date1.toInstant())
													&& date2.toInstant().isAfter(p.getDate().toInstant())) {
												added = true;
												photos.add(p);
											}
										} else {
											added = true;
											photos.add(p);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		refresh();
	}

	/**
	 * refreshes the view with the most current data
	 */
	public void refresh() {
		photoList = new ArrayList<Photo>();
		for (Photo p : photos) {
			photoList.add(p);
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
		ArrayList<User> ul = Synch.userFileToList();
		for (User u : ul) {
			if (u.equals(UserHomeScreenController.currentUser)) {
				user = u;
			}
		}
		ArrayList<Album> albumList = user.getAlbumList();
		ObservableList<Album> albums = FXCollections.observableArrayList();
		for (Album a : albumList) {
			albums.add(a);
		}
		albumsListView.setItems(albums);
		albumsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Album>() {
			@Override
			public void changed(ObservableValue<? extends Album> arg0, Album arg1, Album arg2) {
				destinationAlbum = albumsListView.getSelectionModel().getSelectedItem();
			}
		});
	}

	/**
	 * Adds the searched-for photos to a new album with the given name
	 * 
	 * @param event add button pressed
	 */
	@FXML
	void addPhotosToAlbum(ActionEvent event) {

		Album a = new Album(photoList, newAlbumTextField.getText());
		if (user.getAlbumList().contains(a)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Album Name Already Exists");
			alert.setContentText("Album Already Exists");
			alert.show();
		} else {

			ArrayList<User> ul = Synch.userFileToList();
			for (User u : ul) {
				if (user.equals(u)) {
					u.addAlbum(a);
				}
			}
			Synch.userListToFile(ul);

		}
	}

	/**
	 * adds a tag to the selected photo
	 * 
	 * @param event add tag button pressed
	 */
	@FXML
	void addTag(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Add Tag Confirmation");
		alert.setContentText("Are you sure you want to add tag to photo.");

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
			pipeline(tag1, tag2, date1, date2, operator);
		}
	}

	/**
	 * clears the text in the add tag fields
	 * 
	 * @param event clear button pressed
	 */
	@FXML
	void clearTag(ActionEvent event) {
		tagTextField.clear();
		valTextField.clear();
	}

	/**
	 * copies the selected photo into a new album
	 * 
	 * @param event copy button pressed
	 */
	@FXML
	void copyPhoto(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Copy Photo Confirmation");
		alert.setContentText("Are you sure you want to copy photo.");

		if (alert.showAndWait().get() == ButtonType.OK && destinationAlbum != null) {
			// copy photo
			ArrayList<User> list = Synch.userFileToList();
			for (User u : list) {
				for (Album a : u.getAlbumList()) {
					if (u.getUsername().equals(user.getUsername()) && a.equals(destinationAlbum)) {
						a.addToPhotoList(currentPhoto);
					}
				}
			}
			Synch.userListToFile(list);
		}

	}

	/**
	 * Takes the user back to home screen and keeps them logged in
	 * 
	 * @param event home button pressed
	 * @throws IOException if the view is not found
	 */
	@FXML
	void goToUserHomeScreen(ActionEvent event) throws IOException {

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
	 * removes the selected tag from the photo selected
	 * 
	 * @param event remove button pressed
	 */
	@FXML
	void removeTag(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Removing Tag Confirmation");
		alert.setContentText("Are you sure you want to remove tag.");

		if (alert.showAndWait().get() == ButtonType.OK) {
			currentTag = tagsListView.getSelectionModel().getSelectedItem();
			currentPhoto = photosListView.getSelectionModel().getSelectedItem();
			ArrayList<Photo> fullList = Synch.photoFileToList();
			for (Photo p : fullList) {
				if (p.equals(currentPhoto)) {
					p.getTags().remove(currentTag);
				}
			}
			Synch.photoListToFile(fullList);
			tagTextField.clear();
			valTextField.clear();
			pipeline(tag1, tag2, date1, date2, operator);
		}
	}

	/**
	 * exits and terminates the program
	 * 
	 * @param event exit button press
	 */
	@FXML
	void exit(ActionEvent event) {

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
	void logout(ActionEvent event) throws IOException {

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