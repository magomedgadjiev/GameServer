package application.user;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magomed on 27.03.17.
 */
@Service
@Component
public class UserProfileJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = Logger.getLogger(UserProfileJDBCTemplate.class);

    @Autowired
    public UserProfileJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable() {
        String query = new StringBuilder()
                .append("CREATE EXTENSION IF NOT EXISTS citext; ")
                .append("CREATE TABLE IF NOT EXISTS user_project ( ")
                .append("nickname varchar(128) UNIQUE NOT NULL , ")
                .append("password varchar(128) NOT NULL, ")
                .append("email CITEXT UNIQUE NOT NULL PRIMARY KEY, ")
                .append("rating INT NOT NULL DEFAULT 0);")
                .toString();
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
        String SQL = "select * from user_project where email = ?";
        UserProfile users = jdbcTemplate.queryForObject(SQL, new UserProfileMapper(), email);
        LOGGER.debug("getUserByEmail success");
        return users;
    }

    public void update(String nickname, String password, String email, Integer rating){
        String SQL = "update user_project set nickname = ?, password = ?, rating = ? where LOWER(email) = LOWER(?)";

        jdbcTemplate.update(SQL, nickname, password, rating, email);
        System.out.println("Updated user" );
    }

    public void updateNickname(String nickname, String email){
        String SQL = "update user_project set nickname = ? where LOWER(email) = LOWER(?)";

        jdbcTemplate.update(SQL, nickname, email);
        System.out.println("Updated user" );
    }

    public void updatePassword(String password, String email){
        String SQL = "update user_project set password = ? where LOWER(email) = LOWER(?)";

        jdbcTemplate.update(SQL, password, email);
        System.out.println("Updated user" );
    }

    public void updateRating(String email, Integer rating){
        String SQL = "update user_project set rating = ? where LOWER(email) = LOWER(?)";

        jdbcTemplate.update(SQL, rating, email);
        System.out.println("Updated user" );
    }

    public List<UserProfile> getUsers(){
        String SQL = "select * from user_project";
        List<UserProfile> users = jdbcTemplate.query(SQL, new UserProfileMapper());
        LOGGER.debug("getListUsers success");
        return users;
    }

    public int getCount() {
        String SQL = "select COUNT(*) from user_project";
        int count = jdbcTemplate.queryForObject(SQL, Integer.class);
        LOGGER.debug("getCount success");
        return count;
    }

    public void delete() {
        String SQL = "delete from user_project";
        jdbcTemplate.update(SQL);
        System.out.println("Deleted success" );
    }
}
