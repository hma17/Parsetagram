package com.example.parsetagram.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parsetagram.LogInActivity;
import com.example.parsetagram.R;
import com.example.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {


    private Button LogOutbtn;
    private final static String TAG = "ProfileFragment";



    private ImageView ivProfilePhoto;
    private TextView tvPostCount;
    private TextView tvName;
    private Button SetProfilePic;



    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    private RecyclerView rvPosts;
    private com.example.parsetagram.Adapters.ProfileAdapter adapter;
    private List<Post> mPosts;

    ParseObject user;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogOutbtn = (Button) view.findViewById(R.id.btnLogOut);
        LogOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logOut();
            }

        });

        ivProfilePhoto = view.findViewById(R.id.ivProfilePic);
        tvPostCount = view.findViewById(R.id.tvPostCount);
        tvName = view.findViewById(R.id.tvName);
        SetProfilePic = view.findViewById(R.id.btnSetProfilePic);



        rvPosts = view.findViewById(R.id.rvPosts);
        mPosts = new ArrayList<>();

        adapter = new com.example.parsetagram.Adapters.ProfileAdapter(getContext(), mPosts);
        rvPosts.setAdapter(adapter);

        setUserInfo();
        getPosts();

        ParseUser user = ParseUser.getCurrentUser();

        if (user.get("profileImage") != null) {
            ParseFile img = (ParseFile) user.get("profileImage");
            Glide.with(getContext()).load(img.getUrl()).into(ivProfilePhoto);
        }
        SetProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
                if (photoFile == null) {
                    Log.e(TAG, "No photo to submit");
                    Toast.makeText(getContext(), "There is no photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Glide.with(getContext()).load(photoFile).into(ivProfilePhoto);
                saveUser(photoFile);
            }
        });
    }

    public void saveUser (File photoFile) {
        ParseFile photo = new ParseFile(photoFile);
        ParseUser user = ParseUser.getCurrentUser();
        user.put("ProfileImage", photo);
    }


    private void setUserInfo() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
        userQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    user = object;


                    ParseFile profileImage = object.getParseFile("profileImage");
                    if (profileImage != null) {
                        Glide.with(getContext())
                                .load(profileImage.getUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivProfilePhoto);
                    }
                } else {
                    Log.e(TAG, "Error finding user.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        postsQuery.orderByDescending("createdAt");

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();

                    tvPostCount.setText(String.valueOf(objects.size()));


                    mPosts.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error with posts count.");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error with post count", Toast.LENGTH_SHORT).show();
                }

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

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);
        // wrap File object into a content provider
        // required for API >= 24
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

}

