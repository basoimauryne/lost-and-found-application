package info.lostandfound.firebase.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import info.lostandfound.firebase.R;

public class ClaimActivity extends AppCompatActivity{
     public TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("CONTACTS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        name= findViewById(R.id.postusername);
        email=findViewById(R.id.email);

        Bundle bundle=getIntent().getExtras();
        name.setText(bundle.getString("name"));
        email.setText(bundle.getString("email"));



    }
}
