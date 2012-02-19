package at.eiglets.mongodbtest.repository;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.eiglets.mongodbtest.config.AppConfiguration;
import at.eiglets.mongodbtest.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfiguration.class)
public class PersonRepositoryTest {

	@Inject private PersonRepository repository;
	
	@Test
	public void test() {
		for(int i=0; i<5000;i++) {
			final Person p = new Person();
			p.setFirstName("Marco");
			p.setLastName("Eigletsberger");
			repository.save(p);
		}
		
		for(final Person p : repository.findAll()) {
			repository.remove(p);
		}
	}

}
