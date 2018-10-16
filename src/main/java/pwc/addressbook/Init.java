package pwc.addressbook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Init {

	@Autowired	
	public Init(PersonRepository personRepository, BookRepository bookRepository) {
		
		
		Person p1 = personRepository.save(new Person("Derrick", "0413539670"));
		Person p2 = personRepository.save(new Person("Frodoe Baggis", "1234456789"));
		Person p3 = personRepository.save(new Person("Humpty Dumpty", "0912323445"));
		
		List<Person> people1 = new ArrayList<>();
		people1.add(p1);
		people1.add(p2);
		bookRepository.save(new Book("bookA", people1));
		
		List<Person> people2 = new ArrayList<>();
		people1.add(p2);
		people1.add(p3);
		bookRepository.save(new Book("bookB", people2));
		
		
	}
	
}
