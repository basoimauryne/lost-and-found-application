package info.lostandfound.firebase.Model;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Date;
import org.joda.time.DateTime;

public class Upload implements Serializable {

    public String name;
    public String image;
    public String productname;
    public String productdesc;
    public String email;
    public String tag;
    public String date;
    public String type;



    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String email, String productname, String productdesc, String image, String tag, String date,String type) {
        this.name = name;
        this.email = email;
        this.productname = productname;
        this.productdesc = productdesc;
        this.image = image;
        this.tag = tag;
        this.date = date;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public String getImage() {
        return image;
    }

    public String getTag() {
        return tag;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }



    }


