package com.logansrings.booklibrary.domain;

/**
 * @author mark
 * @deprecated
 */
public class BookInfo {
	String title;
	String authorFirstName;
	String authorLastName;
	boolean markedForDeletion;
	
	public BookInfo(String title, String authorFirstName, String authorLastName) {
		this.title = title;
		this.authorFirstName = authorFirstName;
		this.authorLastName = authorLastName;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthorName() {
		return authorFirstName + " " + authorLastName;
	}
	public String getAuthorFirstName() {
		return authorFirstName;
	}

	public String getAuthorLastName() {
		return authorLastName;
	}

	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}

	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}

}
