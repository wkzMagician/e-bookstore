package wkz.org.booktitle2author.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import wkz.org.booktitle2author.repository.BookRepository;

@Repository
public class BookDaoImpl implements BookDao {
	@Autowired
	private BookRepository bookRepository;

		@Override
		public List<String> getTitle2Author(String title) {
			// Page<String> findTitleByAuthor(String author, Pageable pageable);

			Pageable pageable = PageRequest.of(0, 10);
			return bookRepository.findAuthorByTitle(title, pageable).getContent();
		}

}
