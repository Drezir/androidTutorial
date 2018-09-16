package app.android.adam.androidapp.contact;

public class Contact {

    private final String name;
    private final String number;

    public Contact(String name, String contact) {
        this.name = name;
        this.number = contact;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
