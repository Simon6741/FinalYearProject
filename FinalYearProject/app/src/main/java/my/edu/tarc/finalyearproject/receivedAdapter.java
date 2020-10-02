package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class receivedAdapter extends RecyclerView.Adapter<receivedAdapter.OrderViewHolder>{

    private Context mContext;
    private ArrayList<Order> oList;
    private receivedAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(receivedAdapter.OnItemClickListener listener){

        mListener = listener;
    }

    public receivedAdapter(Context context, ArrayList<Order> orderList){
        mContext = context;
        oList = orderList;
    }

    @NonNull
    @Override
    public receivedAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.received_item, parent, false);
        receivedAdapter.OrderViewHolder ivh = new receivedAdapter.OrderViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull receivedAdapter.OrderViewHolder holder, int position) {
        Order currentItem = oList.get(position);
        holder.oStatus.setText(currentItem.getState());
        holder.oPrice.setText(String.format("RM%.2f", currentItem.getTotalAmount()));
        holder.oDateTime.setText(currentItem.getDelivered_date()+"  "+currentItem.getDelivered_time());

    }

    @Override
    public int getItemCount() {
        return oList.size();
    }


    //try
    public class  OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView oStatus;
        public TextView oPrice;
        public TextView oDateTime;

        public  OrderViewHolder(View itemView, final receivedAdapter.OnItemClickListener listener){
            super(itemView);
            oStatus = itemView.findViewById(R.id.order_status);
            oDateTime = itemView.findViewById(R.id.received_TimeDate);
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