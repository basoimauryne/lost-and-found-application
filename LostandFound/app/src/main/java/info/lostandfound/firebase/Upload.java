package info.lostandfound.firebase;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.IgnoreExtraProperties;

public class Upload {

    public String name;
    public String image;
    public String productname;
    public String productdesc;
    public String email;
    public String tag;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String email, String productname, String productdesc, String image, String tag) {
        this.name = name;
        this.email= email;
        this.productname= productname;
        this.productdesc= productdesc;
        this.image= image;
        this.tag= tag;

    }

    public String getName() { return name;}
    public String getEmail () { return email; }
    public String getProductname () { return productname; }
    public String getProductdesc () { return productdesc; }
    public String getImage() {return image;}
    public String getTag () { return tag; }
}