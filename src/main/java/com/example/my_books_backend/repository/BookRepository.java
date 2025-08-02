package com.example.my_books_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.my_books_backend.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    // 最新の書籍リスト（１０冊）
    List<Book> findTop10ByIsDeletedFalseOrderByPublicationDateDesc();

    // タイトル検索
    List<Book> findByTitleContainingAndIsDeletedFalse(String keyword);

    // 指定されたジャンルIDのリストを取得（OR条件）
    List<Book> findDistinctByGenres_IdInAndIsDeletedFalse(List<Long> genreIds);

    // 指定されたジャンルIDのリストを取得（AND条件）
    @Query("""
        SELECT DISTINCT b FROM Book b
        WHERE b.id IN (
            SELECT b2.id FROM Book b2
            JOIN b2.genres bg
            WHERE bg.id IN :genreIds
            GROUP BY b2.id
            HAVING COUNT(DISTINCT bg.id) = :size
        )
        """)
    List<Book> findBooksHavingAllGenres(
        @Param("genreIds") List<Long> genreIds,
        @Param("size") Long size
    );
}