package com.example.my_books_backend.service;

import java.util.List;
import com.example.my_books_backend.dto.book.BookDetailsResponse;
import com.example.my_books_backend.dto.book.BookResponse;

public interface BookService {
    /**
     * 最新の書籍リスト（１０冊）
     * 
     * @return 書籍リスト
     */
    List<BookResponse> getTop10RecentBooks();

    /**
     * タイトルで書籍を検索したリストを取得
     * 
     * @param keyword 検索キーワード
     * @return 検索結果
     */
    List<BookResponse> getBooksByTitleKeyword(String keyword);

    /**
     * ジャンルIDで書籍を検索したリストを取得
     * 
     * @param genreIdsQuery カンマ区切りのジャンルIDリスト（例："1,2,3"）
     * @param conditionQuery 検索条件（"SINGLE"、"AND"、"OR"のいずれか）
     * @return 検索結果
     */
    List<BookResponse> getBooksByGenre(String genreIdsQuery, String conditionQuery);

    /**
     * 指定された書籍の詳細情報を取得
     * 
     * @param id 書籍ID
     * @return 書籍の詳細情報
     */
    BookDetailsResponse getBookDetails(String id);
}