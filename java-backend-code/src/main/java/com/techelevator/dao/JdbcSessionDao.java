package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Session;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcSessionDao implements SessionDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcSessionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Session> getSessions(int userId) {
        List<Session> sessions = new ArrayList<>();

        String sql = "SELECT session_id, user_id, book_id, minutes, format, start_date_time, note " +
                "FROM sessions " +
                "WHERE user_id = ? " +
                "ORDER BY start_date_time DESC;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                Session session = mapRowToSession(results);
                sessions.add(session);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return sessions;
    }

    @Override
    public List<Session> getSessionsByBookId(int bookId, int userId) {
        List<Session> sessions = new ArrayList<>();

        String sql = "SELECT session_id, user_id, book_id, minutes, format, start_date_time, note " +
                "FROM sessions " +
                "WHERE book_id = ? AND user_id = ? " +
                "ORDER BY start_date_time DESC;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, bookId, userId);
            while (results.next()) {
                Session session = mapRowToSession(results);
                sessions.add(session);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return sessions;
    }

    @Override
    public Session addSession(Session session) {
        Session newSession = null;

        String sql = "INSERT INTO sessions (user_id, book_id, minutes, start_date_time, format, note) " +
                "VALUES (?, ?, ?, COALESCE( ?, CURRENT_TIMESTAMP(0) ) , ?, ?) RETURNING session_id;";
        try {
            int newSessionId = jdbcTemplate.queryForObject(sql, int.class, session.getUserId(), session.getBookId(),
                session.getMinutes(), session.getStartDateTime(), session.getFormat(), session.getNote());
            newSession = getLastAddedSession(session.getUserId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return newSession;
    }

    //Private methods

    private Session getLastAddedSession(int userId) {
        Session session = null;

        String sql = "SELECT session_id, user_id, book_id, minutes, format, start_date_time, note " +
                "FROM sessions " +
                "WHERE user_id = ? " +
                "ORDER BY session_id DESC " + "" +
                "LIMIT 1;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                session = mapRowToSession(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return session;
    }

    private Session mapRowToSession(SqlRowSet rs) {
        Session session = new Session();
        session.setId(rs.getInt("session_id"));
        session.setUserId(rs.getInt("user_id"));
        session.setBookId(rs.getInt("book_id"));
        session.setMinutes(rs.getInt("minutes"));
        session.setFormat(rs.getString("format"));
        session.setStartDateTime(rs.getTimestamp("start_date_time"));
        session.setNote(rs.getString("note"));
        return session;
    }
}
