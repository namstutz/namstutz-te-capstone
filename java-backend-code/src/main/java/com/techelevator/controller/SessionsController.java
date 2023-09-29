package com.techelevator.controller;

import com.techelevator.dao.PrizeDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Prize;
import com.techelevator.model.Session;
import com.techelevator.dao.SessionDao;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/sessions")
public class SessionsController {

    private final SessionDao sessionDao;
    private final PrizeDao prizeDao;
    private final UserDao userDao;

    public SessionsController(SessionDao sessionDao, PrizeDao prizeDao, UserDao userDao) {
        this.sessionDao = sessionDao;
        this.prizeDao = prizeDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = {"", "/user/{targetId}"}, method = RequestMethod.GET)
    public List<Session> getSessions(@PathVariable(required = false) Integer targetId, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        if (targetId != null) {
            User targetUser = getUserFromId(targetId);
            if (user.getFamilyId() != targetUser.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Target User Not In Family");
            }
            user = targetUser;
        }

        try {
            return sessionDao.getSessions(user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Sessions");
        }
    }

    @RequestMapping(path = {"/{id}", "/{id}/user/{targetId}"}, method = RequestMethod.GET)
    public List<Session> getSessionsByBookId(@PathVariable int id, @PathVariable(required = false) Integer targetId, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);
        if (targetId != null) {
            User targetUser = getUserFromId(targetId);
            if (user.getFamilyId() != targetUser.getFamilyId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Target User Not In Family");
            }
            user = targetUser;
        }

        try {
            return sessionDao.getSessionsByBookId(id, user.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Find Sessions");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Session addSession(@RequestBody Session session, Principal userPrincipal) {
        User user = getUserFromPrincipal(userPrincipal);

        if (user.getId() != session.getUserId()) {
            try {
                User targetUser = userDao.getUserById(session.getUserId());
                if (user.getFamilyId() != targetUser.getFamilyId()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Session User Not In Current Family");
                }
                user = targetUser;
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Confirm User in Family");
            }
        }

        Session newSession;
        try {
            newSession = sessionDao.addSession(session);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Adding Session");
        }

        List<Prize> activePrizes;
        try {
            activePrizes = prizeDao.getActivePrizes(newSession.getStartDateTime(), user);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Get Current Prizes");
        }
        for (Prize prize : activePrizes) {
            try {
                prizeDao.increaseProgress(prize.getId(), user.getId(), newSession.getMinutes());
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Update Prize Progress");
            }
        }

        try {
            prizeDao.updateCompletion();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Get Current Prizes");
        }

        return newSession;
    }

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
