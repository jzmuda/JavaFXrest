package pl.spring.demo.dao;

import pl.spring.demo.entity.BookEntity;

import java.util.List;

public interface BookDao extends Dao<BookEntity, Long> {

    List<BookEntity> findBookByTitle(String title);
    
    List<BookEntity> findBookByAuthor(String title);
    
    List<BookEntity> findBookByAuthorOrTitle(String author, String title);
}
