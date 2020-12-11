package com.dts.bookies.logic.boundaries.subboundaries;

import com.dts.bookies.util.Constants;

public class OperationIdBoundary {

	private String space;
	private String id;
	
	public OperationIdBoundary() {
		
	}

	public OperationIdBoundary(String space, String id) {
		setSpace(space);
		setId(id);
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getSpace() + Constants.DELIMITER + getId();
	}
	
	
	
}
