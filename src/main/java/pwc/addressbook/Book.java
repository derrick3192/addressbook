package pwc.addressbook;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	String name;
	
	@ManyToMany
	List<Person> people;

	public Book(){}
	
	public Book(String name, List<Person> people) {
		this.name = name;
		this.people = people;
	}
	
	public long getId() {
		return id;
	}
	
	public List<Person> getPeople() {
		return people;
	}

	public void setPeople(List<Person> people) {
		this.people = people;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", people=" + people + "]";
	}

}
