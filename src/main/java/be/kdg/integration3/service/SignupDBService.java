package be.kdg.integration3.service;

import be.kdg.integration3.domain.UserAccount;
import be.kdg.integration3.domain.UserAccount.*;
import be.kdg.integration3.repository.SignupRepository;
import org.springframework.stereotype.Service;

@Service
public class SignupDBService implements SignupService {
    private final SignupRepository repository;

    public SignupDBService(SignupRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isEmailTaken(String email){
        return repository.isEmailTaken(email);
    }

    @Override
    public UserAccount addUserAccount(String email, String username, String passwd, UseCaseType useCase){
        return repository.addUserAccount(new UserAccount(email, username, passwd, useCase));
    }

    @Override
    public UserAccount addUserAccount(String email, String username, String passwd, UseCaseType useCase, int orgId){
        return repository.addUserAccount(new UserAccount(email, username, passwd, useCase, orgId));
    }

    @Override
    public boolean correctUserDetails(String email, String password){
        return repository.loginDetailsCorrect(email, password);
    }
}
