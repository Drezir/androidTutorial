package app.android.adam.androidapp.shopitems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import app.android.adam.androidapp.AuthenticatedActivity;
import app.android.adam.androidapp.R;
import app.android.adam.androidapp.database.Repository;

public class ShopItemsActivity extends AuthenticatedActivity {

    private Repository repository;
    private ShopItemListAdapter shopItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_items);

        repository = new Repository(this);
        List<ShopItem> shopItems = repository.getAllItems();

        findViewById(R.id.shopItemsSaveData).setOnClickListener(new ButtonSaveDataListener(shopItems));
        findViewById(R.id.shopItemsDeleteData).setOnClickListener(new ButtonClearDataListener(shopItems));

        shopItemListAdapter = new ShopItemListAdapter(shopItems);
        ((ListView)findViewById(R.id.shopitemsListView)).setAdapter(shopItemListAdapter);
    }

    private class ShopItemListAdapter extends BaseAdapter {

        private List<ShopItem> shopItems;

        private ShopItemListAdapter(List<ShopItem> shopItems) {
            this.shopItems = shopItems;
        }

        @Override
        public int getCount() {
            return shopItems.size();
        }

        @Override
        public Object getItem(int position) {
            return shopItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return shopItems.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ShopItem shopItem = shopItems.get(position);
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.shopitem, null);
                TextView textView = convertView.findViewById(R.id.shopItemText);
                ToggleButton toggleButton = convertView.findViewById(R.id.shopItemBought);

                ViewHolder viewHolder = new ViewHolder(toggleButton, textView);

                convertView.setTag(viewHolder);
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            // click listener must be set always no matter of convertView is or is not null
            // otherwise recycle view behaves strangely to checked states of toggle buttons
            viewHolder.shopItemBought.setOnClickListener(new ToggleBoughtListener(shopItem));
            viewHolder.shopItemBought.setChecked(shopItem.isBought());
            viewHolder.shopItemName.setText(shopItem.getName());
            return convertView;
        }

        private class ViewHolder {
            ToggleButton shopItemBought;
            TextView shopItemName;

            ViewHolder(ToggleButton shopItemBought, TextView shopItemName) {
                this.shopItemBought = shopItemBought;
                this.shopItemName = shopItemName;
            }
        }
        private class ToggleBoughtListener implements View.OnClickListener {

            final ShopItem shopItem;

            private ToggleBoughtListener(ShopItem shopItem) {
                this.shopItem = shopItem;
            }

            @Override
            public void onClick(View v) {
                shopItem.setBought(!shopItem.isBought());
                repository.update(shopItem);
            }
        }
    }

    private class ButtonSaveDataListener implements View.OnClickListener {

        private final List<ShopItem> shopItems;

        private ButtonSaveDataListener(List<ShopItem> shopItems) {
            this.shopItems = shopItems;
        }

        @Override
        public void onClick(View v) {
            repository.insertData(new ShopItem("Bananas", false));
            repository.insertData(new ShopItem("Oranges", false));
            repository.insertData(new ShopItem("Peaches", false));
            repository.insertData(new ShopItem("Carrots", false));
            repository.insertData(new ShopItem("Cucumbers", false));
            repository.insertData(new ShopItem("Melons", false));
            repository.insertData(new ShopItem("Lemons", false));
            repository.insertData(new ShopItem("Tomatoes", false));
            repository.insertData(new ShopItem("Potatoes", false));
            repository.insertData(new ShopItem("Onions", false));
            repository.insertData(new ShopItem("Garlics", false));
            repository.insertData(new ShopItem("Salad", false));
            repository.insertData(new ShopItem("Apples", false));
            repository.insertData(new ShopItem("Cherries", false));
            repository.insertData(new ShopItem("Blueberries", false));
            repository.insertData(new ShopItem("Pears", false));
            repository.insertData(new ShopItem("Raspberries", false));

            shopItems.addAll(repository.getAllItems());
            shopItemListAdapter.notifyDataSetChanged();
        }
    }
    private class ButtonClearDataListener implements View.OnClickListener {

        private final List<ShopItem> shopItems;

        private ButtonClearDataListener(List<ShopItem> shopItems) {
            this.shopItems = shopItems;
        }

        @Override
        public void onClick(View v) {
            shopItems.forEach(repository::delete);
            shopItems.clear();
            shopItemListAdapter.notifyDataSetChanged();
        }
    }

}
