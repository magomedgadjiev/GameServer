package application.user;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by magomed on 27.03.17.
 */
@Service
public class UserProfileJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = Logger.getLogger(UserProfileJDBCTemplate.class);

    @Autowired
    public UserProfileJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable() {
        final String query = "CREATE EXTENSION IF NOT EXISTS citext; " +
                "CREATE TABLE IF NOT EXISTS user_project ( " +
                "nickname varchar(128) UNIQUE NOT NULL , " +
                "password varchar(128) NOT NULL, " +
                "email CITEXT UNIQUE NOT NULL PRIMARY KEY, " +
                "wins     INT DEFAULT 0," +
                "losses    INT DEFAULT 0," +
                "draws   INT DEFAULT 0);";
        LOGGER.debug(query +
                "create table success");
        jdbcTemplate.execute(query);
    }

    public void dropTable() {
        final String sql = "DROP TABLE IF EXISTS user_project";
        jdbcTemplate.execute(sql);
        LOGGER.debug("drop success");
    }

    public void create(String nickname, String password, String email) {
        final String sql = "INSERT INTO user_project(nickname, password, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, nickname, password, email);
        LOGGER.debug("insert success");
    }

    public UserProfile getUserProfile(String email) {
        final String sql = "SELECT * FROM user_project WHERE LOWER(email) = LOWER(?)";
        final UserProfile users = jdbcTemplate.queryForObject(sql, new UserProfileMapper(), email);
        LOGGER.debug("getUserByEmail success");
        return users;
    }

    public void updateUserProfile(UserProfile userProfile) {
        if (userProfile.getUsername() != null) {
            final String sql = "UPDATE user_project SET nickname = ? WHERE LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getUsername(), userProfile.getEmail());
        }

        if (userProfile.getPassword() != null) {
            final String sql = "UPDATE user_project SET password = ? WHERE LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getPassword(), userProfile.getEmail());
        }

        LOGGER.debug("Updated user success");
    }

    public List<UserProfile> getUsers() {
        final String sql = "SELECT * FROM user_project ORDER BY wins ";
        final List<UserProfile> users = jdbcTemplate.query(sql, new UserProfileMapper());
        LOGGER.debug("getListUsers success");
        return users;
    }

    public int getCount() {
        final String sql = "SELECT COUNT(*) FROM user_project";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class);
        LOGGER.debug("getCount success");
        return count;
    }
}
