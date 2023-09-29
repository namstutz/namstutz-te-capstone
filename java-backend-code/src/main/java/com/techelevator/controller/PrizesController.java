package com.techelevator.controller;

import com.techelevator.dao.PrizeDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Prize;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/prizes")
public class PrizesController {

    private final PrizeDao prizeDao;
    private final UserDao userDao;

    public PrizesController(PrizeDao prizeDao, UserDao userDao) {
        this.prizeDao = prizeDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = {"", "/user/{targetId}"}, method = RequestMethod.GET)
    public List<Prize> getPrizes(@PathVariable(required = false) Integer targetId, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        if (targetId != null) {
            User targetUser = getUserFromId(targetId);
            if (user.getFamilyId() != targetUser.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Target User Not In Family");
            }
            user = targetUser;
        }

        try {
            return prizeDao.getPrizes(user.getFamilyId(), user.getId(), user.isChild());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Family Users");
        }
    }

    @RequestMapping(path = "/active", method = RequestMethod.GET)
    public List<Prize> getActivePrizes(Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);

        try {
            return prizeDao.getActivePrizes(new Timestamp(System.currentTimeMillis()), user);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Get Active Prizes");
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Prize getPrizeById(@PathVariable int id, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);

        try {
            Prize prize = prizeDao.getPrizeById(id, user.getId());
            if (prize == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Prize Not In Family");
            }
            if (prize.getFamilyId() != user.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Prize Not In Family");
            }
            return prize;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Prize");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Prize addPrize(@RequestBody Prize prize, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);

        try {
            return prizeDao.addPrize(prize, user.getFamilyId(), user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding prize");
        }
    }

    @RequestMapping(path = {"", "/{prizeId}"}, method = RequestMethod.PUT)
    public Prize updatePrize(@PathVariable(required = false) Integer prizeId, @RequestBody Prize prize, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        Prize prizeToUpdate;

        if (prizeId != null) {
            prize.setId(prizeId);
        }

        try {
            prizeToUpdate = prizeDao.getPrizeById(prize.getId(), user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not find prize to update");
        }
        if (prizeToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not find prize to update");
        }
        if (user.getFamilyId() != prizeToUpdate.getFamilyId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Prize Not In Family");
        }

        try {
            return prizeDao.updatePrize(prize, user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating prize");
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deletePrize(@PathVariable int id, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        Prize prizeToDelete;

        try {
            prizeToDelete = prizeDao.getPrizeById(id, user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not find prize to update");
        }
        if (user.getFamilyId() != prizeToDelete.getFamilyId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Prize Not In Family");
        }

        try {
            prizeDao.deletePrize(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting prize");
        }

    }
    //Private Methods

    //TODO instead of making a Dao call for user details, add info to Principal by extending User
    //https://stackoverflow.com/questions/20349594/adding-additional-details-to-principal-object-stored-in-spring-security-context
    private User getUserFromPrincipal(Principal userPrincipal) {
        try {
            return userDao.getUserByUsername(userPrincipal.getName());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User Not Found");
        }
    }

    private User getUserFromId(int id) {
        try {
            return userDao.getUserById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User Not Found");
        }
    }

}

