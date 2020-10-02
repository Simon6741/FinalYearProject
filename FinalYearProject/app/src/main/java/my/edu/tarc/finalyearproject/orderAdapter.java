package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.OrderViewHolder>{

    private Context mContext;
    private ArrayList<Order> oList;
    private orderAdapter.OnItemClickListener mListener;
    private double totalPrice = 0.0;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(orderAdapter.OnItemClickListener listener){

        mListener = listener;
    }

    public orderAdapter(Context context, ArrayList<Order> orderList){
        mContext = context;
        oList = orderList;
    }

    @NonNull
    @Override
    public orderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
        orderAdapter.OrderViewHolder ivh = new orderAdapter.OrderViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull orderAdapter.OrderViewHolder holder, int position) {
        Order currentItem = oList.get(position);
        holder.oStatus.setText(currentItem.getState());
        holder.oID.setText("Order ID : " + currentItem.getKey());
        holder.oPrice.setText(String.format("Total Price : RM%.2f", currentItem.getTotalAmount()));
        holder.oDateTime.setText("Date : "+currentItem.getDate()+"  Time : "+currentItem.getTime());

    }

    @Override
    public int getItemCount() {
        return oList.size();
    }


    //try
    public class  OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView oStatus;
        public TextView oPrice;
        public TextView oID;
        public TextView oDateTime;
        public Button mNext;

        public  OrderViewHolder(View itemView, final orderAdapter.OnItemClickListener listener){
            super(itemView);
            oStatus = itemView.findViewById(R.id.order_status);
            oID = itemView.findViewById(R.id.order_id);
            oDateTime = itemView.findViewById(R.id.order_date_time);
            oPrice = itemView.findViewById(R.id.order_amount);

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