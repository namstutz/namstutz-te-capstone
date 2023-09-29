package com.techelevator.model;

public class OpenLibraryAuthorKey {

    private String key;

    public OpenLibraryAuthorKey() {
    }

    public OpenLibraryAuthorKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "OpenLibraryAuthorKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
