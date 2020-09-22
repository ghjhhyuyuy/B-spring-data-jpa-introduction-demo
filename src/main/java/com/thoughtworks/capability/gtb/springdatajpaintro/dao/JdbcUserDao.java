package com.thoughtworks.capability.gtb.springdatajpaintro.dao;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserDao implements UserDao {

    @Value("${spring.datasource.url}")
    private String connectionUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public List<User> findAll() {
        try (Connection conn = getConnection()) {
            PreparedStatement selectStatement = conn.prepareStatement("select * from user");
            ResultSet rs = selectStatement.executeQuery();

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                Long id = rs.getLong("id");
                Long age = rs.getLong("age");
                String name = rs.getString("name");
                String avatar = rs.getString("avatar");
                String description = rs.getString("description");

                User user = User.builder()
                        .id(id)
                        .age(age)
                        .name(name)
                        .avatar(avatar)
                        .description(description)
                        .build();
                users.add(user);
            }

            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Operate DB failed!");
        }
    }

    @Override
    public Optional<User> findOneById(Long id) {
        try (Connection conn = getConnection()) {
            PreparedStatement selectStatement = conn.prepareStatement("select * from user where id = ?");
            selectStatement.setLong(1, id);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                Long age = rs.getLong("age");
                String name = rs.getString("name");
                String avatar = rs.getString("avatar");
                String description = rs.getString("description");

                return Optional.of(User.builder()
                        .id(id)
                        .age(age)
                        .name(name)
                        .avatar(avatar)
                        .description(description)
                        .build());
            }

            return Optional.empty();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Operate DB failed!");
        }
    }

    @Override
    public void save(User user) {
        try (Connection conn = getConnection()) {
            PreparedStatement insertStatement = conn.prepareStatement(
                    "insert into user (age, name, avatar, description) values (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, user.getAge());
            insertStatement.setString(2, user.getName());
            insertStatement.setString(3, user.getAvatar());
            insertStatement.setString(4, user.getDescription());
            int insertedRows = insertStatement.executeUpdate();

            if (0 == insertedRows) {
                throw new RuntimeException("Insert user failed!");
            }

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new RuntimeException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Operate DB failed!");
        }
    }

    private Connection getConnection() throws SQLException {
        System.out.println(connectionUrl);
        return DriverManager.getConnection(connectionUrl, username, password);
    }
}
