package com.project.esgi.shoppinglistesgi;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String tokenLogin;
    public static ArrayList<String> idItemList;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Fragment fragment = new LoginFragment();
       FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
       fragmentTransaction.add(R.id.fragmentLogin, fragment).addToBackStack(null).commit();

    }


}
