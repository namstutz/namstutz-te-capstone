package com.techelevator.service;

import com.techelevator.model.Book;
import com.techelevator.model.OpenLibraryAuthorName;
import com.techelevator.model.OpenLibraryBook;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class OpenLibraryService {

    public static String BASE_API_URL = "https://openlibrary.org/";

    private final RestTemplate restTemplate = new RestTemplate();

    public Book getBookInfoByIsbn(String isbn) {
        Book book = new Book();
        OpenLibraryBook olb = new OpenLibraryBook();

        try {
            olb = restTemplate.getForObject(BASE_API_URL + "isbn/" + isbn, OpenLibraryBook.class);
        } catch (RestClientResponseException e) {
            book.setTitle("ISBN Not Found");
            return book;
        } catch (ResourceAccessException e) {
            book.setTitle("Could Not Connect to Lookup");
            return book;
        }
        book.setTitle(olb.getTitle());

        if (olb.getAuthors() != null) {
            String[] splitKey = olb.getAuthors().get(0).getKey().split("/");
            String olid = splitKey[2];
            try {
                book.setAuthor(restTemplate.getForObject(BASE_API_URL + "authors/" + olid + ".json", OpenLibraryAuthorName.class).getName());
            } catch (RestClientResponseException e) {
                book.setAuthor("Error in Author Lookup");
            } catch (Exception e) {
                book.setAuthor("Could Not Connect to Lookup");
            }
        }
        return book;
    }
}
