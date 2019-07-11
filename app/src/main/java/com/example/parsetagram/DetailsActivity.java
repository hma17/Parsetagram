package com.example.parsetagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.parsetagram.model.Post;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    Post post;

    private TextView tvDetailsHandle;
    private TextView tvDetailsDescription;
    private ImageView ivDetailsImage;
    private TextView tvTimeStamp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DetailsActivity", "we entered this activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetailsDescription = findViewById(R.id.tvDetailsDescription);
        tvDetailsHandle = findViewById(R.id.tvDetailsHandle);
        ivDetailsImage = findViewById(R.id.ivDetailsImage);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);

        post = Parcels.unwrap(getIntent().getParcelableExtra("Detailed"));
        tvDetailsHandle.setText(post.getUser().getUsername());
        tvDetailsDescription.setText(post.getDescription());
        tvTimeStamp.setText(post.getCreatedAt().toString());
        Glide.with(this).load(post.getImage().getUrl()).into(ivDetailsImage);
    }
}
