package pwc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

	@Autowired PersonRepository personRepository;
	
	private static final String URL_PREFIX = "http://localhost:8080";
	
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
	
	@Before
	public void init() {
		cleanup();
	}
	
	@After
	public void after() {
		cleanup();
	}
	
	public void cleanup() {
		personRepository.deleteAll();
	}
	
}
