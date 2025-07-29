package com.example.my_books_backend.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.my_books_backend.dto.genre.GenreResponse;
import com.example.my_books_backend.dto.book.BookDetailsResponse;
import com.example.my_books_backend.dto.book.BookResponse;
import com.example.my_books_backend.entity.Book;
import com.example.my_books_backend.entity.Genre;
import com.example.my_books_backend.exception.BadRequestException;
import com.example.my_books_backend.exception.NotFoundException;
import com.example.my_books_backend.mapper.BookMapper;
import com.example.my_books_backend.repository.BookRepository;
import com.example.my_books_backend.service.BookService;
import com.example.my_books_backend.service.GenreService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    private final GenreService genreService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookResponse> getBooks() {
        List<Book> books = bookRepository.findByIsDeletedFalse();
        return bookMapper.toBookResponseList(books);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookResponse> getBooksByTitleKeyword(String keyword) {
        List<Book> books = bookRepository.findByTitleContainingAndIsDeletedFalse(keyword);
        return bookMapper.toBookResponseList(books);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookResponse> getBooksByGenre(String genreIdsQuery, String conditionQuery) {
        if (!("SINGLE".equals(conditionQuery)
            || "AND".equals(conditionQuery)
            || "OR".equals(conditionQuery))) {
            throw new BadRequestException("検索条件が不正です。");
        }

        List<Long> genreIds = Arrays.stream(genreIdsQuery.split(","))
            .map(String::trim)
            .map(this::parseGenreId)
            .collect(Collectors.toList());

        Boolean isAndCondition = "AND".equals(conditionQuery);

        List<Book> books = isAndCondition
            ? bookRepository.findBooksHavingAllGenres(genreIds, (long) genreIds.size())
            : bookRepository.findDistinctByGenres_IdInAndIsDeletedFalse(genreIds);

        return bookMapper.toBookResponseList(books);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookDetailsResponse getBookDetails(String id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book not found"));

        List<Long> bookGenreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toList());

        List<GenreResponse> relevantGenres = genreService.getGenresByIds(bookGenreIds);

        BookDetailsResponse response = bookMapper.toBookDetailsResponse(book);
        response.setGenres(relevantGenres);

        return response;
    }

    // ----プライベートメソッド----

    private Long parseGenreId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid genre ID: " + id);
        }
    }
}
