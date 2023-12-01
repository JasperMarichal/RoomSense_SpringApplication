package be.kdg.integration3.presentation.viewmodel;

import be.kdg.integration3.domain.UserAccount;
import jakarta.validation.constraints.*;

public class SignupViewModel {


    @NotBlank(message = "Enter an email address!")
    @Email(message = "Invalid input. Please enter a valid email address.")
    private String email;

    @NotNull
    @Size(min = 4, message = "Username must be at least 8 characters long.")
    private String username;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotNull
    private UserAccount.UseCaseType useCase;

    private int orgId;

    public SignupViewModel() {
    }

    public SignupViewModel(String email, String username, String password, UserAccount.UseCaseType useCase) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.useCase = useCase;
    }

    public SignupViewModel(String email, String username, String password, UserAccount.UseCaseType useCase, int orgId) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.useCase = useCase;
        this.orgId = orgId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserAccount.UseCaseType getUseCase() {
        return useCase;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseCase(UserAccount.UseCaseType useCase) {
        this.useCase = useCase;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "SignupViewModel{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", useCase=" + useCase +
                '}';
    }
}
