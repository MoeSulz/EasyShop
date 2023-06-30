package org.yearup.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class JdbcShoppingCartDao implements ShoppingCartDao {
    private final DataSource dataSource;
@Autowired
    public JdbcShoppingCartDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = "SELECT * FROM shopping_cart WHERE user_id = ?;";
        ShoppingCart shoppingCart = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    int userIDFromDB = resultSet.getInt(1);
                    int productID = resultSet.getInt(2);
                    int quantity = resultSet.getInt(3);

                    shoppingCart = new ShoppingCart(userIDFromDB, productID, quantity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }
}
