package pwc.addressbook;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	
	/**
	 * Find friends by name
	 * @param name
	 * @return
	 */
	List<Person> findByNameOrderByName(@Param("name") String name);
	
	/**
	 * Friends sorted by names for a given book
	 * @param book
	 * @return
	 */
	@Query(value="SELECT p FROM Person p where (SELECT b FROM Book b WHERE b.id=:book) MEMBER OF p.books ORDER BY p.name")
	List<Person> friends(@Param("book") Long book);
	
	/**
	 * 
	 * Given another address book that may or may not contain the same friends,
	 * display the list of friends that are unique to each address book
	 * (the union of all the relative complements). For example:
	 * 
	 * Book1 = { “Bob”, “Mary”, “Jane” }
	 * Book2 = { “Mary”, “John”, “Jane” }
	 * 
	 * The friends that are unique to each address book are: 
	 * Book1 \ Book2 = { “Bob”, “John” } 
	 * 
	 * @param book1
	 * @param book2
	 * @return
	 */
	@RequestMapping(value="/search/union")
	@Query(value="SELECT p FROM Person p where "+
	"((SELECT b FROM Book b WHERE b.id=:book1) MEMBER OF p.books AND (SELECT b FROM Book b WHERE b.id=:book2) NOT MEMBER OF p.books) OR "+
	"((SELECT b FROM Book b WHERE b.id=:book2) MEMBER OF p.books AND (SELECT b FROM Book b WHERE b.id=:book1) NOT MEMBER OF p.books) "+
	"ORDER BY p.name")
	List<Person> union(@Param("book1") Long book1, @Param("book2") Long book2);

}