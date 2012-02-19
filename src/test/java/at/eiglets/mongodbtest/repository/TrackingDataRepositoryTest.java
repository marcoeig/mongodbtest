package at.eiglets.mongodbtest.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfiguration.class)
public class TrackingDataRepositoryTest {

	@Inject
	private TrackingDataRepository repository;
	
	@Test
	public void test() {
//		repository.removeAll();
		long start = System.currentTimeMillis();
		for(int i = 0; i< 100000 ;i++) {
			repository.add(getRandomTd());
		}
		System.out.println((System.currentTimeMillis() - start));
	}
	
//	@Test
	public void testFind() {
		final Map<Key, String> criteria = Maps.newHashMap();
		criteria.put(Key.dp, "willhaben.at4");
		final Collection<TrackingValues> values = repository.findMapReduce(new DateMidnight().minusDays(1), Sets.<Key>newHashSet(Key.dp),
				criteria, Sets.newHashSet(Counter.vc));
		
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
		td.add(Key.dp, "willhaben.at" + (r.nextInt(10000000)+1000000));
		td.inc(Counter.vc);
		if(r.nextBoolean()) {
			td.inc(Counter.vuc);
		}
		return td;
	}

}
