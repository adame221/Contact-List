package com.example.aely.aelylab8_1;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends ListActivity implements Serializable, AppInfo {

    private Button mContactButton;
    private TextView mContactsAddedText;
    private TextView mContactsText;
    ObjectArrayAdapter mAdapter;

    private String strContactsAddedText = "";
    private String strContactsText = "";
    private ArrayList<Contact> mContacts;
    private Contact mUpdatedContact;
    private int updatedContactPosition;

    /**
     * This method saves all the information at the time you changed the view of
     * your phone.
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(getResources().getString(R.string.contacts_added), mContactsAddedText
                .getText().toString());
        savedInstanceState.putString(getResources().getString(R.string.contacts), mContactsText
                .getText().toString());
        savedInstanceState.putSerializable(getResources().getString(R.string.contact_array), mContacts);
    }

    /**
     * This method restores all the information at the time you changed the view of
     * your phone.
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mContactsAddedText.setText(savedInstanceState.getString(getResources().getString(R.string
                .contacts_added)));
        mContactsText.setText(savedInstanceState.getString(getResources().getString(R.string
                .contacts)));

        mContacts = (ArrayList<Contact>) savedInstanceState.getSerializable(getResources().getString(R.string.contact_array));
        for (Contact currentContact : mContacts) {
        }

        mAdapter = new ObjectArrayAdapter(MainActivity.this, R.layout.detail_line, mContacts);
        //display the list
        setListAdapter(mAdapter);
    }

    /**
     * This method creates the information when the app is started.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactButton = (Button) findViewById(R.id.contactButton);
        mContactsAddedText = (TextView) findViewById(R.id.contactsAddedText);
        mContactsText = (TextView) findViewById(R.id.contactsText);

        mContactsText.setText(getResources().getString(R.string.no_contacts));
        mContacts = new ArrayList<>();

        mContactsAddedText.setText(getResources().getString(R.string.contacts_added_label_start));

        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivityForResult(intent, 0);   //expect to get data back from called activity
            }
        });
    }

    /**
     * This method retrieves the data from the sub activity.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {
            mContactsText.setText(null);

            mUpdatedContact = (Contact) data.getSerializableExtra(EXTRA_NEW_CONTACT);
            updatedContactPosition = getIntent().getIntExtra(EXTRA_UPDATED_CONTACT, 0);

            // retrieve the data from Activity called
            if (data == null) {
                return;
            }
            if(mUpdatedContact != null) {
                mContacts.remove(updatedContactPosition);
                mContacts.add(mUpdatedContact);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.contact_updated),
                        Toast.LENGTH_SHORT).show();
                switch (mContacts.size() - 1) {
                }
                mAdapter.notifyDataSetChanged();
            } else {
                mContacts.add((Contact) data.getSerializableExtra(EXTRA_CONTACT));

                for (Contact currentContact : mContacts) {
                }
            }
            mContactsAddedText.setText(getResources().getString(R.string.contacts_added_label) +
                    mContacts.size
                            ());

            strContactsAddedText = mContactsAddedText.getText().toString();
            strContactsText = mContactsText.getText().toString();
        }
        mAdapter = new ObjectArrayAdapter(MainActivity.this, R.layout.detail_line, mContacts);
        //display the list
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);

        final Contact item = (Contact) getListAdapter().getItem(position);
        /*
          If the user selects an item, delete it and refresh the adapter
         */
        Bundle bundle = new Bundle();
        Intent itemIntent = new Intent(MainActivity.this, ContactActivity.class);
        bundle.putSerializable(EXTRA_ITEM_ARRAY, item);
        bundle.putInt(CLICKED_POSITION, position);
        itemIntent.putExtras(bundle);
        startActivityForResult(itemIntent, 1);   //expect to get data back from called activity
    }

    public class ObjectArrayAdapter extends ArrayAdapter<Contact> {

        //declare ArrayList of item
        private ArrayList<Contact> contacts;

        /**
         *  Override the constructor for ArrayAdapter
         *  The only variable we care about now ArrayList<PlatformVersion> objects
         *  it is the list of the objects we want to display
         *
         * @param context
         * @param resource
         * @param contacts
         */
        public ObjectArrayAdapter(Context context, int resource, ArrayList<Contact> contacts) {
            super(context, resource, contacts);
            this.contacts = contacts;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // assign the view we are converting to a local variable
            View view = convertView;

            /*
              Check to see if view null.  If so, we have to inflate the view
              "inflate" basically mean to render or show the view
             */

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.detail_line, null);
            }

            /*

              Recall that the variable position is sent in as an argument to this method
              The variable simply refers to the position of the current object on the list.
              The ArrayAdapter iterate through the list we sent it

              versionObject refers to the current PlatformVersion Object

             */
            Contact contactObject = contacts.get(position);

            if (contactObject != null) {
                // obtain a reference to the widgets in the defined layout "wire up the widgets from detail_line"
                TextView mLastName = (TextView) view.findViewById(R.id.lastName);
                TextView mFirstName = (TextView) view.findViewById(R.id.firstName);
                TextView mComma = (TextView) view.findViewById(R.id.comma);
                TextView mEmail = (TextView) view.findViewById(R.id.email);
                TextView mPhone = (TextView) view.findViewById(R.id.phone);

                if (mLastName != null) {
                    mLastName.setText(contactObject.getLastName());
                }
                if (mFirstName != null) {
                    mFirstName.setText(contactObject.getFirstName());
                }
                if (mEmail != null) {
                    mEmail.setText(contactObject.getEmail());
                }

                if (mPhone != null) {
                    mPhone.setText(contactObject.getPhoneNumber());
                }
            }

            // the view must be returned to our Activity
            return view;
        }
    }
}
