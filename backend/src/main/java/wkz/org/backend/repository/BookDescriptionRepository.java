package wkz.org.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wkz.org.backend.entity.BookDescription;

public interface BookDescriptionRepository extends MongoRepository<BookDescription, Long> {

}
