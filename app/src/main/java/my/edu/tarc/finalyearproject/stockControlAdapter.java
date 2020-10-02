package my.edu.tarc.finalyearproject;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class stockControlAdapter extends RecyclerView.Adapter<stockControlAdapter.ImageViewHolder>{

    private Context mContext;
    private ArrayList<StockControl> mControlList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public stockControlAdapter(Context context, ArrayList<StockControl> controlList){
        mContext = context;
        mControlList = controlList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.control_item, parent, false);
        ImageViewHolder ivh = new ImageViewHolder(v,mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        StockControl currentItem = mControlList.get(position);
        holder.mDateTime.setText(currentItem.getDate() + "   "+currentItem.getTime());
        String styledText = "<font color='cyan'>"+currentItem.getName()+"</font>" + currentItem.getDescription()+".";
        holder.mDesc.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        holder.mStock.setText("Available stock : " + currentItem.getAvailableStock());
    }

    @Override
    public int getItemCount() {
        return mControlList.size();
    }


    //try
    public class  ImageViewHolder extends RecyclerView.ViewHolder {


        public TextView mDesc;
        public TextView mDateTime;
        public TextView mStock;

        public  ImageViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            mDesc = itemView.findViewById(R.id.stockDesc);
            mStock = itemView.findViewById(R.id.stock_qty);
            mDateTime = itemView.findViewById(R.id.control_date_time);
            
        }


    }

}
