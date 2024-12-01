package wkz.org.backend.entity;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@Data
public class Category {
	@Id
	@GeneratedValue private Long id;

	private String tag;

	@Relationship(type = "SUB_CATEGORY", direction = Relationship.Direction.OUTGOING)
	private Set<Category> subCategories = new HashSet<>();

	public void addSubCategory(Category category) {
		subCategories.add(category);
	}
}
