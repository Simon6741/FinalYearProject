package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>{

    private Context mContext;
    private ArrayList<ImageUpload> mImgList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public ImageListAdapter(Context context, ArrayList<ImageUpload> imageList){
        mContext = context;
        mImgList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        ImageViewHolder ivh = new ImageViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageUpload currentItem = mImgList.get(position);
        holder.mName.setText(currentItem.getName());
        holder.mDesc.setText(currentItem.getDesc());
        holder.mCategory.setText("Category : " + currentItem.getCategory());
        holder.mPrice.setText(String.format("RM%.2f", currentItem.getPrice()));
        holder.mStock.setText("Available stock : " + currentItem.getAvailableStock());
        Glide.with(mContext)
                .load(mImgList.get(position).getUrl())
                .placeholder(R.drawable.blank_image_icon)
                .fitCenter()
                .into(holder.mimageView);
    }

    @Override
    public int getItemCount() {
        return mImgList.size();
    }


    //try
    public class  ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public ImageView mimageView;
        public TextView mDesc;
        public TextView mPrice;
        public TextView mCategory;
        public TextView mStock;
        public ImageView mDeleteImage;
        public ImageView mEditImage;

        public  ImageViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            mName = itemView.findViewById(R.id.productName);
            mimageView = itemView.findViewById(R.id.imgView);
            mDesc = itemView.findViewById(R.id.productDesc);
            mCategory = itemView.findViewById(R.id.productCat);
            mPrice = itemView.findViewById(R.id.productPrice);
            mStock = itemView.findViewById(R.id.productStock);
            mDeleteImage = itemView.findViewById(R.id.image_delete);
            mEditImage = itemView.findViewById(R.id.image_edit);


            mEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){

                            listener.onEditClick(position);
                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){

                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }


    }

}
