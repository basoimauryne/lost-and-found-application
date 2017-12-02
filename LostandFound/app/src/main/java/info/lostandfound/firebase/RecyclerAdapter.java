package info.lostandfound.firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import com.bumptech.glide.Glide;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;

    public RecyclerAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Upload upload = uploads.get(position);

        holder.postusername.setText(upload.getName());
        holder.postproductname.setText(upload.getProductname());
        holder.postproductdesc.setText(upload.getProductdesc());
        holder.postCategory.setText(upload.getTag());

        Glide.with(context).load(upload.getImage()).into(holder.postImageView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postusername;
        public TextView postproductname;
        public TextView postproductdesc;
        public TextView postCategory;
        public ImageView postImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            postusername = (TextView) itemView.findViewById(R.id.postusername);
            postproductname = (TextView) itemView.findViewById(R.id.postproductname);
            postproductdesc = (TextView) itemView.findViewById(R.id.postproductdesc);
            postCategory = (TextView) itemView.findViewById(R.id.postCategory);
            postImageView = (ImageView) itemView.findViewById(R.id.postImageView);
        }
    }
}