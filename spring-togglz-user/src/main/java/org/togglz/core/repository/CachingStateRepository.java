package org.togglz.core.repository;

import org.togglz.core.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togglz.core.Feature;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CachingStateRepository implements StateRepository {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final StateRepository stateRepository;
	private final Cache<FeatureState> cache;

	public CachingStateRepository(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
		this.cache = new LRUTimedInMemoryCache();
	}

	@Override
	public FeatureState getFeatureState(Feature feature) {
		return Optional
			.ofNullable(cache.get(feature.name()))
			.orElseGet(() -> {
				final FeatureState v = stateRepository.getFeatureState(feature);
				cache.put(feature.name(), v);
				return v;
			});
	}

	@Override
	public void setFeatureState(FeatureState featureState) {
		stateRepository.setFeatureState(featureState);
		cache.evict(featureState.getFeature().name());
	}

	static class LRUTimedInMemoryCache implements Cache<FeatureState> {

		private final Map<String, CachingStateRepository.CacheEntry> cache;
		private final int maxEntries;
		private final long ttl;

		LRUTimedInMemoryCache() {
			this.cache = new ConcurrentHashMap<>();
			this.ttl = Duration.ofMinutes(30).toMillis();
			this.maxEntries = 1256;
		}

		@Override
		public FeatureState get(String k) {
			final CacheEntry entry = cache.get(k);
			if(entry != null){
				if(isExpired(entry)){
					evict(k);
				}
				return entry.getState();
			}
			return null;
		}

		@Override
		public void evict(String k) {
			cache.remove(k);
		}

		@Override
		public void clear(String k) {
			cache.clear();
		}

		@Override
		public void put(String k, FeatureState v) {
			if(cache.size() >= maxEntries){
				evict(cache.keySet().iterator().next());
			}
			cache.put(k, new CacheEntry(v));
		}

		/**
		 * Checks whether this supplied {@link CacheEntry} should be ignored.
		 */
		boolean isExpired(CacheEntry entry) {
			if (ttl == 0) {
				return false;
			}
			return entry.getTimestamp() + ttl < System.currentTimeMillis();
		}

	}

	private static class CacheEntry {

		private final FeatureState state;
		private final long timestamp;

		public CacheEntry(FeatureState state) {
			this.state = state;
			this.timestamp = System.currentTimeMillis();
		}

		public FeatureState getState() {
			return state;
		}

		public long getTimestamp() {
			return timestamp;
		}

	}

}
