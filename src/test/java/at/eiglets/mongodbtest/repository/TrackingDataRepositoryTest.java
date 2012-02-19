package at.eiglets.mongodbtest.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.eiglets.mongodbtest.config.AppConfiguration;
import at.eiglets.mongodbtest.domain.TrackingData;
import at.eiglets.mongodbtest.domain.TrackingData.Counter;
import at.eiglets.mongodbtest.domain.TrackingData.Key;
import at.eiglets.mongodbtest.domain.TrackingValues;
import at.eiglets.mongodbtest.repository.CollectParameters.Builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfiguration.class)
public class TrackingDataRepositoryTest {

	@Inject
	private TrackingDataRepository repository;
	
//	@Test
	public void test() {
//		repository.removeAll();
		long start = System.currentTimeMillis();
		for(int i = 0; i< 5000 ;i++) {
			repository.add(getRandomTd());
		}
		System.out.println((System.currentTimeMillis() - start));
	}
	
	@Test
	public void testFind() {
		final Multimap<Key, Object> criteria = ArrayListMultimap.create();
		criteria.put(Key.dp, "willhaben.at4");
		criteria.put(Key.dp, "willhaben.at9888973");
		
		final CollectParameters params = new Builder()
			.from(new DateMidnight().minusDays(1))
			.until(new DateMidnight())
			.where(criteria.asMap())
			.keys(Sets.newHashSet(Key.dp))
			.counters(Sets.newHashSet(Counter.vc)).build();
		
		long start = System.currentTimeMillis();
		final Collection<TrackingValues> values = repository.findMapReduce(params);
		System.out.println((System.currentTimeMillis() - start));
		
		for(final TrackingValues v : values) {
			System.out.println("=> " + v);
		}
		
	}
	
	private final Random r = new Random();
	private final DateMidnight statday = new DateMidnight().minusDays(1);
	
	private TrackingData getRandomTd() {
		final TrackingData td = new TrackingData();
		td.setStatday(statday);
		td.add(Key.ca, "113122" + r.nextInt(100));
		td.add(Key.dp, "willhaben.at" + (r.nextInt(10)));
		td.inc(Counter.vc);
		if(r.nextBoolean()) {
			td.inc(Counter.vuc);
		}
		return td;
	}

}
