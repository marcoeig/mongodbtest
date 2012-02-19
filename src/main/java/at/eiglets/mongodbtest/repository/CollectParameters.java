package at.eiglets.mongodbtest.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateMidnight;

import at.eiglets.mongodbtest.domain.TrackingData.Counter;
import at.eiglets.mongodbtest.domain.TrackingData.Key;

public class CollectParameters {

	private DateMidnight from = new DateMidnight();
	private DateMidnight until = new DateMidnight();
	private Map<Key, Collection<Object>> criteria = Collections.emptyMap();
	
	private Set<Key> keys = Collections.emptySet();
	private Set<Counter> counters = Collections.emptySet();
	
	public DateMidnight getFrom() {
		return from;
	}

	public DateMidnight getUntil() {
		return until;
	}

	public Map<Key, Collection<Object>> getCriteria() {
		return criteria;
	}

	public Set<Key> getKeys() {
		return keys;
	}

	public Set<Counter> getCounters() {
		return counters;
	}

	public static class Builder {

		private final CollectParameters fp = new CollectParameters();
		
		public Builder from(final DateMidnight from) {
			fp.from = from;
			return this;
		}
		
		public Builder until(final DateMidnight until) {
			fp.until = until;
			return this;
		}
		
		public Builder where(final Map<Key, Collection<Object>> criteria) {
			fp.criteria = criteria;
			return this;
		}
		
		public Builder keys(final Set<Key> keys) {
			fp.keys = keys;
			return this;
		}
		
		public Builder counters(final Set<Counter> counters) {
			fp.counters = counters;
			return this;
		}
		
		public CollectParameters build() {
			return fp;
		}
		
	}
	
}
