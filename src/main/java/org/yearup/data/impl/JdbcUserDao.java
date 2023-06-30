package org.yearup.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.UserDao;
import org.yearup.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {
    private final DataSource dataSource;
@Autowired
    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users;";
        List<User> users = new ArrayList<>();

        try(Connection connection = dataSource.getConnection())
    }

    @Override
    public User getUserById(int userId) {
        return null;
    }

    @Override
    public User getByUserName(String username) {
        return null;
    }

    @Override
    public int getIdByUsername(String username) {
        return 0;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public boolean exists(String username) {
        return false;
    }
}
