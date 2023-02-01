package model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * Steven Loporto
 *
 */
public class Photo implements Serializable {
	private static final long serialVersionUID = 7122582895421915872L;
	private String filepath;
	private ArrayList<Tag> tags;
	private String caption;

	/**
	 * Photo constructor
	 * 
	 * @param filepath the path to the photo
	 * @param tags     any tags the photo has
	 * @param caption  the caption for the photo
	 */
	public Photo(String filepath, ArrayList<Tag> tags, String caption) {
		this.filepath = filepath;
		if (tags == null) {
			this.tags = new ArrayList<Tag>();
		} else {
			this.tags = tags;
		}
		this.caption = caption;
	}

	/**
	 * @return the filepath to the Photo
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * @return the caption of the photo
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param s the caption to be set
	 */
	public void setCaption(String s) {
		caption = s;
	}

	/**
	 * @return a representation of the photo as an Image object
	 */
	public Image getImage() {
		return new Image("file:" + filepath);
	}

	/**
	 * @return an ArrayList of the photo's tags
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}

	/**
	 * @param t the tag name to be added to the photo
	 * @param v the value associated with the tag
	 */
	public void addTag(String t, String v) {
		tags.add(new Tag(t, v));
	}

	/**
	 * @param t the tag name to be deleted from the photo
	 * @param v the value associated with the tag
	 */
	public void deleteTag(String t, String v) {
		Tag tbd = null;
		for (Tag x : tags) {
			if (x.getTag().equals(t) && x.getValue().equals(v)) {
				tbd = x;
			}
		}
		tags.remove(tbd);
	}

	public String toString() {
		return filepath + tags;
	}

	/**
	 * @return a FileTime object of the time the file was last modified
	 */
	public FileTime getDate() {
		try {
			Path file = Paths.get(filepath);
			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

			return attr.lastModifiedTime();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param addedTag the tag to be added
	 */
	public void addTag(Tag addedTag) {
		tags.add(addedTag);
	}

	public String getPath() {
		return filepath;
	}

	public boolean equals(Object o) {
		if (o instanceof Photo && o != null) {
			if (((Photo) o).getPath().equals(filepath)) {
				return true;
			}
		}
		return false;
	}

}
