package com.example.dessert_order_app.uiFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dessert_order_app.Modle.Blog;
import com.example.dessert_order_app.R;
import com.example.dessert_order_app.ViewHolder.BlogViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
TextView txt_blog_message;
    String myId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        recyclerView = view.findViewById(R.id.recycler_blog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        txt_blog_message = view.findViewById(R.id.txt_blog_message);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = currentUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Blog");

        FloatingActionButton fab = view.findViewById(R.id.btn_blog_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new PostBlogFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference blogRef = FirebaseDatabase.getInstance().getReference().child("Blog");

        blogRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    txt_blog_message.setVisibility(View.GONE);

                    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Blog>()
                            .setQuery(databaseReference, Blog.class)
                            .build();


                    final FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final BlogViewHolder blogViewHolder, int i, @NonNull Blog blog) {
                            final String blog_id = getRef(i).getKey();

                            databaseReference.child(blog_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String blog_title = dataSnapshot.child("blog_title").getValue().toString();
                                    String blog_date = dataSnapshot.child("blog_date").getValue().toString();
                                    String blog_description = dataSnapshot.child("blog_description").getValue().toString();
                                    String image_path = dataSnapshot.child("image_path").getValue().toString();
                                    String myId = dataSnapshot.child("blog_user_id").getValue().toString();

                                    DatabaseReference userRef = firebaseDatabase.getReference().child("Users").child(myId);
                                    userRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String icon_path = dataSnapshot.child("imagePath").getValue().toString();
                                            String blog_user_name = dataSnapshot.child("name").getValue().toString();
                                            blogViewHolder.getTxt_blog_user().setText(" by " + blog_user_name);
                                            Picasso.get().load(icon_path).into(blogViewHolder.getImage_blog_circle_icon());

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    blogViewHolder.getTxt_blog_title().setText(blog_title);
                                    blogViewHolder.getTxt_blog_date().setText(blog_date);
                                    blogViewHolder.getTitle_blog_des().setText(blog_description);
                                    Picasso.get().load(image_path).into(blogViewHolder.getImage_blog_image());


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @NonNull
                        @Override
                        public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_blog, parent, false);
                            BlogViewHolder holder = new BlogViewHolder(view);


                            return holder;
                        }

                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }else {
                    txt_blog_message.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
