package com.dts.bookies.logic.boundaries;

import com.dts.bookies.logic.boundaries.subboundaries.ItemIdBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;
import com.dts.bookies.logic.boundaries.subboundaries.UserIdBoundary;

import java.util.Date;
import java.util.Map;

public class ItemBoundary {
	private ItemIdBoundary itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private User createdBy;
	private LocationBoundary location;
	private Map<String, Object> itemAttributes;
	
	public ItemBoundary() {

	}

	public ItemBoundary(ItemIdBoundary itemId, String type, String name, Boolean active, Date createdTimeStamp,
			User createdBy, LocationBoundary location, Map<String, Object> itemAttributes) {
		setItemId(itemId);
		setType(type);
		setName(name);
		setActive(active);
		setCreatedTimestamp(createdTimeStamp);
		setCreatedBy(createdBy);
		setLocation(location);
		setItemAttributes(itemAttributes);
	}

	public ItemIdBoundary getItemId() {
		return itemId;
	}

	public void setItemId(ItemIdBoundary itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimeStamp) {
		this.createdTimestamp = createdTimeStamp;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public LocationBoundary getLocation() {
		return location;
	}

	public void setLocation(LocationBoundary location) {
		this.location = location;
	}

	public Map<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(Map<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}

	@Override
	public String toString() {
		return "ItemBoundary [itemId=" + itemId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", itemAttributes=" + itemAttributes + "]";
	}
}
