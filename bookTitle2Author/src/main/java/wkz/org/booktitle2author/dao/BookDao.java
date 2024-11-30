package wkz.org.booktitle2author.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao {
	List<String> getTitle2Author(String title);

}
