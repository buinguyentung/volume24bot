package blogspot.pyimlife.volume24hcoin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    //private Context context;
    private ArrayList<String> dataList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.item_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainRecyclerViewAdapter(ArrayList<String> dataList) {
        this.dataList = dataList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.fragment_main_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(dataList.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
