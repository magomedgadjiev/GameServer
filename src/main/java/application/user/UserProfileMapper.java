package application.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileMapper implements RowMapper<UserProfile> {
    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        final UserProfile user = new UserProfile();
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("nickname"));
        user.setPassword(rs.getString("password"));
        user.setWins(rs.getInt("wins"));
        user.setWins(rs.getInt("losses"));
        user.setWins(rs.getInt("draws"));
        return user;
    }
}