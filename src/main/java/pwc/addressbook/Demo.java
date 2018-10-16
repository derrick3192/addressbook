package pwc.addressbook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 * This class creates some demo data, if you do wish this data to be created on startup, you will need to set the spring profile to demo,
 * i.e. : uncomment #spring.profiles.active=demo in application.properties
 */
@Profile("demo")
@Component
public class Demo {

	@Autowired	
	public Demo(PersonRepository personRepository, BookRepository bookRepository) {
		
		
		Person bob = personRepository.save(new Person("Bob", "1111111111"));
		Person mary = personRepository.save(new Person("Mary", "2222222222"));
		Person jane = personRepository.save(new Person("Jane", "3333333333"));
		Person john = personRepository.save(new Person("John", "4444444444"));
		
		List<Person> people1 = new ArrayList<>();
		people1.add(bob);
		people1.add(mary);
		people1.add(jane);
		bookRepository.save(new Book("bookA", people1));
		
		List<Person> people2 = new ArrayList<>();
		people2.add(mary);
		people2.add(john);
		people2.add(jane);
		bookRepository.save(new Book("bookB", people2));
		
		
	}
	
}
