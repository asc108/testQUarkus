package com.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import javax.sql.DataSource;
import jakarta.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    DataSource dataSource;

    @GET
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM users")) {
            while (rs.next()) {
                users.add(new User(rs.getLong("id"), rs.getString("name")));
            }
        }
        return users;
    }

    @POST
    public User create(User user) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO users (name) VALUES (?)", 
                 Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.id = rs.getLong(1);
                }
            }
        }
        return user;
    }
}