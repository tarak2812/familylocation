package familylocation.bhulijyo.com.familylocation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.util.List;

import familylocation.bhulijyo.com.familylocation.dummy.DummyContent.DummyItem;


public class MyFriendsRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendsRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;

    public MyFriendsRecyclerViewAdapter(List<DummyItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSwitch.setText(mValues.get(position).content);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final Switch mSwitch;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSwitch = (Switch) view.findViewById(R.id.switch2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSwitch.getText() + "'";
        }
    }
}
