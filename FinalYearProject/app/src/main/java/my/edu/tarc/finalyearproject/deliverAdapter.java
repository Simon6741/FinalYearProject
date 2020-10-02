package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class deliverAdapter extends RecyclerView.Adapter<deliverAdapter.DeliverViewHolder>{

    private Context mContext;
    private ArrayList<Order> dList;
    private deliverAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(deliverAdapter.OnItemClickListener listener){

        mListener = listener;
    }

    public deliverAdapter(Context context, ArrayList<Order> deliverList){
        mContext = context;
        dList = deliverList;
    }

    @NonNull
    @Override
    public deliverAdapter.DeliverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.deliver_item, parent, false);
        deliverAdapter.DeliverViewHolder ivh = new deliverAdapter.DeliverViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull deliverAdapter.DeliverViewHolder holder, int position) {
        Order currentItem = dList.get(position);
        holder.dID.setText(currentItem.getKey());
        holder.dName.setText("Name : " + currentItem.getName());
        holder.dPhone.setText("Phone : " + currentItem.getPhone());
        holder.dAddress.setText(currentItem.getAddress());
        holder.dPrice.setText(String.format("RM%.2f", currentItem.getTotalAmount()));
        holder.dDateTime.setText("Date : "+currentItem.getDate()+"  Time : "+currentItem.getTime());

    }

    @Override
    public int getItemCount() {
        return dList.size();
    }


    //try
    public class  DeliverViewHolder extends RecyclerView.ViewHolder {

        public TextView dPrice;
        public TextView dID;
        public TextView dName;
        public TextView dPhone;
        public TextView dAddress;
        public TextView dDateTime;

        public  DeliverViewHolder(View itemView, final deliverAdapter.OnItemClickListener listener){
            super(itemView);
            dID = itemView.findViewById(R.id.deliver_ID);
            dName = itemView.findViewById(R.id.owner_name);
            dPhone = itemView.findViewById(R.id.owner_phone);
            dAddress = itemView.findViewById(R.id.deliver_address);
            dDateTime = itemView.findViewById(R.id.deliver_date_time);
            dPrice = itemView.findViewById(R.id.deliver_amount);

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
