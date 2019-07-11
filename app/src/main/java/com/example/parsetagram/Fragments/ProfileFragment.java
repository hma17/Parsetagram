package com.example.parsetagram.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parsetagram.LogInActivity;
import com.example.parsetagram.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {


    private Button LogOutbtn;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        LogOutbtn = (Button) view.findViewById(R.id.btnLogOut);
        LogOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logOut();
            }

        });


    }


    private void logOut() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login Successful!");
                    final Intent intent = new Intent(getContext(), LogInActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }

        });
    }


}

