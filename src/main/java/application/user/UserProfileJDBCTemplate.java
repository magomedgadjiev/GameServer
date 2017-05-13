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
                "rating INT NOT NULL DEFAULT 0);";
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
        final String sql = "insert into user_project(nickname, password, email, rating) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, nickname, password, email, 0);
        LOGGER.debug("insert success");
    }

    public UserProfile getUserProfile(String email) {
        final String sql = "select * from user_project where LOWER(email) = LOWER(?)";
        final UserProfile users = jdbcTemplate.queryForObject(sql, new UserProfileMapper(), email);
        LOGGER.debug("getUserByEmail success");
        return users;
    }

    public void updateUserProfile(UserProfile userProfile){
        if (userProfile.getUsername() != null){
            final String sql = "update user_project set nickname = ? where LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getUsername(), userProfile.getEmail());
        }

        if (userProfile.getPassword() != null){
            final String sql = "update user_project set password = ? where LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getPassword(), userProfile.getEmail());
        }

        LOGGER.debug("Updated user success" );
    }

    public List<UserProfile> getUsers(){
        final String sql = "select * from user_project ORDER BY ";
        final List<UserProfile> users = jdbcTemplate.query(sql, new UserProfileMapper());
        LOGGER.debug("getListUsers success");
        return users;
    }

    public int getCount() {
        final String sql = "select COUNT(*) from user_project";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class);
        LOGGER.debug("getCount success");
        return count;
    }
}
