package wkz.org.backend.repository;

import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import wkz.org.backend.entity.Category;

public interface CategoryRepository extends Neo4jRepository<Category, Long> {
	Category findByTag(String tag); // Find node by tag
	List<Category> findBySubCategoriesTag(String tag);

	// Find all nodes connected by two edges
	List<Category> findBySubCategoriesSubCategoriesTag(String tag);

	// Find all nodes within two edges
	@Query("MATCH (c:Category{tag:$tag})-[:SUB_CATEGORY*0..2]->(sub:Category) RETURN sub")
	List<Category> findWithinTwoSubCategories(String tag);
}