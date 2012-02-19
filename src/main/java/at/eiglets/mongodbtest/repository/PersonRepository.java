package at.eiglets.mongodbtest.repository;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import at.eiglets.mongodbtest.domain.Person;

@Repository
public class PersonRepository {

	private final MongoTemplate mongoTemplate;

	@Inject
	public PersonRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@PostConstruct
	public void init() {
//		Person p = new Person();
//		p.setFirstName("Test");
//		p.setLastName("Eigletsberger");
//
//		mongoTemplate.save(p);
//
//		System.out.println(p.getId());
//
//		final Collection<Person> persons = mongoTemplate.findAll(Person.class);
//
//		for (final Person person : persons) {
//			System.out.println(person);
//		}
//
//		final Collection<Person> persons2 = mongoTemplate.find(new Query(
//				Criteria.where("firstName").is("Marco")), Person.class);
//		
//
//		for (final Person person : persons2) {
//			System.out.println("=> " + person);
//		}

	}
	
	public Person findById(final String id) {
		Assert.notNull(id);
		return mongoTemplate.findById(id, Person.class);
	}
	
	public Collection<Person> findAll() {
		return mongoTemplate.findAll(Person.class);
	}
	
	public void remove(final Person person) {
		Assert.notNull(person.getId());
		mongoTemplate.remove(person);
	}
	
	public void save(final Person person) {
		mongoTemplate.save(person);
	}

}
