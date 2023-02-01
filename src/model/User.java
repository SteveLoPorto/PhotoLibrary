package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Steven Loporto
 *
 */
public class User implements Serializable {
	private static final long serialVersionUID = 8299921590644677805L;
	private String username;
	private ArrayList<Album> albumList;

	/**
	 * @param username  the name of the user
	 * @param albumList a list of Albums belonging to the user, if any
	 */
	public User(String username, ArrayList<Album> albumList) {
		this.username = username;
		if (albumList == null) {
			this.albumList = new ArrayList<Album>();
		} else {
			this.albumList = albumList;
		}
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the albumList
	 */
	public ArrayList<Album> getAlbumList() {
		return albumList;
	}

	/**
	 * @param album the albumList to set
	 */
	public void addAlbum(Album album) {
		albumList.add(album);
	}

	/**
	 * @param a the album to be deleted
	 */
	public void deleteAlbum(Album a) {
		albumList.remove(a);
	}

	public boolean equals(Object o) {
		if (o instanceof User && o != null) {
			return username.equals(((User) o).getUsername());
		}
		return false;
	}

	public String toString() {
		return username;
	}

}
