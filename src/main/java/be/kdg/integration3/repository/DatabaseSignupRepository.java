package be.kdg.integration3.repository;

import be.kdg.integration3.domain.UserAccount;
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
        int emails = jdbcTemplate.queryForObject("SELECT COUNT(email) FROM user_account WHERE email = ?",
                (rs, rowNum) -> rs.getInt(1), email);

        return emails >= 1;
    }

    @Override
    public UserAccount addUserAccount(UserAccount userAccount){
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
    }
}
