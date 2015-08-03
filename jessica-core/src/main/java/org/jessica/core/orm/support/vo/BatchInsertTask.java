package org.jessica.core.orm.support.vo;

import java.util.Collection;

public class BatchInsertTask {
	private Collection<?> entities;

	public BatchInsertTask() {
	}

	public BatchInsertTask(Collection<?> entitiesCol) {
		this.setEntities(entitiesCol);
	}

	public void setEntities(Collection<?> entities) {
		this.entities = entities;
	}

	public Collection<?> getEntities() {
		return entities;
	}

}
