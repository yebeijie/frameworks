package edu.demo.service.impl;

import edu.demo.dao.UserDao;
import edu.demo.service.UserService;
import edu.nf.beans.annotations.Component;
import edu.nf.beans.annotations.Inject;

@Component("service")
public class UserServiceImpl implements UserService{

    @Inject(name = "dao")
    private UserDao dao;

    @Override
    public void add() {
        dao.save();
    }
}
