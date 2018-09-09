package app.android.adam.androidapp.shopitems;


public class ShopItem {

    private String name;
    private boolean bought;
    private long id;

    public ShopItem(String name, boolean bought) {
        this.name = name;
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    @Override
    public String toString() {
        return name;
    }
}
