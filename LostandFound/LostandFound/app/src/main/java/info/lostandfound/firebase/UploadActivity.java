package info.lostandfound.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/23/2017.
 */
@IgnoreExtraProperties
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