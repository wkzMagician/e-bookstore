package wkz.org.booktitle2author.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wkz.org.booktitle2author.dao.BookDao;

@RestController
public class BookController {
	@Autowired
	private BookDao bookDao;

	@GetMapping("/")
	public String title2author(@QueryParam("title") String title) {
		return JSON.toJSONString(bookDao.getTitle2Author(title));
	}
}
