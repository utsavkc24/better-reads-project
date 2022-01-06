package io.javabrains.betterreads.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {
    private final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            model.addAttribute("book", book);
            String coverImageUrl = "images/no-image.jpg";
            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            }

            model.addAttribute("coverImage", coverImageUrl);
            return "books";
        }
        return "book-not-found";
    }
}
