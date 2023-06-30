package org.yearup.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;

@Component
public class JdbcShoppingCartDao implements ShoppingCartDao {
    private final DataSource dataSource;
@Autowired
    public JdbcShoppingCartDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = "SELECT * FROM "
    }
}
