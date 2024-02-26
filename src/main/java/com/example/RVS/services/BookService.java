package com.example.RVS.services;

import com.example.RVS.entities.Book.BookEntity;
import com.example.RVS.repositories.AuthorRepository;
import com.example.RVS.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public List<BookEntity> getBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public boolean updateBook(BookEntity update) {
        if(Objects.isNull(update.getId()) || update.getId() <= 0)
            return false;

        bookRepository.save(update);

        return true;
    }





}
