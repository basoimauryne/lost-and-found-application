package info.lostandfound.firebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import com.bumptech.glide.Glide;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.lostandfound.firebase.Activity.ClaimActivity;
import info.lostandfound.firebase.Activity.Constants;
import info.lostandfound.firebase.Activity.MainActivity;
import info.lostandfound.firebase.Model.Upload;
import info.lostandfound.firebase.R;

import static android.R.id.list;
import static android.content.ContentValues.TAG;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements Filterable {


    private Context context;
    private List<Upload> uploads;
    private List<Upload> uploadsFiltered;
    public String name, email;
    public  DatabaseReference mDatabase2;


    public RecyclerAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
        this.uploadsFiltered = uploads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list, parent, false);
        mDatabase2 = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CLAIMED);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (uploadsFiltered != null && uploadsFiltered.size() !=0){

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final Upload upload = uploadsFiltered.get(position);
            name = upload.getName();
            email = upload.getEmail();
            final String name2=user.getDisplayName();




            holder.postusername.setText(upload.getName());
            holder.postproductname.setText(upload.getProductname());
            holder.postproductdesc.setText(upload.getProductdesc());
            holder.postCategory.setText(upload.getTag());
            holder.timestamp.setText(upload.getDate());

            holder.btn_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    notifyDataSetChanged();
                    Intent intent = new Intent(context, ClaimActivity.class);
                    intent.putExtra("name", uploadsFiltered.get(position).getName());
                    intent.putExtra("email", uploadsFiltered.get(position).getEmail());
                    view.getContext().startActivity(intent);
                }

            });
            if (name.equals(name2)) {

                holder.btn_claimed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query key =ref.child("uploads").orderByChild("productname").equalTo(upload.getProductname());

                        key.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {

                                    uploadsFiltered.remove(holder.getAdapterPosition());
                                    String uploadId = mDatabase2.push().getKey();
                                    mDatabase2.child(uploadId).setValue(appleSnapshot.getValue());
                                    appleSnapshot.getRef().removeValue();
                                    uploadsFiltered.clear();
                                    uploadsFiltered.addAll(uploadsFiltered);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(),uploadsFiltered.size());

                                    notifyDataSetChanged();


                                    Toast.makeText(context, " Successfully claimed!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });




                    }
                });



            }
            else{
                holder.btn_claimed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Only "+ name +" can mark it successfully claimed ", Toast.LENGTH_LONG).show();
                    }
                });
            }


            Glide.with(context).load(upload.getImage()).into(holder.postImageView);
        }

    }


    @Override
    public int getItemCount() {

        return uploadsFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postusername;
        public TextView postproductname;
        public TextView postproductdesc;
        public TextView postCategory;
        public ImageView postImageView;
        public Button btn_claim;
        public Button btn_claimed;
        public TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);

            postusername = (TextView) itemView.findViewById(R.id.postusername);
            postproductname = (TextView) itemView.findViewById(R.id.postproductname);
            postproductdesc = (TextView) itemView.findViewById(R.id.postproductdesc);
            postCategory = (TextView) itemView.findViewById(R.id.postCategory);
            postImageView = (ImageView) itemView.findViewById(R.id.postImageView);
            btn_claim = itemView.findViewById(R.id.btn_claim);
            btn_claimed = itemView.findViewById(R.id.btn_claimed);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);


        }
    }




    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                } else {
                    List<Upload> filteredList = new ArrayList<>();
                    for (Upload row : uploads) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getProductname().toLowerCase().contains(charString.toLowerCase()) || row.getProductdesc().toLowerCase().contains(charString.toLowerCase()) ||row.getType().toLowerCase().contains(charString.toLowerCase())|| row.getTag().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }

                    uploadsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = uploadsFiltered;
                return filterResults;
            }



            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                uploadsFiltered = (ArrayList<Upload>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilter2() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                } else {
                    List<Upload> filteredList = new ArrayList<>();
                    for (Upload row : uploads) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getType().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }

                    uploadsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = uploadsFiltered;
                return filterResults;
            }



            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                uploadsFiltered = (ArrayList<Upload>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


   /* public void filtertype(String type) {
        List<Upload> list = new ArrayList<>();
        List<Upload> filteredList = Lists.newArrayList(Collections2.filter(
                list,

        notifyDataSetChanged();
    }
    public void filterBooks_documents() {
        filtertype("Books_documents");
    }

    public void filteraccessories() {
        filtertype("accessories");
    }
    public void filterelectronics() {
        filtertype("electronics");
    }

    public void filterclothing() {
        filtertype("clothing");
    }
    public void filterpersons() {
        filtertype("persons");
    }

    public void filterother() {
        filtertype("other");
    }
*/



}