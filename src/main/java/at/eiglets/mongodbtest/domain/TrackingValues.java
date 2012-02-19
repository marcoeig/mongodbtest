package at.eiglets.mongodbtest.domain;

import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.joda.time.DateMidnight;

import at.eiglets.mongodbtest.domain.TrackingData.Counter;
import at.eiglets.mongodbtest.domain.TrackingData.Key;

import com.google.common.collect.Maps;

public class TrackingValues {
	
	private final DateMidnight statday;
	private final Map<Key, String> keys = Maps.newHashMap();
	private final Map<Counter, Integer> counters = Maps.newHashMap();
	
	public TrackingValues(DateMidnight statday) {
		this.statday = statday;
	}
	
	public void add(final String keyOrCounter, final Object obj) {
		if(EnumUtils.isValidEnum(Key.class, keyOrCounter)) {
			keys.put(Key.valueOf(keyOrCounter), String.valueOf(obj));
		} else if(EnumUtils.isValidEnum(Counter.class, keyOrCounter)) {
			counters.put(Counter.valueOf(keyOrCounter), ((Number)obj).intValue());
		}
	}

	public Map<Key, String> getKeys() {
		return keys;
	}

	public Map<Counter, Integer> getCounters() {
		return counters;
	}

	public DateMidnight getStatday() {
		return statday;
	}

	@Override
	public String toString() {
		return "TrackingValues [statday=" + statday + ", keys=" + keys
				+ ", counters=" + counters + "]";
	}
	
}
