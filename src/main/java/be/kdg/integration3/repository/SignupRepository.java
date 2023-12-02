package be.kdg.integration3.repository;

import be.kdg.integration3.domain.UserAccount;

public interface SignupRepository {
    boolean isEmailTaken(String email);

    UserAccount addUserAccount(UserAccount userAccount);

    boolean loginDetailsCorrect(String email, String password);
}
