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

public class productListAdapter extends RecyclerView.Adapter<productListAdapter.ProductViewHolder> {

    private Context mContext;
    private ArrayList<ImageUpload> mImgList;
    private productListAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(productListAdapter.OnItemClickListener listener){

        mListener = listener;
    }

    public productListAdapter(Context context, ArrayList<ImageUpload> productList){
        mContext = context;
        mImgList = productList;
    }

    @NonNull
    @Override
    public productListAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        productListAdapter.ProductViewHolder ivh = new productListAdapter.ProductViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull productListAdapter.ProductViewHolder holder, int position) {
        ImageUpload currentItem = mImgList.get(position);
        holder.mName.setText(currentItem.getName());
        holder.mDesc.setText(currentItem.getDesc());
        holder.mCategory.setText("Category : " + currentItem.getCategory());
        holder.mPrice.setText(String.format("RM%.2f", currentItem.getPrice()));
        holder.mStock.setText(String.format("Available stock : %3d", currentItem.getAvailableStock()));
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
    public class  ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public ImageView mimageView;
        public TextView mDesc;
        public TextView mPrice;
        public TextView mCategory;
        public TextView mStock;


        public  ProductViewHolder(View itemView, final productListAdapter.OnItemClickListener listener){
            super(itemView);
            mName = itemView.findViewById(R.id.productName);
            mimageView = itemView.findViewById(R.id.imgView);
            mDesc = itemView.findViewById(R.id.productDesc);
            mCategory = itemView.findViewById(R.id.productCat);
            mPrice = itemView.findViewById(R.id.productPrice);
            mStock = itemView.findViewById(R.id.productStock);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener !=null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){

                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }


    }


}