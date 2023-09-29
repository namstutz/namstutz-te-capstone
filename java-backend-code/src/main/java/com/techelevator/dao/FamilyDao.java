package com.techelevator.dao;

import com.techelevator.model.Family;
import com.techelevator.model.User;

import java.util.List;

public interface FamilyDao {

    Family getFamilyById(int id);

    Family createFamily(String familyName);

}
