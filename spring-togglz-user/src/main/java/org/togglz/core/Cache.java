package org.togglz.core;

public interface Cache<T> {
	T get(String k);
	void evict(String k);
	void clear(String k);
	void put(String k, T v);
}
