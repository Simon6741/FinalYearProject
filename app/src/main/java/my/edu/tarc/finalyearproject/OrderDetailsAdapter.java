package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.CartViewHolder>{

    private Context mContext;
    private ArrayList<Cart> cList;
    private cartAdapter.OnItemClickListener mListener;
    private double totalPrice = 0.0;


    public OrderDetailsAdapter(Context context, ArrayList<Cart> cartList){
        mContext = context;
        cList = cartList;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.order_detail_item, parent, false);
        OrderDetailsAdapter.CartViewHolder ivh = new OrderDetailsAdapter.CartViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.CartViewHolder holder, int position) {
        Cart currentItem = cList.get(position);
        holder.mName.setText("Name : " + currentItem.getPname());
        holder.qty.setText("Quantity : " + currentItem.getQuantity());
        holder.mPrice.setText(String.format("RM%.2f", currentItem.getPprice()));

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

        public  CartViewHolder(View itemView){
            super(itemView);
            mName = itemView.findViewById(R.id.cart_pname);
            mPrice = itemView.findViewById(R.id.cart_pprice);
            qty = itemView.findViewById(R.id.cart_pquantity);

        }


    }
}
