package com.example.my_books_backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.my_books_backend.dto.book.BookDetailsResponse;
import com.example.my_books_backend.dto.book.BookResponse;
import com.example.my_books_backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "書籍")
public class BookController {
    private final BookService bookService;

    @Operation(description = "最新の書籍リスト（１０冊）")
    @GetMapping("/new-releases")
    public ResponseEntity<List<BookResponse>> getTop10RecentBooks() {
        List<BookResponse> response = bookService.getTop10RecentBooks();
        return ResponseEntity.ok(response);
    }

    @Operation(description = "タイトル検索: 指定されたタイトルから書籍を検索")
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> getBooksByTitleKeyword(
        @Parameter(description = "タイトルに指定された文字列を含む書籍を検索", example = "魔法", required = true) @RequestParam String q
    ) {
        List<BookResponse> response = bookService.getBooksByTitleKeyword(q);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "ジャンル検索: 指定されたジャンルIDと条件に基づいて書籍を検索")
    @GetMapping("/discover")
    public ResponseEntity<List<BookResponse>> getBooksByGenre(
        @Parameter(description = """
            検索対象のジャンルIDをカンマ区切りで指定
            - 単一ジャンル: 1
            - 複数ジャンル: 1,2,3
            """, example = "1,2", required = true) @RequestParam String genreIds,
        @Parameter(description = """
            ジャンル検索の条件を指定
            - SINGLE: 指定したジャンルのみ（複数指定の場合、最初のジャンルのみ）
            - AND: 指定したすべてのジャンル
            - OR: 指定したいずれかのジャンル
            """, example = "AND", required = true, schema = @Schema(allowableValues = { "SINGLE",
            "AND",
            "OR" })) @RequestParam String condition
    ) {
        List<BookResponse> response = bookService.getBooksByGenre(genreIds, condition);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "特定の書籍の詳細")
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailsResponse> getBookDetails(@PathVariable String id) {
        BookDetailsResponse response = bookService.getBookDetails(id);
        return ResponseEntity.ok(response);
    }
}
