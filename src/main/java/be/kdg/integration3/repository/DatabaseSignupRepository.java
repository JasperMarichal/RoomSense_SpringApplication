package be.kdg.integration3.repository;

import be.kdg.integration3.domain.UserAccount;
import be.kdg.integration3.util.exception.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseSignupRepository implements SignupRepository {
    JdbcTemplate jdbcTemplate;

    public DatabaseSignupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isEmailTaken(String email){
        try {
            int emails = jdbcTemplate.queryForObject("SELECT COUNT(email) FROM user_account WHERE email = ?",
                    (rs, rowNum) -> rs.getInt(1), email);

            return emails >= 1;
        } catch (DataAccessException e){
            throw new DatabaseException("Can not connect to database", e);
        } catch (NullPointerException e){
            return true;
        }
    }

    @Override
    public UserAccount addUserAccount(UserAccount userAccount){
        try {
            if (userAccount.getOrgId() != 0) {
                jdbcTemplate.update("INSERT INTO user_account (email, username, passwd, use_case, org_id) " +
                        "VALUES (?, ?, ?, CAST(? AS use_case_t), ?)", userAccount.getEmail(), userAccount.getUsername(),
                        userAccount.getPasswd(), userAccount.getUseCase().toString().toLowerCase(), userAccount.getOrgId());
            } else {
                jdbcTemplate.update("INSERT INTO user_account (email, username, passwd, use_case) " +
                                "VALUES (?, ?, ?, CAST(? AS use_case_t))", userAccount.getEmail(), userAccount.getUsername(),
                        userAccount.getPasswd(), userAccount.getUseCase().toString().toLowerCase());
            }
            return userAccount;
        } catch (DataAccessException e){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    @Override
    public boolean loginDetailsCorrect(String email, String password){
        try {
            int matchedUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_account WHERE email = ? AND passwd = ?",
                    (rs, row) -> rs.getInt(1), email, password);

            return matchedUsers >= 1;
        } catch (DataAccessException e){
            throw new DatabaseException("Can not connect to database", e);
        } catch (NullPointerException e){
            return false;
        }
    }
}
