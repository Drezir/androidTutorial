 package app.android.adam.androidapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.android.adam.androidapp.contact.Contact;

 public class ContactActivity extends AppCompatActivity {

    private ListView contactsView;
    private List<Contact> contacts;

    private static final int CONTACTS_READ_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contactsView = findViewById(R.id.contactListView);

        checkContactsPermissions();
    }

     private void checkContactsPermissions() {
         if (Build.VERSION.SDK_INT >= 23) {
             if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                     PackageManager.PERMISSION_GRANTED) {
                 requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
                         CONTACTS_READ_PERMISSIONS);
                 return;
             }
         }
         readContacts();
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         switch (requestCode) {
             case CONTACTS_READ_PERMISSIONS:
                 if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     readContacts();
                 }
                 break;
             default:
                 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         }
     }

    private void readContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);

        contacts = new ArrayList<>();
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(new Contact(name, phone));
        }

        contactsView.setAdapter(new ContactsAdapter(contacts));
    }

    private class ContactsAdapter extends BaseAdapter {

         private final List<Contact> contactsList;

         private ContactsAdapter(List<Contact> contactsList) {
             this.contactsList = contactsList;
         }


         @Override
         public int getCount() {
             return contactsList.size();
         }

         @Override
         public Object getItem(int position) {
             return contactsList.get(position);
         }

         @Override
         public long getItemId(int position) {
             return 0;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             if (convertView == null) {
                 LayoutInflater layoutInflater = getLayoutInflater();
                 convertView = layoutInflater.inflate(R.layout.contacts_item, null);
                 convertView.setTag(new ViewHolder(
                         convertView.findViewById(R.id.contactName),
                         convertView.findViewById(R.id.contactPhone)
                 ));
             }
             ViewHolder viewHolder = (ViewHolder) convertView.getTag();
             Contact actualContact = contactsList.get(position);
             viewHolder.name.setText(actualContact.getName());
             viewHolder.phone.setText(actualContact.getNumber());
             return convertView;
         }

         private class ViewHolder {
             final TextView name, phone;

             private ViewHolder(TextView name, TextView phone) {
                 this.name = name;
                 this.phone = phone;
             }
         }
     }
}
