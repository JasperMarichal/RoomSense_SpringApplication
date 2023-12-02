package be.kdg.integration3.domain;

public class UserAccount {
    public enum UseCaseType {
        OFFICE, SCHOOL, HOME
    }

    private final String email;
    private final String username;
    private final String passwd;
    private final UseCaseType useCase;
    private int orgId = 0;

    public UserAccount(String email, String username, String passwd, UseCaseType useCase) {
        this.email = email;
        this.username = username;
        this.passwd = passwd;
        this.useCase = useCase;
    }

    public UserAccount(String email, String username, String passwd, UseCaseType useCase, int orgId) {
        this.email = email;
        this.username = username;
        this.passwd = passwd;
        this.useCase = useCase;
        this.orgId = orgId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswd() {
        return passwd;
    }

    public UseCaseType getUseCase() {
        return useCase;
    }

    public int getOrgId() {
        return orgId;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", use_case='" + useCase + '\'' +
                ", org_id=" + orgId +
                '}';
    }
}