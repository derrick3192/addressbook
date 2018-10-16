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
	
	@RequestMapping(value="/search/findallderricks")
	@Query(value="SELECT * FROM person u where u.name = 'Derrick';", nativeQuery = true)
	List<Person> findAllDerricks();

}