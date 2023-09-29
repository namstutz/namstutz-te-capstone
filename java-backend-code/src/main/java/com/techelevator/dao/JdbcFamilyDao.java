package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.Family;

@Component
public class JdbcFamilyDao implements FamilyDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcFamilyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Family getFamilyById(int id) {
        Family family = null;
        String sql = "SELECT family_id, family_name FROM families WHERE family_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                family = mapRowToFamily(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return family;
    }

    @Override
    public Family createFamily(String familyName) {
        Family newFamily;

        String insertFamilySql = "INSERT INTO families (family_name) VALUES (?) RETURNING family_id";
        try {
            int newFamilyId = jdbcTemplate.queryForObject(insertFamilySql, int.class, familyName);
            newFamily = getFamilyById(newFamilyId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newFamily;
    }

    private Family mapRowToFamily(SqlRowSet rs) {
        Family family = new Family();
        family.setId(rs.getInt("family_id"));
        family.setName(rs.getString("family_name"));
        return family;
    }

}
