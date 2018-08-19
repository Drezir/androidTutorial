package app.android.adam.androidapp.user;

public class Credentials {

    private final String username, password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Credentials) {
            final Credentials credentials = (Credentials)obj;
            return username.equals(credentials.username) && password.equals(credentials.password);
        }
        return false;
    }
}
