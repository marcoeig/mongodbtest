package at.eiglets.mongodbtest.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.joda.time.DateMidnight;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import at.eiglets.mongodbtest.domain.TrackingData;
import at.eiglets.mongodbtest.domain.TrackingData.Counter;
import at.eiglets.mongodbtest.domain.TrackingData.Key;
import at.eiglets.mongodbtest.domain.TrackingValues;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;

@Repository
public class TrackingDataRepository {

	private final MongoTemplate mongoTemplate;

	@Inject
	public TrackingDataRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public void add(final TrackingData td) {
		final Criteria criteria = Criteria.where("statday").is(
				td.getStatday().toString("yyyymmdd"));
		for (final Map.Entry<Key, String> entry : td.getKeys().entrySet()) {
			criteria.and(entry.getKey().name()).is(entry.getValue());
		}

		final Update update = new Update();
		for (final Map.Entry<Counter, AtomicInteger> entry : td.getCounters()
				.entrySet()) {
			update.inc(entry.getKey().name(), entry.getValue().intValue());
		}

		mongoTemplate.upsert(Query.query(criteria), update, TrackingData.class);
	}

	public Collection<TrackingValues> findGroupBy(final DateMidnight statday, final Set<Key> groupByKeys,
			final Map<Key, String> criteriaKeys, final Set<Counter> counters) {
		final Criteria criteria = Criteria.where("statday").is(
				statday.toString("yyyymmdd"));
		
		for (final Map.Entry<Key, String> entry : criteriaKeys.entrySet()) {
			criteria.and(entry.getKey().name()).is(entry.getValue());
		}
		final List<String> keys = Lists.newArrayList(Collections2.transform(groupByKeys, new Function<Key, String>(){

			@Override
			public String apply(Key key) {
				return key.name();
			}
			
		}));
		keys.add("statday");
		
		final GroupBy groupBy = new GroupBy(keys.toArray(new String[0]));
		
		final StringBuilder reduce = new StringBuilder("function(obj,prev) {");
		final StringBuilder initial = new StringBuilder("{");
		for(final Counter counter : counters) {
			initial.append(counter.name()).append(":0,");
			reduce.append("prev."+counter.name() + " += obj."+counter.name()+";");
		}
		initial.deleteCharAt(initial.lastIndexOf(","));
		initial.append("}");
		reduce.append("}");
		
		groupBy.reduceFunction(reduce.toString());
		groupBy.initialDocument(initial.toString());
		
		return Lists.newArrayList(mongoTemplate.group(criteria, "trackingData",groupBy, TrackingValues.class));
	}
	
	public Collection<TrackingValues> findMapReduce(final FindParameters findParameters) {
		final Map<Key, Collection<Object>> criteriaKeys = findParameters.getCriteria();
		final Set<Key> groupByKeys = findParameters.getKeys();
		final Set<Counter> counters = findParameters.getCounters();
		
		final DateMidnight from = findParameters.getFrom();
		final DateMidnight to = findParameters.getUntil();
		
		final Criteria criteria = Criteria.where("statday").gte(
				from.toString("yyyymmdd")).lte(to.toString("yyyymmdd"));
		
		for (final Map.Entry<Key, Collection<Object>> entry : criteriaKeys.entrySet()) {
			final List<Criteria> ors = Lists.newArrayList();
			for(final Object value : entry.getValue()) {
				ors.add(Criteria.where(entry.getKey().name()).is(value));
			}
			criteria.orOperator(ors.toArray(new Criteria[0]));
		}
		
		final String mapPattern = "function(){ emit([KEYS], [COUNTERS]); };";
		final String reducePattern = "function(key, values){var result = [COUNTERS]; values.forEach(function(value){[COUNTERS_INC]});return result;};";
		
		final StringBuilder keysMap = new StringBuilder("{statday: this.statday,");
		for(final Key key : groupByKeys) {
			keysMap.append(key.name() + ": this." + key.name() +",");
		}
		keysMap.deleteCharAt(keysMap.lastIndexOf(","));
		keysMap.append("}");
		
		final StringBuilder countersMap = new StringBuilder("{");
		final StringBuilder result = new StringBuilder("{");
		final StringBuilder inc = new StringBuilder();
		for(final Counter counter : counters) {
			countersMap.append(counter.name() + ": this." + counter.name() +",");
			result.append(counter.name() + ":0,");
			inc.append("result."+ counter.name() + " += value."+counter.name()+";");
		}
		countersMap.deleteCharAt(countersMap.lastIndexOf(","));
		countersMap.append("}");
		result.deleteCharAt(result.lastIndexOf(","));
		result.append("}");
		
		final String m = mapPattern.replace("[KEYS]", keysMap).replace("[COUNTERS]", countersMap);
		final String r = reducePattern.replace("[COUNTERS]", result).replace("[COUNTERS_INC]", inc);
		
		return Lists.newArrayList(mongoTemplate.mapReduce(Query.query(criteria), "trackingData", m, r, TrackingValues.class));
	}

	public void removeAll() {
		mongoTemplate.dropCollection(TrackingData.class);
	}

}
