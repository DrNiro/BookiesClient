package com.dts.bookies.logic.boundaries.subboundaries;

import com.dts.bookies.util.Constants;

public class UserIdBoundary {

	private String space;
	private String email;
	
	public UserIdBoundary() {
		
	}

	public UserIdBoundary(String space, String email) {
		setSpace(space);
		setEmail(email);
	}
	
	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return getSpace() + Constants.DELIMITER + getEmail();
	}
}
