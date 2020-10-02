package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.Sum;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.CartViewHolder>{

    private Context mContext;
    private ArrayList<Cart> cList;
    private OnItemClickListener mListener;
    private double totalPrice = 0.0;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public cartAdapter(Context context, ArrayList<Cart> cartList){
        mContext = context;
        cList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_item, parent, false);
        CartViewHolder ivh = new CartViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart currentItem = cList.get(position);
        holder.mName.setText("Name : " + currentItem.getPname());
        holder.qty.setText("Quantity : " + currentItem.getQuantity());
        holder.mPrice.setText(String.format("RM%.2f", currentItem.getPprice()));

        double oneTPTotalPrice = currentItem.getPprice() * currentItem.getQuantity();
        totalPrice += oneTPTotalPrice;

        Sum.tp = totalPrice;

    }

    @Override
    public int getItemCount() {


        return cList.size();
    }


    //try
    public class  CartViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public TextView qty;
        public TextView mPrice;
        public ImageView mDeleteImage;
        public ImageView mEditImage;
        public Button mNext;

        public  CartViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            mName = itemView.findViewById(R.id.cart_pname);
            mPrice = itemView.findViewById(R.id.cart_pprice);
            qty = itemView.findViewById(R.id.cart_pquantity);
            mNext = itemView.findViewById(R.id.nextProcessBtn);
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
