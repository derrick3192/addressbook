package pwc.addressbook;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("serial")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@PropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
public class TestBookRepository {

	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	BookRepository bookRepository;
	
	Person bob = new Person("Bob", "1111111111");
	Person mary = new Person("Mary", "2222222222");
	Person jane = new Person("Jane", "3333333333");
	Person john = new Person("John", "4444444444");
	
	List<Person> people1 = new ArrayList<Person>(){{
		add(bob);
		add(mary);
		add(jane);
	}};
	
	List<Person> people2 = new ArrayList<Person>(){{
		add(mary);
		add(john);
		add(jane);
	}};
	

	public void testValidBookSave() {
		Book book = bookRepository.save(new Book("HelloWorldBook", people1));
		assertNotNull(book.getId());
		assertEquals(1, bookRepository.count());
	}

	@Test(expected=Exception.class)
	public void whenNameTooShort_expectException() {
		bookRepository.save(new Book("H", people1));
	}
	
	@Test(expected=Exception.class)
	public void whenNameTooLong_expectException() {
		bookRepository.save(new Book(
				"HAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHA"+
				"HAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHA"+
				"HAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHA", people1));
	}
	
	@Before
	public void init() {
		cleanup();
	}
	
	@After
	public void after() {
		cleanup();
	}
	
	public void cleanup() {
		bookRepository.deleteAll();
		personRepository.deleteAll();
	}

}
