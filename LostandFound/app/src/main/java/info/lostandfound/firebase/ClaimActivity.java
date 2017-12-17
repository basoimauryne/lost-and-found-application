package info.lostandfound.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ClaimActivity extends AppCompatActivity{
     public TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);


        name= findViewById(R.id.postusername);
        email=findViewById(R.id.email);

        Bundle bundle=getIntent().getExtras();
        name.setText(bundle.getString("name"));
        email.setText(bundle.getString("email"));



    }
}
