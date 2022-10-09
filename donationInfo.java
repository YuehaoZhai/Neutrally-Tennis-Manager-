package com.example.neutrallytennis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class donationInfo extends AppCompatActivity {
    private EditText emailAddress;
    private TextView result;
    private Button assign, GoToView;
    private String newDonationEmail;
    int number = 0;

    //List<String> usedPasswordList = new ArrayList<>();
    JSONArray donationUser = new JSONArray();
    JSONObject root = new JSONObject();
    JSONObject pwRoot = new JSONObject();

    //private static final String FILE_NAME = "text.json";
    private static final String FILE_NAME = "db.json";
    private static final String PASSWORD_FILE = "RandomPassword.json";

    private int track_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_info);

        assign = findViewById(R.id.assign);
        emailAddress = findViewById(R.id.fill_user_name);
        result = findViewById(R.id.dispayResult);

        GoToView = findViewById(R.id.ToDonaList);

        //-------initialize donation dataset (random password file - stored in "assets")
        String jsonString = getJsonFromAssets(getApplicationContext(),"donationList.json");

        try {
            JSONObject obj = new JSONObject(jsonString);

            String rowUsed = obj.getString("rowTrack");
            root.put("total",rowUsed);
            //store used user info
            JSONArray userList = obj.getJSONArray("users");
            //number of users donated
            number = Integer.parseInt(rowUsed);

            //create initial file user who donated with
            for(int i = 0; i<number; i++){
                //current user
                JSONObject currentDonation = userList.getJSONObject(i);
                //password
                String pw = currentDonation.getString("password");
                String mail = currentDonation.getString("email");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("User",mail);
                jsonObject.put("Password",pw);
                donationUser.put(jsonObject);
            }
            root.put("DonationList",donationUser);
            //-------end of initializing FILE_NAME (store donation info)

            //-------get a list of random password and store them in the PASSWORD_FILE
            List<String> ls = new ArrayList<>();
            JSONArray passwordList = new JSONArray();
            for(int i = 0; i<999; i++){
                JSONObject currentObject = userList.getJSONObject(i);
                String pw = currentObject.getString("password");
                ls.add(pw);

                //store each password
                JSONObject ipw = new JSONObject();
                ipw.put("password",pw);
                passwordList.put(ipw);
            }

            //put JSONARRAY into json root
            pwRoot.put("list",passwordList);
            //String passwordRecord = pwRoot.toString();
            String passArrString = pwRoot.toString();
            //JSONArray check = pwRoot.getJSONArray("list");
            //String passArrString = check.toString();

            try {
                FileOutputStream fos = null;
                fos = openFileOutput(PASSWORD_FILE,MODE_PRIVATE);
                fos.write(passArrString.getBytes());
                //Toast.makeText(donationInfo.this, "ALREADY SAVED TO "+ getFilesDir(), Toast.LENGTH_SHORT).show();


                File dir = getFilesDir();

                File file = new File(dir, FILE_NAME);
                if(!file.exists()){
                    FileOutputStream fos2 = null;
                    fos2 = openFileOutput(FILE_NAME,MODE_PRIVATE);
                    fos2.write(root.toString().getBytes());
                }

                //Toast.makeText(donationInfo.this, "ALREADY SAVED TO "+ getFilesDir(), Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            //---------end of initializing PASSWORD_FILE

            //result.setText(root.toString());



            //"Assign" button: assign new user with password and update the FILE_NAME
            assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String inputEmail = emailAddress.getText().toString();
                    result.setText(inputEmail);

                    //Get index(=current number of users)
                    int i = loadNumberOfUser();
                    //Get password with row_number(i) from PASSWORD_FILE
                    i = i+1;
                    String assignedPW = loadPassword(i-1);
                    result.setText(assignedPW); //works here
                    String display = "Assign User: "+inputEmail+" with Password ( "+assignedPW+" ) !";
                    result.setText(display);

                    //displayJson();

                    //update UserList ()
                    updateDonationRecord(i, inputEmail, assignedPW);
                    //displayJson();
                    Toast.makeText(donationInfo.this, "ALREADY SAVED TO "+ getFilesDir(), Toast.LENGTH_SHORT).show();
                }
            });


            //click "VIEW USER LIST" and switch to DonationDataset activity to view all users with their assigned password
            GoToView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),DonationDataset.class);
                    startActivity(intent);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    //----debug

    public void updateDonationRecord(int row, String email, String password){
        FileInputStream inputStream = null;

        try{
            inputStream = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text=br.readLine())!=null){
                sb.append(text);
            }

            String jsString = sb.toString();
            //reader - new root for FILE_NAME
            JSONObject reader = new JSONObject(jsString);

            //update jsonobject - numberOfUser
            reader.put("total",row);


            //get JSONARRAY and update JSONARRAY
            JSONArray ja = reader.getJSONArray("DonationList");
            int index = row -1;
            //?????maybe need to store each item in the json array
            JSONObject newUser = new JSONObject();
            newUser.put("User",email);
            newUser.put("Password",password);
            ja.put(newUser);
            reader.put("DonationList",ja);

            String input = reader.toString();

            String display = "Assign User: "+email+" with Password ( "+password+" ) !";
            result.setText(display);
            //result.setText(input);

            //convert to jsonstring and write in file
            FileOutputStream outputStream = openFileOutput(FILE_NAME,MODE_PRIVATE);
            outputStream.write(input.getBytes());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int loadNumberOfUser() {
        int number  = 0;
        FileInputStream inputStream = null;

        try{
            inputStream = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text=br.readLine())!=null){
                sb.append(text);
            }

            String jsString = sb.toString();
            //result.setText(jsString);
            JSONObject reader = new JSONObject(jsString);
            String n = reader.getString("total");
            number = Integer.parseInt(n);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return number;
    }

    //get password from PASSWORD_FILE
    public String loadPassword(int index) {
        String returnPassword = "";
        FileInputStream inputStream = null;
        try{
            inputStream = openFileInput(PASSWORD_FILE);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text=br.readLine())!=null){
                sb.append(text);
            }

            String jsString = sb.toString();
            JSONObject reader = new JSONObject(jsString);
            JSONArray jsonArray = reader.getJSONArray("list");
            JSONObject iterator = jsonArray.getJSONObject(index);
            returnPassword = iterator.getString("password");
            //String test = iterator.getString("password");
            //result.setText(test);

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        String output = "";
        FileInputStream inputStream = null;

        try{
            inputStream = openFileInput(PASSWORD_FILE);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text=br.readLine())!=null){
                sb.append(text);
            }

            String jsString = sb.toString();
            result.setText(jsString);
            JSONObject reader = new JSONObject(jsString);
            JSONArray ls = reader.getJSONArray("list");
            JSONObject current = ls.getJSONObject(row);

            result = current.getString("password");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return output;

         */
        return returnPassword;
    }



    //display user list with corresponding password
    public void displayJson() {
        FileInputStream inputStream = null;
        try{
            inputStream = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text=br.readLine())!=null){
                sb.append(text);
            }

            String jsString = sb.toString();
            result.setText(jsString);

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //read initial dataset(random password file from folder assets)
    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

}