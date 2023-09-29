package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Prize;
import com.techelevator.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPrizeDao implements PrizeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPrizeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Prize> getPrizes(int familyId, int userId, boolean isChild) {
        List<Prize> prizes = new ArrayList<>();

        String sql = "SELECT p.prize_id, p.family_id, p.prize_name, p.description, p.milestone, p.for_parents, p.for_children, p.max_prizes, p.claimed_prizes, p.start_date, p.end_date, " +
                "COALESCE(up.progress_minutes, 0) AS progress_minutes, " +
                "COALESCE(up.completed, false) AS completed, " +
                "up.completion_timestamp, " +
                "(CURRENT_TIMESTAMP(0) BETWEEN p.start_date AND p.end_date) AS currently_active, " +
                "(CURRENT_TIMESTAMP(0) > p.end_date) AS expired " +
                "FROM prizes AS p " +
                "LEFT JOIN users_prizes AS up " +
                "ON p.prize_id = up.prize_id AND up.user_id = ? " +
                "WHERE p.family_id = ? ";
        if (isChild) {
            sql += "AND p.for_children = true ";
        }
        sql +=  "ORDER BY currently_active DESC, start_date DESC;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, familyId);
            while (results.next()) {
                Prize prize = mapRowToPrize(results);
                prizes.add(prize);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return prizes;
    }

    @Override
    public Prize getPrizeById(int prizeId, int userId) {
        Prize prize = null;

        String sql = "SELECT p.prize_id, p.family_id, p.prize_name, p.description, p.milestone, p.for_parents, p.for_children, p.max_prizes, p.claimed_prizes, p.start_date, p.end_date, " +
                "COALESCE(up.progress_minutes, 0) AS progress_minutes, " +
                "COALESCE(up.completed, false) AS completed, " +
                "up.completion_timestamp, " +
                "(CURRENT_TIMESTAMP(0) BETWEEN p.start_date AND p.end_date) AS currently_active, " +
                "(CURRENT_TIMESTAMP(0) > p.end_date) AS expired " +
                "FROM prizes AS p " +
                "LEFT JOIN users_prizes AS up " +
                "ON p.prize_id = up.prize_id AND up.user_id = ? " +
                "WHERE p.prize_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, prizeId);
            if (results.next()) {
                prize = mapRowToPrize(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return prize;
    }

    @Override
    public List<Prize> getActivePrizes(Timestamp timestamp, User user) {
        List<Prize> prizes = new ArrayList<>();

        String sql = "SELECT p.prize_id, p.family_id, p.prize_name, p.description, p.milestone, p.for_parents, p.for_children, p.max_prizes, p.claimed_prizes, p.start_date, p.end_date, " +
                "COALESCE(up.progress_minutes, 0) AS progress_minutes, " +
                "COALESCE(up.completed, false) AS completed, " +
                "up.completion_timestamp, " +
                "true AS currently_active, " +
                "false AS expired " +
                "FROM prizes AS p " +
                "LEFT JOIN users_prizes AS up " +
                "ON p.prize_id = up.prize_id AND up.user_id = ? " +
                "WHERE p.family_id = ? ";
        if (user.isParent()) {
            sql += "AND p.for_parents = true ";
        } else if (user.isChild()) {
            sql += "AND p.for_children = true ";
        }
        sql +=  "AND p.start_date < ? AND p.end_date > ? " +
                "ORDER BY start_date DESC;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user.getId(), user.getFamilyId(), timestamp, timestamp);
            while (results.next()) {
                Prize prize = mapRowToPrize(results);
                prizes.add(prize);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return prizes;

    }

    @Override
    public Prize addPrize(Prize prize, int familyId, int userId) {
        Prize newPrize = null;

        String sql = "INSERT INTO prizes (family_id, prize_name, description, milestone, for_parents, for_children, max_prizes, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING prize_id;";
        try {
            int newPrizeId = jdbcTemplate.queryForObject(sql, int.class, familyId, prize.getPrizeName(), prize.getDescription(),
                    prize.getMilestone(), prize.isForParents(), prize.isForChildren(), prize.getMaxPrizes(), prize.getStartDate(), prize.getEndDate());
            newPrize = getPrizeById(newPrizeId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return newPrize;
    }

    @Override
    public Prize updatePrize(Prize prize, int userId) {
        Prize updatedPrize = null;

        String sql = "UPDATE prizes " +
                "SET prize_name = ?, description = ?, milestone = ?, for_parents = ?, for_children = ?, max_prizes = ?, start_date = ?, end_date = ? " +
                "WHERE prize_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, prize.getPrizeName(), prize.getDescription(), prize.getMilestone(),
                    prize.isForParents(), prize.isForChildren(), prize.getMaxPrizes(), prize.getStartDate(), prize.getEndDate(),
                    prize.getId());
            if (numberOfRows != 1) {
                throw new DaoException("Prize not found.");
            } else {
                updatedPrize = getPrizeById(prize.getId(), userId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return updatedPrize;
    }

    @Override
    public void deletePrize(int prizeId) {
        String sql = "DELETE FROM prizes WHERE prize_id = ?;";

        try {
            int numberOfRows = jdbcTemplate.update(sql, prizeId);
            if (numberOfRows != 1) {
                throw new DaoException("Prize not found.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }

    @Override
    public void increaseProgress(int prizeId, int userId, int minutes) {
        String sql = "INSERT INTO users_prizes (user_id, prize_id, progress_minutes)" +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, prize_id) " +
                "DO UPDATE SET progress_minutes = users_prizes.progress_minutes + ?;";
        try {
            jdbcTemplate.update(sql, userId, prizeId, minutes, minutes);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }

    @Override
    public List<Integer> updateCompletion() {
        List<Integer> completedPrizes = new ArrayList<>();

        String sql = "UPDATE users_prizes AS up " +
                "SET completed = true," +
                "completion_timestamp = CURRENT_TIMESTAMP(0) " +
                "FROM prizes AS p " +
                "WHERE up.prize_id = p.prize_id " +
                "AND (p.max_prizes = 0 OR p.max_prizes - p.claimed_prizes > 0) " +
                "AND up.completed = false " +
                "AND up.progress_minutes >= p.milestone " +
                "RETURNING p.prize_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                completedPrizes.add(results.getInt("prize_id"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }

        String sql2 = "UPDATE prizes " +
                "SET claimed_prizes = claimed_prizes + 1 " +
                "WHERE prize_id = ?;";
        try {
            for (Integer prizeId : completedPrizes) {
                jdbcTemplate.update(sql2, prizeId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }

        return completedPrizes;
    }

    private Prize mapRowToPrize(SqlRowSet rs) {
        Prize prize = new Prize();
        prize.setId(rs.getInt("prize_id"));
        prize.setFamilyId(rs.getInt("family_id"));
        prize.setPrizeName(rs.getString("prize_name"));
        prize.setDescription(rs.getString("description"));
        prize.setMilestone(rs.getInt("milestone"));
        prize.setClaimedPrizes(rs.getInt("claimed_prizes"));
        prize.setProgressMinutes(rs.getInt("progress_minutes"));
        prize.setForParents(rs.getBoolean("for_parents"));
        prize.setForChildren(rs.getBoolean("for_children"));
        prize.setMaxPrizes(rs.getInt("max_prizes"));
        prize.setStartDate(rs.getTimestamp("start_date"));
        prize.setEndDate(rs.getTimestamp("end_date"));
        prize.setCompleted(rs.getBoolean("completed"));
        prize.setCompletionDate(rs.getTimestamp("completion_timestamp"));
        prize.setCurrentlyActive(rs.getBoolean("currently_active"));
        prize.setExpired(rs.getBoolean("expired"));
        return prize;
    }

}
