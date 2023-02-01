package model;

import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *Steven Loporto
 *
 */
public class Album implements Serializable {

	private static final long serialVersionUID = 6389784095891549059L;
	private ArrayList<Photo> photosList;
	private String name;

	/**
	 * Album constructor
	 * 
	 * @param photosList a list of photos that belong in the album if applicable
	 * @param name       the name of the album
	 */
	public Album(ArrayList<Photo> photosList, String name) {
		if (photosList == null) {
			this.photosList = new ArrayList<Photo>();
		} else {
			this.photosList = photosList;
		}
		this.name = name;
	}

	/**
	 * Adds a photo to an album and returns whether the addition was successful
	 * 
	 * @param addedPhoto the photo to add
	 * @return true if the photo was successfully added
	 */
	public boolean addToPhotoList(Photo addedPhoto) {
		if (Synch.photoFileToList().contains(addedPhoto)) {
		}

		String addedPhotoFilePath = addedPhoto.getFilepath();
		boolean duplicate = false;
		if (photosList.isEmpty() != true) {
			Iterator<Photo> iter = photosList.iterator();

			while (iter.hasNext()) {
				String currentPhotoFilePath = iter.next().getFilepath();
				if (currentPhotoFilePath.equals(addedPhotoFilePath)) {
					duplicate = true;
					break;
				}

			}
		}

		if (duplicate == false) {
			this.photosList.add(addedPhoto);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * removes a photo from the album
	 * 
	 * @param removedPhoto the photo to remove
	 */
	public void removePhotoFromList(Photo removedPhoto) {
		photosList.remove(removedPhoto);

	}

	/**
	 * getter for Album's photo list
	 * 
	 * @return the photosList
	 */
	public ArrayList<Photo> getPhotosList() {
		return photosList;
	}

	/**
	 * @param name the name to set the album as
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the album name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the album photo list
	 * 
	 * @param photosList the photosList to set
	 */

	public void setPhotosList(ArrayList<Photo> photosList) {
		this.photosList = photosList;
	}

	public String toString() {

		FileTime earliestDate = null;
		FileTime latestDate = null;
		int photoListSize = photosList.size();

		if (photoListSize != 0) {
			earliestDate = photosList.get(0).getDate();
			latestDate = photosList.get(0).getDate();
			Iterator<Photo> iter = photosList.iterator();
			while (iter.hasNext()) {
				Photo currPhoto = iter.next();

				if (currPhoto.getDate().compareTo(earliestDate) < 0) {
					earliestDate = currPhoto.getDate();
				}
				if (currPhoto.getDate().compareTo(latestDate) > 0) {
					latestDate = currPhoto.getDate();
				}

			}

			return (name + ", " + photoListSize + ", " + earliestDate.toString().substring(0, 10) + ", "
					+ latestDate.toString().substring(0, 10));
		}

		return (name + ", " + photoListSize);

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Album) {
			return name.equals(((Album) o).name);
		}
		return false;
	}
}
