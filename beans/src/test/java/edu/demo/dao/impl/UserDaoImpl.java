package edu.demo.dao.impl;

import edu.demo.dao.UserDao;
import edu.nf.beans.annotations.Component;

@Component("dao")
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("INSERT INTO USER_INFO...");
    }
}
