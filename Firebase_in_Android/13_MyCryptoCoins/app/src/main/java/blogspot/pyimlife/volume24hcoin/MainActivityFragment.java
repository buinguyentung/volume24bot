package blogspot.pyimlife.volume24hcoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainRecyclerViewAdapter mRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_fragment_main_list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupList();
    }

    private void setupList() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Bitcoin");
        data.add("Etherium");
        data.add("Ripple");
        data.add("Bitcoin Cash");
        data.add("Litecoin");
        data.add("NEO");
        data.add("Stellar");
        data.add("EOS");
        data.add("Cardano");
        data.add("Stellar");
        data.add("IOTA");
        data.add("Dash");
        data.add("Monero");
        data.add("TRON");
        data.add("NEM");
        data.add("ICON");
        data.add("Bitcoin Gold");
        data.add("Zcash");
        data.add("Verge");

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new MainRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
