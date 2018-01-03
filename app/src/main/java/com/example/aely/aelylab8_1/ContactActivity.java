package com.example.aely.aelylab8_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by student on 10/22/2015.
 */
public class ContactActivity extends Activity implements Serializable, AppInfo {

    private TextView mFirstNameLabel;
    private EditText mEditFirstName;
    private TextView mLastNameLabel;
    private EditText mEditLastName;
    private TextView mEmailLabel;
    private EditText mEditEmail;
    private TextView mPhoneLabel;
    private EditText mEditPhone;
    private Button mAddButton;
    private Button mCancelButton;
    private String strEditFirstName;
    private String strEditLastName;
    private String strEmail;
    private String strPhone;
    private String mUpdatedFirstName;
    private String mUpdatedLastName;
    private String mUpdatedEmail;
    private String mUpdatedPhone;
    public ArrayList<Contact> mEditContacts;
    Contact newContact;
    private int contactPosition;

    /**
     * This method saves all the information at the time you changed the view of
     * your phone.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(getResources().getString(R.string.edit_first_name), mEditFirstName.getText()
                .toString());
        savedInstanceState.putString(getResources().getString(R.string.edit_last_name),
                mEditLastName.getText().toString());
        savedInstanceState.putString(getResources().getString(R.string.edit_email), mEditEmail.getText()
                .toString());
        savedInstanceState.putString(getResources().getString(R.string.edit_phone), mEditPhone.getText()
                .toString());
    }

    /**
     * This method restores all the information at the time you changed the view of
     * your phone.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mEditFirstName.setText(savedInstanceState.getString(getResources().getString(R.string.edit_first_name)));

        mEditLastName.setText(savedInstanceState.getString(getResources().getString(R.string
                .edit_last_name)));

        mEditEmail.setText(savedInstanceState.getString(getResources().getString(R.string.edit_email)));

        mEditPhone.setText(savedInstanceState.getString(getResources().getString(R.string.edit_phone)));
    }

    /**
     * This method creates information when the app is loaded.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view to the second activities layout
        setContentView(R.layout.contact_activity);

        newContact = (Contact) getIntent().getSerializableExtra(EXTRA_ITEM_ARRAY);
        contactPosition = getIntent().getIntExtra(CLICKED_POSITION, 0);

        //Wire up the widgets
        mFirstNameLabel = (TextView) findViewById(R.id.firstNameLabel);
        mEditFirstName = (EditText) findViewById(R.id.editFirstName);
        mLastNameLabel = (TextView) findViewById(R.id.lastNameLabel);
        mEditLastName = (EditText) findViewById(R.id.editLastName);
        mEmailLabel = (TextView) findViewById(R.id.emailLabel);
        mEditEmail = (EditText) findViewById(R.id.editEmail);
        mPhoneLabel = (TextView) findViewById(R.id.phoneLabel);
        mEditPhone = (EditText) findViewById(R.id.editPhone);
        mAddButton = (Button) findViewById(R.id.addButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);

        if (newContact != null) {

            mEditFirstName.setText(newContact.getFirstName());
            mEditLastName.setText(newContact.getLastName());

            if (newContact.getEmail() != null) {
                mEditEmail.setText(newContact.getEmail());
            }

            if(newContact.getPhoneNumber() != null) {
                mEditPhone.setText(newContact.getPhoneNumber());
            }

            mAddButton.setText("Update");
        }

        /**
         * This method sends no information back to the main activity
         */
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        /**
         * This method adds the entered information to the main activity
         */
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAddButton.getText().equals("Update")) {
                    boolean valid = true;
                    mUpdatedFirstName = mEditFirstName.getText().toString();
                    mUpdatedLastName = mEditLastName.getText().toString();
                    mUpdatedEmail = mEditEmail.getText().toString();
                    mUpdatedPhone = mEditPhone.getText().toString();

                    if (mUpdatedFirstName.equals("")) {
                        mEditFirstName.setError(getResources().getString(R.string.first_name_required));
                        valid = false;
                    }

                    if (mUpdatedLastName.equals("")) {
                        mEditLastName.setError(getResources().getString(R.string.last_name_required));
                        valid = false;
                    }

                    if (valid) {

                        Contact updatedContact = new Contact(mUpdatedFirstName, mUpdatedLastName, mUpdatedEmail, mUpdatedPhone);

                        Bundle contactBundle = new Bundle();
                        Intent contactIntent = new Intent(ContactActivity.this, MainActivity.class);
                        contactBundle.putSerializable(EXTRA_NEW_CONTACT, updatedContact);
                        contactBundle.putInt(EXTRA_UPDATED_CONTACT, contactPosition);
                        contactIntent.putExtras(contactBundle);
                        setResult(RESULT_OK, contactIntent);
                        finish();
                    }
                } else {
                    boolean valid = true;
                    strEditFirstName = mEditFirstName.getText().toString();
                    strEditLastName = mEditLastName.getText().toString();
                    strEmail = mEditEmail.getText().toString();
                    strPhone = mEditPhone.getText().toString();

                    if (strEditFirstName.equals("")) {
                        mEditFirstName.setError(getResources().getString(R.string.first_name_required));
                        valid = false;
                    }

                    if (strEditLastName.equals("")) {
                        mEditLastName.setError(getResources().getString(R.string.last_name_required));
                        valid = false;
                    }

                    if (valid) {

                        Contact contact = new Contact(strEditFirstName, strEditLastName, strEmail, strPhone);
                        Intent data = new Intent();

                        data.putExtra(EXTRA_CONTACT, contact);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
            }
        });
    }
}