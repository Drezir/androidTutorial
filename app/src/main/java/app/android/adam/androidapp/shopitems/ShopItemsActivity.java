package app.android.adam.androidapp.shopitems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.android.adam.androidapp.AuthenticatedActivity;
import app.android.adam.androidapp.R;

public class ShopItemsActivity extends AuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_items);

        List<ShopItem> shopItems = new ArrayList<ShopItem>(5);
        shopItems.add(new ShopItem("Bananas", false));
        shopItems.add(new ShopItem("Oranges", false));
        shopItems.add(new ShopItem("Peaches", false));
        shopItems.add(new ShopItem("Carrots", false));
        shopItems.add(new ShopItem("Cucumbers", false));
        shopItems.add(new ShopItem("Melons", false));
        shopItems.add(new ShopItem("Lemons", false));
        shopItems.add(new ShopItem("Tomatoes", false));
        shopItems.add(new ShopItem("Potatoes", false));
        shopItems.add(new ShopItem("Onions", false));
        shopItems.add(new ShopItem("Garlics", false));
        shopItems.add(new ShopItem("Salad", false));
        shopItems.add(new ShopItem("Apples", false));
        shopItems.add(new ShopItem("Cherries", false));
        shopItems.add(new ShopItem("Blueberries", false));
        shopItems.add(new ShopItem("Pears", false));
        shopItems.add(new ShopItem("Raspberries", false));


        ((ListView)findViewById(R.id.shopitemsListView)).setAdapter(new ShopItemListAdapter(shopItems));
    }

    private class ShopItemListAdapter extends BaseAdapter {

        private List<ShopItem> shopItems;

        public ShopItemListAdapter(List<ShopItem> shopItems) {
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
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.shopitem, null);
                TextView textView = convertView.findViewById(R.id.shopItemText);
                final ToggleButton toggleButton = convertView.findViewById(R.id.shopItemBought);
                ViewHolder viewHolder = new ViewHolder(toggleButton, textView);
                convertView.setTag(viewHolder);
            }
            final ShopItem shopItem = shopItems.get(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.shopItemBought.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopItem.setBought(!shopItem.isBought());
                }
            });
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
    }
}
