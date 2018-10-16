package pwc.addressbook;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	List<Person> findByName(@Param("name") String name);
	
	@Query(value="SELECT p FROM Person p where (SELECT b FROM Book b WHERE b.id=:book) MEMBER OF p.books ORDER BY p.name")
	List<Person> friends(@Param("book") Long book);
	
	@RequestMapping(value="/search/union")
	@Query(value="SELECT p FROM Person p where "+
	"((SELECT b FROM Book b WHERE b.id=:book1) NOT MEMBER OF p.books AND (SELECT b FROM Book b WHERE b.id=:book2) MEMBER OF p.books) OR "+
	"((SELECT b FROM Book b WHERE b.id=:book2) NOT MEMBER OF p.books AND (SELECT b FROM Book b WHERE b.id=:book1) MEMBER OF p.books) "+
	"ORDER BY p.name")
	List<Person> union(@Param("book1") Long book1, @Param("book2") Long book2);

}