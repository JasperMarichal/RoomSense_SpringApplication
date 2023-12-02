package be.kdg.integration3.service;

import be.kdg.integration3.domain.UserAccount;
import be.kdg.integration3.domain.UserAccount.*;

public interface SignupService {
    boolean isEmailTaken(String email);

    UserAccount addUserAccount(String email, String username, String passwd, UseCaseType useCase);

    UserAccount addUserAccount(String email, String username, String passwd, UseCaseType useCase, int orgId);
}
