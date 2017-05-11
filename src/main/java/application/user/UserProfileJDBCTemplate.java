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
        String query = "DROP TABLE IF EXISTS user_project";
        jdbcTemplate.execute(query);
        LOGGER.debug("drop success");
    }

    public void create(String nickname, String password, String email) {
        String SQL = "insert into user_project(nickname, password, email, rating) values (?, ?, ?, ?)";
        jdbcTemplate.update(SQL, nickname, password, email, 0);
        LOGGER.debug("insert success");
    }

    public UserProfile getUserProfile(String email) {
        String SQL = "select * from user_project where LOWER(email) = LOWER(?)";
        UserProfile users = jdbcTemplate.queryForObject(SQL, new UserProfileMapper(), email);
        LOGGER.debug("getUserByEmail success");
        return users;
    }

    public void updateUserProfile(UserProfile userProfile){
        if (userProfile.getUsername() != null){
            final String sql = "update user_project set nickname = ? where LOWER(email) = LOWER(?)";
            jdbcTemplate.update(sql, userProfile.getUsername(), userProfile.getEmail());
        }

        if (userProfile.getPassword() != null){
            final String SQL = "update user_project set password = ? where LOWER(email) = LOWER(?)";
            jdbcTemplate.update(SQL, userProfile.getPassword(), userProfile.getEmail());
        }

        if (userProfile.getRating() != 0){
            final String SQL = "update user_project set rating = ? where LOWER(email) = LOWER(?)";
            jdbcTemplate.update(SQL, userProfile.getRating(), userProfile.getEmail());
        }

        LOGGER.debug("Updated user success" );
    }

    public List<UserProfile> getUsers(){
        final String SQL = "select * from user_project ORDER BY rating DESC";
        List<UserProfile> users = jdbcTemplate.query(SQL, new UserProfileMapper());
        LOGGER.debug("getListUsers success");
        return users;
    }

    public int getCount() {
        final String SQL = "select COUNT(*) from user_project";
        final int count = jdbcTemplate.queryForObject(SQL, Integer.class);
        LOGGER.debug("getCount success");
        return count;
    }

    public void delete() {
        final String SQL = "delete from user_project";
        jdbcTemplate.update(SQL);
        System.out.println("Deleted success" );
    }
}
