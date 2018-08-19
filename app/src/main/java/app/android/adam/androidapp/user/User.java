package app.android.adam.androidapp.user;

public enum User {
    INSTANCE;

    private final Credentials credentials;
    private boolean loggedIn;

    User() {
        this.credentials = new Credentials("admin", "admin");
    }

    public boolean login(final Credentials credentials) {
        return loggedIn || (loggedIn = this.credentials.equals(credentials));
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }
}
