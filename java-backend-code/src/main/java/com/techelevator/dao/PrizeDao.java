package com.techelevator.dao;

import com.techelevator.model.Prize;
import com.techelevator.model.User;

import java.sql.Timestamp;
import java.util.List;

public interface PrizeDao {

    List<Prize> getPrizes(int familyId, int userId, boolean isChild);

    Prize getPrizeById(int prizeId, int userId);

    List<Prize> getActivePrizes(Timestamp timestamp, User user);

    Prize addPrize(Prize prize, int familyId, int userId);

    Prize updatePrize(Prize prize, int userId);

    void deletePrize(int prizeId);

    void increaseProgress(int prizeId, int userId, int minutes);

    List<Integer> updateCompletion();

}
