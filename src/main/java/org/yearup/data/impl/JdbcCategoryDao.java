package org.yearup.data.impl;

import com.mysql.cj.protocol.Resultset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCategoryDao implements CategoryDao {
    private final DataSource dataSource;

    @Autowired
    public JdbcCategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultset = preparedStatement.executeQuery()) {

            while (resultset.next()) {
                int categoryId = resultset.getInt(1);
                String name = resultset.getString(2);
                String description = resultset.getString(3);

                Category category = new Category(categoryId, name, description);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String query = "SELECT * FROM categories WHERE category_id = ?;";
        Category category = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int categoryIdFromDB = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String description = resultSet.getString(3);

                    category = new Category(categoryIdFromDB, name, description);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        String query = "INSERT INTO categories (name, description) values (?,?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    category.setCategoryId(generatedId);
                } else {
                    throw new SQLException("Creating category failed.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        String query = "UPDATE categories SET name = ? AND description = ? WHERE category_id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        String query = "DELETE FROM categories WHERE category_id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
