package wkz.org.backend.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Description")
public class BookDescription {
	@Id
	private Long id;

	@Setter
	@Getter
	private String description;

	public BookDescription(Long id, String description) {
		this.id = id;
		this.description = description;
	}

}
