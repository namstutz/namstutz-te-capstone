package com.techelevator.controller;

import com.techelevator.dao.BookDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Book;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/books")
public class BooksController {

    private final BookDao bookDao;
    private final UserDao userDao;

    public BooksController(BookDao bookDao, UserDao userDao) {
        this.bookDao = bookDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = {"", "/user/{targetId}"}, method = RequestMethod.GET)
    public List<Book> getBooks(@PathVariable(required = false) Integer targetId, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        if (targetId != null) {
            User targetUser = getUserFromId(targetId);
            if (user.getFamilyId() != targetUser.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Target User Not In Family");
            }
            user = targetUser;
        }

        try {
            return bookDao.getBooks(user.getFamilyId(), user.getId(), user.isChild());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Family Users");
        }
    }

    @RequestMapping(path = {"/{id}", "/{id}/user/{targetId}"}, method = RequestMethod.GET)
    public Book getBookById(@PathVariable int id, @PathVariable(required = false) Integer targetId, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        if (targetId != null) {
            User targetUser = getUserFromId(targetId);
            if (user.getFamilyId() != targetUser.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Target User Not In Family");
            }
            user = targetUser;
        }

        try {
            Book book = bookDao.getBookById(id, user.getId());
            if (book == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Book Not In Collection");
            }
            if (book.getFamilyId() != user.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Book Not In Collection");
            }
            return book;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Book");
        }
    }

    @RequestMapping(path = {"/recommended", "/recommended/user/{targetId}"}, method = RequestMethod.GET)
    public Book getRecommendedBook(@PathVariable(required = false) Integer targetId, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        if (targetId != null) {
            User targetUser = getUserFromId(targetId);
            if (user.getFamilyId() != targetUser.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Target User Not In Family");
            }
            user = targetUser;
        }

        try {
            Book book = bookDao.getRecommendedBook(user.getFamilyId(), user.getId(), user.isChild());
            if (book == null) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Books In Collection");
            }
            return book;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Book");
        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Book addBook(@RequestBody Book book, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);

        Book newBook;
        try {
            newBook = bookDao.addBook(book, user.getFamilyId(), user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding book");
        }
        return newBook;
    }

    @RequestMapping(path = "/{bookId}", method = RequestMethod.PATCH)
    public void modifyBookStatus(@PathVariable int bookId,
                                 @RequestParam(required = false) Boolean completed,
                                 @RequestParam(defaultValue = "0") int userId,
                                 Principal userPrincipal) {
        User currentUser = getUserFromPrincipal(userPrincipal);
        if (userId == 0) {
            userId = currentUser.getId();
        } else if (currentUser.getId() != userId) {
            try {
                User targetUser = userDao.getUserById(userId);
                if (currentUser.getFamilyId() != targetUser.getFamilyId()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User Not In Current Family");
                }
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Confirm User in Family");
            }
        }

        if (completed != null) {
            try {
                bookDao.setBookCompleted(bookId, completed, userId);
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating book");
            }
        }
    }

    //Private Methods

    //TODO instead of making a Dao call for user details, add info to Principal by extending User
    //https://stackoverflow.com/questions/20349594/adding-additional-details-to-principal-object-stored-in-spring-security-context
    private User getUserFromPrincipal(Principal userPrincipal) {
        try {
            return userDao.getUserByUsername(userPrincipal.getName());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User Not Found");
        }
    }

    private User getUserFromId(int id) {
        try {
            return userDao.getUserById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User Not Found");
        }
    }

}
