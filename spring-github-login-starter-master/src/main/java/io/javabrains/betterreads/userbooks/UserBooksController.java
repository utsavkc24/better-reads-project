package io.javabrains.betterreads.userbooks;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import io.javabrains.betterreads.book.Book;
import io.javabrains.betterreads.book.BookRepository;
import jnr.constants.platform.Local;

@Controller
public class UserBooksController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBooksRepository userBooksRepository;

    @PostMapping(value = "/addUserBooks")
    public ModelAndView addUserAndBooks(@AuthenticationPrincipal OAuth2User principal,
            @RequestBody MultiValueMap<String, String> formData, Model model) {

        if (principal == null || principal.getAttribute("login") == null) {
            return null;
        }

        UserBooks userBooks = new UserBooks();
        UserBooksPrimaryKey userBooksPrimaryKey = new UserBooksPrimaryKey();

        String bookId = formData.getFirst("bookId");
        String userId = principal.getAttribute("login");

        // Optional<Book> optionalBook = bookRepository.findById("bookId");

        // if (!optionalBook.isPresent()) {
        // model.addAttribute("userBooks", optionalBook.get());
        // } else {
        // model.addAttribute("userBooks", optionalBook.get());
        // }

        userBooksPrimaryKey.setUserId(userId);
        userBooksPrimaryKey.setBookId(bookId);

        userBooks.setUserBooksPrimaryKey(userBooksPrimaryKey);
        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startedDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        userBooks.setRating(Integer.parseInt(formData.getFirst("rating")));

        userBooksRepository.save(userBooks);

        return new ModelAndView("redirect:/books/" + bookId);
    }
}