package my.edu.tarc.finalyearproject;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Domain.Transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context mContext;
    private List<Transaction> mactivityList;

    public ActivityAdapter(Context context, List<Transaction> activityList) {
        mContext = context;
        mactivityList = activityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Transaction singleActivity = mactivityList.get(position);

        Double amount = singleActivity.getTransactionAmt();
        String desc = singleActivity.getTransactionDesc();
        String type = singleActivity.getTransactionType();
        String date = singleActivity.getDatetime();

        holder.txtAmount.setText(String.format("RM %.2f", amount));
        holder.txtDesc.setText(desc);
        holder.txtType.setText(type);
        holder.textDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return mactivityList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAmount, txtDesc, txtType, textDate;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtType = itemView.findViewById(R.id.txtType);
            textDate = itemView.findViewById(R.id.textDate);

        }
    }

}
