package com.vishal.intentsimpletest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImplicitIntentTestActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG;
    private final static int REQUEST_CODE_PICK_PHONE_CONTACT = 1;
    private final static int REQUEST_CODE_PICK_CONTACT = 2;
    private final static int REQUEST_CODE_IMAGE_CAPTURE = 3;
    private final static int REQUEST_CODE_PICK_IMAGE = 4;
    EditText textField;
    Button openDialerBtn, openURLBtn, openMapBtn, openPhoneContactBtn, openContactBtn, openGalleryBtn, openCameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit_intent_test);

        TAG  = getResources().getString(R.string.app_log_tag);

        textField = (EditText) findViewById(R.id.textField);

        openDialerBtn = (Button) findViewById(R.id.openDialer);
        openURLBtn = (Button) findViewById(R.id.openURL);
        openMapBtn = (Button) findViewById(R.id.openMap);
        openContactBtn = (Button) findViewById(R.id.openContact);
        openPhoneContactBtn = (Button) findViewById(R.id.openPhoneContacts);
        openCameraBtn = (Button) findViewById(R.id.openCamera);
        openGalleryBtn = (Button) findViewById(R.id.openGallery);

        openDialerBtn.setOnClickListener(this);
        openURLBtn.setOnClickListener(this);
        openMapBtn.setOnClickListener(this);
        openContactBtn.setOnClickListener(this);
        openCameraBtn.setOnClickListener(this);
        openGalleryBtn.setOnClickListener(this);

    }

    private void printContactInfo(Intent intent) {

        Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ImplicitIntentTestActivity.REQUEST_CODE_PICK_PHONE_CONTACT) {
            if(resultCode == RESULT_OK) {
                Log.v(TAG, "Got Result for select-phone-contact request .. !!");

                // Get the URI and query the content provider for the phone number
                Uri contactUri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver().query(contactUri, projection,
                        null, null, null);

                // If the cursor returned is valid, get the phone number
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(numberIndex);
                    Log.v(TAG, "Selected Phone number is : " + number);
                    Toast.makeText(this,"Selected Phone number is : " + number, Toast.LENGTH_SHORT);

                }
            } else {
                Log.v(TAG, "Got RESULT_CANCEL for select-phone-contact request !!!");
            }
        } else if(requestCode == ImplicitIntentTestActivity.REQUEST_CODE_PICK_CONTACT) {
            if(resultCode == RESULT_OK) {

                Log.v(TAG, "Got Result for select-contact request .. !!");
                printContactInfo(data);


            } else {
                Log.v(TAG, "Got RESULT_CANCEL for select-contact request !!!");
            }

        } else if(requestCode == ImplicitIntentTestActivity.REQUEST_CODE_IMAGE_CAPTURE) {

            if(resultCode == RESULT_OK) {

                Log.v(TAG, "Got Captured Image  !!");

                Bitmap thumbnail = data.getParcelableExtra("data");
                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(thumbnail);

            } else {
                Log.v(TAG, "Didn't get any captured image .... !!!");
            }
        } else if(requestCode == ImplicitIntentTestActivity.REQUEST_CODE_PICK_IMAGE) {

            InputStream stream = null;

            if(resultCode == RESULT_OK) {
                Log.v(TAG, "Got an image from gallery !!");
                try {
                    stream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmapx = BitmapFactory.decodeStream(stream);
                    stream.close();

                    ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmapx);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Log.v(TAG, "Didn't get any image from gallery.... !!!");

            }
        }
    }

    @Override
    public void onClick(View view) {

        String text = textField.getText().toString().trim();
        String errorMsg = "";

        switch (view.getId()) {

            case R.id.openDialer:
                if(!text.isEmpty())  {
                    text = text.replaceAll("[^0-9+]","");
                    if(text.matches("[0-9]+") || text.matches("\\+[0-9]+")) {
                        Intent i1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text));
                        startActivity(i1);
                    }
                    else {
                        errorMsg = "Specify a proper phone number to dial";
                    }
                }
                else {
                    errorMsg = "Specify a phone number to dial";
                }
                break;
            case R.id.openURL:
                //Open Browser, Intent.ACTION_VIEW and parameter using Uri.parse(url)
                if(!text.isEmpty() && text.startsWith("http"))  {
                    Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                    startActivity(i1);
                }
                else {
                    errorMsg = "Specify a valid http or https URL";
                }
                break;
            case R.id.openMap:
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + text));
                startActivity(i3);
                break;
            case R.id.openContact:
                //TODO: Yet to implement.
                break;
            case R.id.openPhoneContacts:
                Intent i5 = new Intent(Intent.ACTION_PICK);
                i5.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i5, ImplicitIntentTestActivity.REQUEST_CODE_PICK_PHONE_CONTACT);
                break;
            case R.id.openCamera:
                Intent i6 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i6, ImplicitIntentTestActivity.REQUEST_CODE_IMAGE_CAPTURE);
                break;
            case R.id.openGallery:
                Intent i7 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i7, ImplicitIntentTestActivity.REQUEST_CODE_PICK_IMAGE);
                break;
        }

        if(!errorMsg.isEmpty()) {
            Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show();
        }
    }
}
