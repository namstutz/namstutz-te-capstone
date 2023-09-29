package com.techelevator.controller;

import com.techelevator.model.Book;
import com.techelevator.service.OpenLibraryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/lookup")
public class LookupController {

    private final OpenLibraryService openLibraryService = new OpenLibraryService();

    public LookupController() {
    }

    @RequestMapping(path = "/isbn/{isbn}", method = RequestMethod.GET)
    public Book getBookInfoByIsbn(@PathVariable String isbn) {
        return openLibraryService.getBookInfoByIsbn(isbn);
    }
}
