package model;

public abstract class User {

    private String name;
    private String username;
    private String password;

    private int failedAttempts;
    private boolean blocked;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void recordFailure() {
        failedAttempts++;

        if (failedAttempts >= 3) {
            blocked = true;
        }
    }

    public void resetAttempts() {
        failedAttempts = 0;
    }

    public void unblock() {
        blocked = false;
        failedAttempts = 0;
    }

    public void restoreSecurity(int attempts, boolean locked) {
        failedAttempts = attempts;
        blocked = locked;
    }

    public abstract String getRole();

    public abstract String toFileLine();
}