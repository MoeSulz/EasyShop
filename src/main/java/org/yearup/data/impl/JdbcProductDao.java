package org.yearup.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProductDao implements ProductDao {
    private final DataSource dataSource;

    @Autowired
    public JdbcProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color) {
        return null;
    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int productId = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    BigDecimal price = resultSet.getBigDecimal(3);
                    int categoryIdFromDB = resultSet.getInt(4);
                    String description = resultSet.getString(5);
                    String color = resultSet.getString(6);
                    String imageUrl = resultSet.getString(7);
                    int stock = resultSet.getInt(8);
                    boolean isFeatured = resultSet.getBoolean(9);

                    products.add(new Product(productId, name, price, categoryIdFromDB, description, color, stock, isFeatured, imageUrl));

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public Product getById(int productId) {
        String query = "SELECT * FROM products WHERE product_id = ?;";
        Product product = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, productId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int productIdFromDB = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    BigDecimal price = resultSet.getBigDecimal(3);
                    int categoryIdFromDB = resultSet.getInt(4);
                    String description = resultSet.getString(5);
                    String color = resultSet.getString(6);
                    String imageUrl = resultSet.getString(7);
                    int stock = resultSet.getInt(8);
                    boolean isFeatured = resultSet.getBoolean(9);

                    product = new Product(productIdFromDB, name, price, categoryIdFromDB, description, color, stock, isFeatured, imageUrl);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    @Override
    public Product create(Product product) {
        String query = "INSERT INTO products (name, price, category_id, description, color, image_url, stock, featured) values (?,?,?,?,?,?,?,?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getCategoryId());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setString(5, product.getColor());
            preparedStatement.setString(6, product.getImageUrl());
            preparedStatement.setInt(7, product.getStock());
            preparedStatement.setBoolean(8, product.isFeatured());

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedID = keys.getInt(1);
                    product.setProductId(generatedID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    @Override
    public void update(int productId, Product product) {
        String query = "UPDATE products SET name = ? AND price = ? AND category_id = ? AND description = ? AND color = ? AND stock = ? AND featured = ? AND image_url = ? WHERE product_id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getCategoryId());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setString(5, product.getColor());
            preparedStatement.setInt(6, product.getStock());
            preparedStatement.setBoolean(7, product.isFeatured());
            preparedStatement.setString(8, product.getImageUrl());
            preparedStatement.setInt(9, productId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
