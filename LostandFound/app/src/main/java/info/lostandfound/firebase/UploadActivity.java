package info.lostandfound.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.IgnoreExtraProperties;

public class UploadActivity{

    public String name;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadActivity() {
    }

    public UploadActivity(String name, String url) {
        this.name = name;
        this.url= url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
