package com.logansrings.booklibrary.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.logansrings.booklibrary.bean.AuthorBean;
import com.logansrings.booklibrary.bean.BookBean;
import com.logansrings.booklibrary.webservice.dto.AuthorDto;

public class ObjectFactory {

//	public static List<BookBean> createBookBeans(Collection<Book> books) {
//		return createBookBeans(books);
//	}

	public static List<BookBean> createBookBeans(Collection<Book> books) {
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
		return createBookBean(findBook(bookId));
	}

	public static Book findBook(Long bookId) {
		return Book.find(bookId);
	}

	public static List<Book> getBooks(List<BookBean> bookBeans) {
		List<Book> books = new ArrayList<Book>();
		for (BookBean bookBean : bookBeans) {
			books.add(getBook(bookBean));
		}
		return books;
	}

	public static Book getBook(BookBean bookBean) {
		return Book.find(bookBean.getTitle(), 
				Author.find(bookBean.getAuthorId()));
	}

	public static Book createBook(String title, Long authorId) {
		return Book.create(title, Author.find(authorId));
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
