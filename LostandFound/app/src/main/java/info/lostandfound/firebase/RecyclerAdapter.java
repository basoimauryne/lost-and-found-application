package info.lostandfound.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.List;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements Filterable {


    private Context context;
    private List<Upload> uploads;
    private List<Upload> uploadsFiltered;
    public String name, email;


    public RecyclerAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
        this.uploadsFiltered = uploads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

            if (uploadsFiltered != null && uploadsFiltered.size() !=0){
                final Upload upload = uploadsFiltered.get(position);
                name = upload.getName();
                email = upload.getEmail();

                holder.postusername.setText(upload.getName());
                holder.postproductname.setText(upload.getProductname());
                holder.postproductdesc.setText(upload.getProductdesc());
                holder.postCategory.setText(upload.getTag());
                holder.btn_claim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(context, ClaimActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        view.getContext().startActivity(intent);
                    }

                });

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

        public ViewHolder(View itemView) {
            super(itemView);

            postusername = (TextView) itemView.findViewById(R.id.postusername);
            postproductname = (TextView) itemView.findViewById(R.id.postproductname);
            postproductdesc = (TextView) itemView.findViewById(R.id.postproductdesc);
            postCategory = (TextView) itemView.findViewById(R.id.postCategory);
            postImageView = (ImageView) itemView.findViewById(R.id.postImageView);
            btn_claim = itemView.findViewById(R.id.btn_claim);


        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    uploadsFiltered = uploads;
                } else {
                    List<Upload> filteredList = new ArrayList<>();
                    for (Upload row : uploads) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getProductname().toLowerCase().contains(charString.toLowerCase()) || row.getProductdesc().toLowerCase().contains(charString.toLowerCase()) || row.getTag().toLowerCase().contains(charString.toLowerCase())) {
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


}
