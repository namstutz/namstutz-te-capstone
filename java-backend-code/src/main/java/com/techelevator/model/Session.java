package com.techelevator.model;

import java.sql.Timestamp;

public class Session {

    private int id;
    private int userId;
    private int bookId;
    private int minutes;
    private Timestamp startDateTime;
    private String format;
    private String note;

    public Session() {
    }

    public Session(int id, int userId, int bookId, int minutes, Timestamp startDateTime, String format, String note) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.minutes = minutes;
        this.startDateTime = startDateTime;
        this.format = format;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", minutes=" + minutes +
                ", startDateTime=" + startDateTime +
                ", format='" + format + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
