package application.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileMapper implements RowMapper<UserProfile> {
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfile user = new UserProfile();
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("nickname"));
        user.setPassword(rs.getString("password"));
        user.setRating(rs.getInt("rating"));
        return user;
    }
}