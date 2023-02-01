package model;

import java.io.Serializable;

/**
 *  Steven Loporto
 *
 */
public class Tag implements Serializable {

	private static final long serialVersionUID = -1120926033902191257L;
	private String tag;
	private String value;

	/**
	 * Tag constructor
	 * 
	 * @param tag   the tag name
	 * @param value the value associated with the tag
	 */
	public Tag(String tag, String value) {
		this.setTag(tag);
		this.setValue(value);
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return (tag + ": " + value);
	}

	public boolean equals(Object t) {
		if (t instanceof Tag)
			return (tag.equals(((Tag) t).getTag()) && value.equals(((Tag) t).getValue()));
		return false;
	}

}
