package com.techelevator.model;

import java.util.List;

public class OpenLibraryBook {

    private String title;
    private List<OpenLibraryAuthorKey> authors;

    public OpenLibraryBook() {
    }

    public OpenLibraryBook(String title, List<OpenLibraryAuthorKey> authors) {
        this.title = title;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OpenLibraryAuthorKey> getAuthors() {
        return authors;
    }

    public void setAuthors(List<OpenLibraryAuthorKey> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "OpenLibraryBook{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                '}';
    }
}
