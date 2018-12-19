package org.togglz.core.repository.featurestate;

import org.togglz.core.Feature;
import org.togglz.core.repository.FeatureState;

import java.util.*;
import java.util.function.BiFunction;


public class StreamFeatureState extends FeatureState {

	private final BiFunction<String, String, String> parameterSupplier;
	private final Map<String, String> cache;

	public StreamFeatureState(Feature feature, BiFunction<String, String, String> parameterSupplier) {
		super(feature);
		this.parameterSupplier = parameterSupplier;
		final int maxEntries = 512;
		cache = new LinkedHashMap(maxEntries + 1, .75F, true) {
			public boolean removeEldestEntry(Map.Entry eldest) {
				return size() > maxEntries;
			}
		};
	}

	@Override
	public String getParameter(final String name) {
		return Optional
			.ofNullable(cache.get(name))
			.orElseGet(() -> {
				final String v = parameterSupplier.apply(getFeature().name(), name);
				cache.put(name, v);
				return v;
		});
	}

	@Override
	public FeatureState copy() {
		return super.copy();
	}

	@Override
	public List<String> getUsers() {
		throw new UnsupportedOperationException("It was deprecated");
	}

	@Override
	public FeatureState addUser(String user) {
		throw new UnsupportedOperationException("It was deprecated");
	}

	@Override
	public FeatureState addUsers(Collection<String> users) {
		throw new UnsupportedOperationException("It was deprecated");
	}

	@Override
	public Set<String> getParameterNames() {
		throw new UnsupportedOperationException("As this feature state can have a huge number of parameter then this feature is not supported");
	}

	@Override
	public Map<String, String> getParameterMap() {
		throw new UnsupportedOperationException("As this feature state can have a huge number of parameter then this feature is not supported");
	}
}
