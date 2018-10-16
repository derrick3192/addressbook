package pwc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.specification.RequestSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@PropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
public class TestPersonRepository {

	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	BookRepository bookRepository;
	
	private static final String URL_PREFIX = "http://localhost:8080";
	
	Person bob;
	Person mary;
	Person jane;
	Person john;
	
	Book book1;
	Book book2;
	
	private RequestSpecification given() {
		return RestAssured.given();
	}
	
	@Test
	public void testSaveValidPerson() {
		Person newP = new Person("Derrick", "0413539670");
		Person p1 = personRepository.save(newP);
		long id = p1.getId();
		assertNotNull(id);
		assertEquals(1, personRepository.count());
		Person retrieved = personRepository.findById(id).get();
		
		assertEquals(newP.getName(), retrieved.getName());
		assertEquals(newP.getNumber(), retrieved.getNumber());
	}
	
	Person saveHerbit() {
		return personRepository.save(new Person("Herbit", "0123456789"));
	}
	
	@Test
	public void whenValidSave_RestAPIShouldReturnPerson() {
		
		Person p = saveHerbit();
		
		ResponseBody<?> body = given()
	        .when()
	        .get(URL_PREFIX+"/people")
	        .then()
	        .extract()
	        .response()
	        .body();
		
		assertEquals(1, body.jsonPath().getList("_embedded.people").size());
		assertEquals(p.getName() , body.jsonPath().getString("_embedded.people[0].name"));
		assertEquals(p.getNumber() , body.jsonPath().getString("_embedded.people[0].number"));
	}
	
	@Test(expected=Exception.class)
	public void whenNameTooShort_expectException() {
		personRepository.save(new Person("H", "0123456789"));
	}
	
	@Test(expected=Exception.class)
	public void whenNameTooLong_expectException() {
		personRepository.save(new Person(
				"HAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHA"+
				"HAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHA"+
				"HAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHAFDSFDSHA", "0123456789"));
	}
	
	@Test(expected=Exception.class)
	public void whenNumberTooShort_expectException() {
		personRepository.save(new Person("Herbit", "0"));
	}
	
	@Test(expected=Exception.class)
	public void whenNumberTooLong_expectException() {
		personRepository.save(new Person("Herbit", "012345678910"));
	}

	public void createPeople() {
		bob = personRepository.save(new Person("Bob", "1111111111"));
		mary = personRepository.save(new Person("Mary", "2222222222"));
		jane = personRepository.save(new Person("Jane", "3333333333"));
		john = personRepository.save(new Person("John", "4444444444"));
	}
	
	public void createBook1() {
		List<Person> people1 = new ArrayList<>();
		people1.add(bob);
		people1.add(mary);
		people1.add(jane);
		book1 = bookRepository.save(new Book("bookA", people1));
	}
	
	public void createBook2() {
		List<Person> people2 = new ArrayList<>();
		people2.add(mary);
		people2.add(john);
		people2.add(jane);
		book2 = bookRepository.save(new Book("bookB", people2));
	}
	
	public void createExampleData() {
		createPeople();
		createBook1();
		createBook2();
	}
	
	@Test
	public void testUniqueToEachAddressBook() {
		createExampleData();
		
		List<Person> union = personRepository.union(book1.getId(), book2.getId());
		assertEquals(bob.getName(), union.get(0).getName());
		assertEquals(john.getName(), union.get(1).getName());
	}
	
	@Test
	public void testUniqueOnly1BookAllUnique() {
		
		createPeople();
		
		List<Person> people1 = new ArrayList<>();
		people1.add(bob);
		people1.add(mary);
		people1.add(jane);
		book1 = bookRepository.save(new Book("bookA", people1));
		
		book2 = bookRepository.save(new Book("book2", new ArrayList<>()));
		
		List<Person> union = personRepository.union(book1.getId(), book2.getId());
		assertEquals(bob.getName(), union.get(0).getName());
		assertEquals(jane.getName(), union.get(1).getName());
		assertEquals(mary.getName(), union.get(2).getName());
	}
	
	@Test
	public void testTwoBooksAllUnique() {
		
		createPeople();
		
		List<Person> people1 = new ArrayList<>();
		people1.add(bob);
		people1.add(mary);
		book1 = bookRepository.save(new Book("bookA", people1));
		
		List<Person> people2 = new ArrayList<>();
		people2.add(jane);
		people2.add(john);
		book2 = bookRepository.save(new Book("book2",people2));
		
		List<Person> union = personRepository.union(book1.getId(), book2.getId());
		
		assertEquals(4, union.size());
		assertEquals(bob.getName(), union.get(0).getName());
		assertEquals(jane.getName(), union.get(1).getName());
		assertEquals(john.getName(), union.get(2).getName());
		assertEquals(mary.getName(), union.get(3).getName());
	}
	
	@Test
	public void testTwoBooksNoneUnique() {
		
		createPeople();
		
		List<Person> people1 = new ArrayList<>();
		people1.add(bob);
		people1.add(mary);
		people1.add(jane);
		people1.add(john);
		book1 = bookRepository.save(new Book("bookA", people1));
		
		List<Person> people2 = new ArrayList<>();
		people2.add(bob);
		people2.add(mary);
		people2.add(jane);
		people2.add(john);
		book2 = bookRepository.save(new Book("book2",people2));
		
		List<Person> union = personRepository.union(book1.getId(), book2.getId());
		
		assertEquals(0, union.size());
	}
	
	
	@Test
	public void testFindByName() {
		createExampleData();
		
		List<Person> johns = personRepository.findByNameOrderByName("John");
		assertEquals(1, johns.size());
		assertEquals("John", johns.get(0).getName());
	}

	@Test
	public void testFriendsForBook1() {
		createExampleData();
		
		List<Person> friends = personRepository.friends(book1.getId());
		
		assertEquals(friends.size(), 3);
		assertEquals(bob.getName(), friends.get(0).getName());
		assertEquals(jane.getName(), friends.get(1).getName());
		assertEquals(mary.getName(), friends.get(2).getName());
		
	}
	
	@Test
	public void testFriendsForBook2() {
		createExampleData();
		
		List<Person> friends = personRepository.friends(book2.getId());
		
		assertEquals(friends.size(), 3);
		assertEquals(jane.getName(), friends.get(0).getName());
		assertEquals(john.getName(), friends.get(1).getName());
		assertEquals(mary.getName(), friends.get(2).getName());
		
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
