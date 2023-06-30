package org.yearup.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class JdbcProfileDao implements ProfileDao {
    private final DataSource dataSource;
@Autowired
    public JdbcProfileDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Profile create(Profile profile) {
        String query = "INSERT INTO profiles SET first_name = ? AND last_name = ? AND phone = ? AND email = ? AND address = ? AND city = ? AND state = ? AND zip = ?;";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, profile.getFirstName());
            preparedStatement.setString(2, profile.getLastName());
            preparedStatement.setString(3, profile.getPhone());
            preparedStatement.setString(4, profile.getEmail());
            preparedStatement.setString(5, profile.getAddress());
            preparedStatement.setString(6, profile.getCity());
            preparedStatement.setString(7, profile.getState());
            preparedStatement.setString(8, profile.getZip());

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedID = keys.getInt(1);
                    profile.setUserId(generatedID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return profile;
    }
}
