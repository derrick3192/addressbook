package pwc.addressbook;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull(message="name cannot be null")
	@Size(min=3, max=50, message="name must be between 3 & 50 characters")
	private String name;
	
	@NotNull(message="number cannot be null")
	@Pattern(message="must be a mobile number with 10 digits", regexp="(^$|[0-9]{10})")
	private String number;
	
	@ManyToMany(mappedBy = "people")
	List<Book> books = new ArrayList<>();
	
	public Person() {}
	
	public Person(String name, String number) {
		this.name = name;
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", number=" + number + ", books=" + books + "]";
	}	
	
	

}