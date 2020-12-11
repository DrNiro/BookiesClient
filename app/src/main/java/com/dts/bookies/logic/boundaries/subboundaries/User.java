package com.dts.bookies.logic.boundaries.subboundaries;

public class User {

	private UserIdBoundary userId;
	
	public User() {
		
	}

	public User(UserIdBoundary userId) {
		super();
		setUserId(userId);
	}

	public UserIdBoundary getUserId() {
		return userId;
	}
	
	public void setUserId(UserIdBoundary userId) {
		this.userId = userId;
	}
}
