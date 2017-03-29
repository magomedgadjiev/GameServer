package application.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserProfileMapper implements RowMapper<UserProfile> {
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfile user = new UserProfile();
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("nickname"));
        user.setPassword(rs.getString("password"));
        user.setRating(rs.getInt("rating"));
        return user;
    }
}