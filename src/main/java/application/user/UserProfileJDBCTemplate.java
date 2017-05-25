package application.user;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by magomed on 27.03.17.
 */
@Service
@Transactional
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
        LOGGER.info(query +
                "create table success");
        jdbcTemplate.execute(query);
    }

    public void dropTable() {
        final String sql = "DROP TABLE IF EXISTS user_project";
        jdbcTemplate.execute(sql);
        LOGGER.info("drop success");
    }

    public void deleteUser(String login) {
        final String sql = "DELETE FROM user_project WHERE lower(nickname) = lower(?)";
        jdbcTemplate.update(sql, login);
        LOGGER.info("delete success");
    }

    public void create(String nickname, String password, String email) {
        final String sql = "INSERT INTO user_project(nickname, password, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, nickname, password, email);
        LOGGER.info("insert success");
    }


    public @Nullable UserProfile getUserProfileByEmail(String email) {
        final String sql = "SELECT * FROM user_project WHERE LOWER(email) = LOWER(?)";
        try {
            final UserProfile user = jdbcTemplate.queryForObject(sql, new UserProfileMapper(), email);
            LOGGER.info("getUserByEmail success");
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public @Nullable UserProfile getUserProfileByUsername(String username) {
        try {
            final String sql = "SELECT * FROM user_project WHERE LOWER(nickname) = LOWER(?)";
            final UserProfile user = jdbcTemplate.queryForObject(sql, new UserProfileMapper(), username);
            LOGGER.info("getUserByEmail success");
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateUserProfile(UserProfile userProfile) {
        if (userProfile.getUsername() != null) {
            final String sql = "UPDATE user_project SET lower(nickname) = lower(?) WHERE LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getUsername(), userProfile.getEmail());
        }

        if (userProfile.getPassword() != null) {
            final String sql = "UPDATE user_project SET password = ? WHERE LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getPassword(), userProfile.getEmail());
        }
        LOGGER.info("Updated user success");
    }

    public void updateRating(String username, String key) {
        switch (key) {
            case "WIN": {
                final String sql = "UPDATE user_project SET wins = user_project.wins + 1 WHERE lower(nickname) = lower(?)";
                jdbcTemplate.update(sql, username);
                LOGGER.info("Win set success");
                break;
            }
            case "LOSE": {
                final String sql = "UPDATE user_project SET losses = user_project.losses + 1 WHERE lower(nickname) = lower(?)";
                jdbcTemplate.update(sql, username);
                LOGGER.info("Lose set success");
                break;
            }
            case "DRAW": {
                final String sql = "UPDATE user_project SET draws = user_project.draws + 1 WHERE lower(nickname) = lower(?)";
                jdbcTemplate.update(sql, username);
                LOGGER.info("Draws set success");
                break;
            }
            default:
                break;
        }
    }

    public List<UserProfile> getUsers(int count) {
        final String sql = "SELECT * FROM user_project ORDER BY user_project.wins - user_project.losses desc LIMIT ?";
        final List<UserProfile> users = jdbcTemplate.query(sql, new UserProfileMapper(), count);
        LOGGER.info("getListUsers success");
        return users;
    }

    public int getCount() {
        final String sql = "SELECT COUNT(*) FROM user_project";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class);
        LOGGER.info("getCount success");
        return count;
    }
}
