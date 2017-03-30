package com.carlos;

import com.carlos.service.BookService;
import com.carlos.service.BookService2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JavaSpiderApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(JavaSpiderApplication.class, args);
		BookService bookService = (BookService) applicationContext.getBean("bookService");
		bookService.getBookInfo();
		
		ConfigurableApplicationContext applicationContext2 = SpringApplication.run(JavaSpiderApplication.class, args);
		BookService2 bookService2 = (BookService2) applicationContext2.getBean("bookService2");
		bookService2.getBookInfo();
	}
}
