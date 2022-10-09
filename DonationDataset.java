package com.example.neutrallytennis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DonationDataset extends AppCompatActivity {
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<User> userList = new ArrayList<>();

    private static final String FILE_NAME = "db.json";
    private String jsString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_dataset);

        //get list of users from FILE_NAME
        //userList.add(new User("louise@gamil.com","12345"));
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

            jsString = sb.toString();
            //Toast.makeText(DonationDataset.this, jsString, Toast.LENGTH_LONG).show();

            JSONObject reader = new JSONObject(jsString);
            String n = reader.getString("total");
            int rowNo = Integer.parseInt(n);


            JSONArray ja = reader.getJSONArray("DonationList");

            for(int i = 0; i<rowNo; i++){
                JSONObject currentUser = ja.getJSONObject(i);
                String m = currentUser.getString("User");
                String p = currentUser.getString("Password");
                userList.add(new User(m,p));
            }


        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mRecyclerview = findViewById(R.id.donaList);

        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(userList);

        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);



    }

    private void getUserInfo(){
        //get list of users from FILE_NAME
        //userList.add(new User("louise@gamil.com","12345"));
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

            jsString = sb.toString();
            Toast.makeText(DonationDataset.this, jsString, Toast.LENGTH_LONG).show();
            /*
            JSONObject root = new JSONObject(jsString);
            JSONArray ls = root.getJSONArray("DonationList");

            String test = "";
            JSONObject row = root.getJSONObject("total");
            test = row.getString("total");
            Toast.makeText(DonationDataset.this, jsString, Toast.LENGTH_LONG).show();


            int rowNo = Integer.getInteger(row.getString("total"));


            for(int i = 0; i<rowNo-1; i++){
                JSONObject currentUser = ls.getJSONObject(i);
                String m = currentUser.getString("email");
                String p = currentUser.getString("password");
                test = test + m;
                userList.add(new User(m,p));
            }
            //Toast.makeText(DonationDataset.this, test, Toast.LENGTH_LONG).show();


             */

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*catch (JSONException e) {
            e.printStackTrace();
        }

         */

    }



}