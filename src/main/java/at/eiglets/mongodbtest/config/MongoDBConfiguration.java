package at.eiglets.mongodbtest.config;

import java.net.UnknownHostException;

import javax.inject.Inject;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.stereotype.Component;

import at.eiglets.mongodbtest.domain.TrackingValues;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@Configuration
@PropertySource("classpath:mongodb.properties")
public class MongoDBConfiguration extends AbstractMongoConfiguration {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Inject
	private Environment env;

	@Bean
	@Override
	public Mongo mongo() throws UnknownHostException, MongoException {
		final String mongoHost = env.getProperty("mongodb.host");
		final int mongoPort = Integer.valueOf(env.getProperty("mongodb.port",
				"0"));
		log.info("create mongoDB instance (host={}, port={})", mongoHost,
				mongoPort);
		return new Mongo(mongoHost, mongoPort);
	}

	@Override
	public String getDatabaseName() {
		return env.getProperty("mongodb.databaseName");
	}

	@Override
	protected void afterMappingMongoConverterCreation(
			MappingMongoConverter converter) {
		final CustomConversions conversions = new CustomConversions(
				Lists.newArrayList(new TrackingValuesConverter()));
		converter.setCustomConversions(conversions);
	}

	@ReadingConverter
	public static class TrackingValuesConverter implements
			Converter<DBObject, TrackingValues> {

		private final DateTimeFormatter formatter = DateTimeFormat
				.forPattern("yyyymmdd");

		@Override
		public TrackingValues convert(DBObject source) {

			final DBObject keys = (DBObject) source.get("_id");

			final String statday = String.valueOf(keys.get("statday"));
			
			final TrackingValues values = new TrackingValues(formatter
					.parseDateTime(statday).toDateMidnight());
			for (final String key : keys.keySet()) {
				values.add(key, keys.get(key));
			}
			final DBObject v = (DBObject) source.get("value");
			for (final String key : v.keySet()) {
				values.add(key, v.get(key));
			}
			return values;
		}

	}

}
