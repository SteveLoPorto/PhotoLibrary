package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *  Steven Loporto
 *
 */
public abstract class Synch {

	/**
	 * @param <T>  the object which will be sorted
	 * @param list the list to be cleaned
	 * @return a list with 0 duplicates of the same object - uses contains(Object o)
	 */
	private static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
		ArrayList<T> newList = new ArrayList<T>();
		for (T element : list) {
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}
		return newList;
	}

	/**
	 * Saves the user list as a serialized file
	 * 
	 * @param list the list which needs to be saved as a file
	 */
	public static void userListToFile(ArrayList<User> list) {
		try {
			FileOutputStream fileOut = new FileOutputStream("data/users.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(list);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Reads the file and returns a list
	 * 
	 * @return the list of all users
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<User> userFileToList() {
		ArrayList<User> e = null;
		try {
			FileInputStream fileIn = new FileInputStream("data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (ArrayList<User>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			return new ArrayList<User>();
		} catch (ClassNotFoundException c) {
			return new ArrayList<User>();
		}
		return e;
	}

	/**
	 * Saves the photo list as a serialized file
	 * 
	 * @param list the list which needs to be saved as a file
	 */
	public static void photoListToFile(ArrayList<Photo> list) {
		list = removeDuplicates(list);
		try {
			FileOutputStream fileOut = new FileOutputStream("data/photos.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(list);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Reads the file and returns a list
	 * 
	 * @return the list of all photos
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Photo> photoFileToList() {
		ArrayList<Photo> e = null;
		try {
			FileInputStream fileIn = new FileInputStream("data/photos.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (ArrayList<Photo>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			return new ArrayList<Photo>();
		} catch (ClassNotFoundException c) {
			return new ArrayList<Photo>();
		}
		return e;
	}

	/**
	 * Returns a list of all photos that belong to an album
	 * 
	 * @param a the Album to be read
	 * @return an ArrayList of photos which belong to the album
	 */
	public static ArrayList<Photo> getPhotosForAlbum(Album a) {
		ArrayList<Photo> ret = new ArrayList<Photo>();
		for (Photo p : photoFileToList()) {
			if (a.getPhotosList().contains(p)) {
				ret.add(p);
			}
		}
		return ret;
	}

}
