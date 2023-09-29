package com.techelevator.dao;

import com.techelevator.model.Session;

import java.util.List;

public interface SessionDao {

    List<Session> getSessions(int userId);

    List<Session> getSessionsByBookId(int bookId, int userId);

    Session addSession(Session session);

}
