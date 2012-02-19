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
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
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
import com.google.common.collect.Lists;

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
	
	public Collection<TrackingValues> findMapReduce(final CollectParameters params) {
		final Map<Key, Collection<Object>> criteriaKeys = params.getCriteria();
		final Set<Key> groupByKeys = params.getKeys();
		final Set<Counter> counters = params.getCounters();
		final DateMidnight from = params.getFrom();
		final DateMidnight to = params.getUntil();
		
		final Criteria criteria = Criteria.where("statday").gte(
				from.toString("yyyymmdd")).lte(to.toString("yyyymmdd"));
		
		for (final Map.Entry<Key, Collection<Object>> entry : criteriaKeys.entrySet()) {
			final List<Criteria> ors = Lists.newArrayList();
			for(final Object value : entry.getValue()) {
				ors.add(Criteria.where(entry.getKey().name()).is(value));
			}
			criteria.orOperator(ors.toArray(new Criteria[0]));
		}
		
		final TrackingMapReduce mapReduceFunction = new TrackingMapReduce(groupByKeys.toArray(new Key[0]), counters.toArray(new Counter[0]));
		
		final String m = mapReduceFunction.mapFunction();
		final String r = mapReduceFunction.reduceFunction();
				
		long start = System.currentTimeMillis();
		final MapReduceResults<TrackingValues> result = mongoTemplate.mapReduce(Query.query(criteria), "trackingData", m, r, TrackingValues.class);
		System.out.println((System.currentTimeMillis() - start));
		return Lists.newArrayList(result);
	}

	public void removeAll() {
		mongoTemplate.dropCollection(TrackingData.class);
	}

}
