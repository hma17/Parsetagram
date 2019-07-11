package com.example.parsetagram.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parsetagram.R;
import com.example.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class TimelineFragment extends Fragment {

    private final static String TAG = "TimelineFragment";

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private List<Post> mPosts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvItems);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        mPosts = new ArrayList<>();

        adapter = new PostsAdapter(getContext(), mPosts);
        rvPosts.setAdapter(adapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTopPosts();

        setupPullToRefresh();
    }

    private void setupPullToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopPosts();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.orderByDescending("createdAt");
        postsQuery.setLimit(20);

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();

                    mPosts.addAll(objects);
                    adapter.notifyDataSetChanged();

                    for (int i = 0; i < objects.size(); i++) {
                        Log.d(TAG, "Post [ " + i + " ] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                    }
                } else {
                    Log.e(TAG, "Error getting posts.");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error getting posts", Toast.LENGTH_SHORT).show();
                }

                swipeContainer.setRefreshing(false);
            }
        });
    }
}