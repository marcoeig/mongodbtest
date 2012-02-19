package at.eiglets.mongodbtest.service;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.eiglets.mongodbtest.domain.Person;
import at.eiglets.mongodbtest.repository.PersonRepository;

@Service
public class PersonService {

	private final PersonRepository personRepository;

	@Inject
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	public Collection<Person> findAll() {
		return personRepository.findAll();
	}
	
	public Person findById(final String id) {
		return personRepository.findById(id);
	}
	
	public void remove(final Person person) {
		personRepository.remove(person);
	}
	
	public void save(final Person person) {
		personRepository.save(person);
	}
	
}
