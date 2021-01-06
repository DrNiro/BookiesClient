package com.dts.bookies.logic.boundaries;

import com.dts.bookies.logic.boundaries.subboundaries.Item;
import com.dts.bookies.logic.boundaries.subboundaries.OperationIdBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.User;

import java.util.Date;
import java.util.Map;

public class OperationBoundary {
	private OperationIdBoundary operationId;
	private String type;
	private Item item;
	private Date createdTimestamp;
	private User invokedBy;
	private Map<String, Object> operationAttributes;
	
	public OperationBoundary() {
		
	}
	
	public OperationBoundary(OperationIdBoundary operationId, String type, Item item,
							 Date createdTimestamp, User invokedBy,
			Map<String, Object> opertaionAttributes) {
		setOperationId(operationId);
		setType(type);
		setItem(item);
		setCreatedTimestamp(createdTimestamp);
		setInvokedBy(invokedBy);
		setOperationAttributes(opertaionAttributes);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public OperationIdBoundary getOperationId() {
		return operationId;
	}

	public void setOperationId(OperationIdBoundary operationId) {
		this.operationId = operationId;
	}

	public User getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(User invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(Map<String, Object> operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

	@Override
	public String toString() {
		return "OperationBoundary [operationId=" + operationId + ", type=" + type + ", item=" + item
				+ ", createdTimestamp=" + createdTimestamp + ", invokedBy=" + invokedBy + ", operationAttributes="
				+ operationAttributes + "]";
	}
}
