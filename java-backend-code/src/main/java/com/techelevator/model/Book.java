package com.techelevator.model;

import java.sql.Timestamp;

public class Book {

    private int id;
    private int familyId;
    private String isbn = "";
    private String title;
    private String author = "";
    private String coverUrl = "";
    private String note = "";
    private boolean completed = false;
    private boolean recommended = false;
    private Timestamp lastRead;
    private boolean forChildren = true;

    public Book() {
    }

    public Book(int id, int familyId, String isbn, String title, String author, String coverUrl, String note, boolean forChildren) {
        this.id = id;
        this.familyId = familyId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.note = note;
        this.forChildren = forChildren;
    }

    public Book(int id, int familyId, String isbn, String title, String author, String coverUrl, String note, boolean forChildren, boolean completed, boolean recommended, Timestamp lastRead) {
        this.id = id;
        this.familyId = familyId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.note = note;
        this.forChildren = forChildren;
        this.completed = completed;
        this.recommended = recommended;
        this.lastRead = lastRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public Timestamp getLastRead() {
        return lastRead;
    }

    public void setLastRead(Timestamp lastRead) {
        this.lastRead = lastRead;
    }

    public boolean isForChildren() {
        return forChildren;
    }

    public void setForChildren(boolean forChildren) {
        this.forChildren = forChildren;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", familyId=" + familyId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", note='" + note + '\'' +
                ", completed=" + completed +
                ", recommended=" + recommended +
                ", lastRead=" + lastRead +
                ", forChildren=" + forChildren +
                '}';
    }
}
