package at.eiglets.mongodbtest.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateMidnight;

import com.google.common.collect.Maps;

public class TrackingData {
	
	public static enum Key {
		ca, cu, dp, pa, pd, p1, p2, p3, p4
	}
	
	public static enum Counter {
		cc, cuc, vc, vuc
	}
	
	private DateMidnight statday;

	private final ConcurrentMap<Key, String> keys = Maps.newConcurrentMap();
	private final ConcurrentMap<Counter, AtomicInteger> counters = Maps.newConcurrentMap();
	
	public void add(final Key key, final String value) {
		keys.put(key, value);
	}
	
	public void inc(final Counter counter) {
		AtomicInteger cnt = counters.get(counter);
		if (cnt == null) {
		    final AtomicInteger value = new AtomicInteger();
		    cnt = counters.putIfAbsent(counter, value);
		    if (cnt == null) {
		        cnt = value;
		    }
		}
		cnt.incrementAndGet();
	}

	public Map<Key, String> getKeys() {
		return keys;
	}

	public Map<Counter, AtomicInteger> getCounters() {
		return counters;
	}
	
	public DateMidnight getStatday() {
		return statday;
	}

	public void setStatday(DateMidnight statday) {
		this.statday = statday;
	}

	@Override
	public String toString() {
		return "TrackingData [statday=" + statday + ", keys=" + keys
				+ ", counters=" + counters + "]";
	}
	
}
