package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class deliverHistoryAdapter extends RecyclerView.Adapter<deliverHistoryAdapter.DeliverViewHolder>{

    private Context mContext;
    private ArrayList<DeliverHistory> dList;
    private deliverHistoryAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(deliverHistoryAdapter.OnItemClickListener listener){

        mListener = listener;
    }

    public deliverHistoryAdapter(Context context, ArrayList<DeliverHistory> deliverList){
        mContext = context;
        dList = deliverList;
    }

    @NonNull
    @Override
    public deliverHistoryAdapter.DeliverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.deliver_history, parent, false);
        deliverHistoryAdapter.DeliverViewHolder ivh = new deliverHistoryAdapter.DeliverViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull deliverHistoryAdapter.DeliverViewHolder holder, int position) {
        DeliverHistory currentItem = dList.get(position);
        holder.dID.setText(currentItem.getPid());
        holder.dName.setText("Name : " + currentItem.getName());
        holder.dAddress.setText(currentItem.getAddress());
        holder.dDateTime.setText("Date : "+currentItem.getDate()+"  Time : "+currentItem.getTime());

    }

    @Override
    public int getItemCount() {
        return dList.size();
    }


    //try
    public class  DeliverViewHolder extends RecyclerView.ViewHolder {

        public TextView dID;
        public TextView dName;
        public TextView dAddress;
        public TextView dDateTime;

        public  DeliverViewHolder(View itemView, final deliverHistoryAdapter.OnItemClickListener listener){
            super(itemView);
            dID = itemView.findViewById(R.id.history_ID);
            dName = itemView.findViewById(R.id.history_name);
            dAddress = itemView.findViewById(R.id.history_address);
            dDateTime = itemView.findViewById(R.id.history_date_time);

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
