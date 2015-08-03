package org.jessica.core.orm.support.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jessica.core.orm.support.utils.CollectionUtils;

/**
 * 
 * TODO Comment of ormHive
 * 
 * @author fujohnwang
 *
 */
public class OrmMRBase {

	private Map<String, List<Object>> resources = new HashMap<String, List<Object>>();
	private Map<String, Lock> locks = new HashMap<String, Lock>();
	private Set<String> keys = new HashSet<String>();

	public OrmMRBase(String[] keys) {
		this(Arrays.asList(keys));
	}

	public OrmMRBase(List<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			throw new IllegalArgumentException("empty collection is invalid for hive to spawn data holders.");
		}
		this.keys.addAll(keys);
		initResourceHolders();
		initResourceLocks();
	}

	public OrmMRBase(Set<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			throw new IllegalArgumentException("empty collection is invalid for hive to spawn data holders.");
		}
		this.keys.addAll(keys);
		initResourceHolders();
		initResourceLocks();
	}

	private void initResourceLocks() {
		for (String key : keys) {
			locks.put(key, new ReentrantLock());
		}
	}

	private void initResourceHolders() {
		for (String key : keys) {
			resources.put(key, new ArrayList<Object>());
		}
	}

	public Map<String, List<Object>> getResources() {
		return this.resources;
	}

	public <T> void emit(String key, T entity) {
		Lock lock = locks.get(key);
		lock.lock();
		try {
			resources.get(key).add(entity);
		} finally {
			lock.unlock();
		}
	}
}
