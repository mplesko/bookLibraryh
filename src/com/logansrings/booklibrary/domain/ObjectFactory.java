package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.List;

import com.logansrings.booklibrary.bean.AuthorBean;
import com.logansrings.booklibrary.bean.BookBean;
import com.logansrings.booklibrary.webservice.dto.AuthorDto;

public class ObjectFactory {

	public static List<BookBean> createBookBeans(Library library) {
		return createBookBeans(library.getBooks());
	}

	public static List<BookBean> createBookBeans(List<Book> books) {
		List<BookBean> bookBeans = new ArrayList<BookBean>();
		for (Book book : books) {
			bookBeans.add(createBookBean(book));
		}
		return bookBeans;
	}

	private static BookBean createBookBean(Book book) {
		return new BookBean(book.getId(), book.getTitle(), 
				book.getAuthorId(), book.getAuthorFirstName(), book.getAuthorLastName());
	}

	public static BookBean createBookBean(Long bookId) {
		return createBookBean(createBook(bookId));
	}

	public static Book createBook(Long bookId) {
		return new Book(bookId);
	}

	public static List<Book> createBooks(List<BookBean> bookBeans) {
		List<Book> books = new ArrayList<Book>();
		for (BookBean bookBean : bookBeans) {
			books.add(createBook(bookBean));
		}
		return books;
	}

	public static Book createBook(BookBean bookBean) {
		return new Book(bookBean.getTitle(), 
				new Author(bookBean.getAuthorFirstName(), 
						bookBean.getAuthorLastName()));
	}

	public static Book createBook(String title, Long authorId) {
		return new Book(title, new Author(authorId));
	}

	public static List<AuthorBean> createAuthorBeans(List<Author> authors) {
		List<AuthorBean> authorBeans = new ArrayList<AuthorBean>();
		for (Author author : authors) {
			authorBeans.add(createAuthorBean(author));
		}
		return authorBeans;
	}

	private static AuthorBean createAuthorBean(Author author) {
		return new AuthorBean(author.getId(), author.getFirstName(), author.getLastName());
	}

    public static List<AuthorDto> createAuthorDtos(List<AuthorBean> authorBeans) {
        List<AuthorDto> authorDtos = new ArrayList<AuthorDto>();
        for (AuthorBean authorBean : authorBeans) {
                        authorDtos.add(authorBean.getDto());
        }
        return authorDtos;
}
}
