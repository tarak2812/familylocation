package familylocation.bhulijyo.com.familylocation;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.List;


public class MyFriendsRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendsRecyclerViewAdapter.ViewHolder> {

    private final List<FriendsContent.FriendItem> mValues;
    String TAG;

    public MyFriendsRecyclerViewAdapter(List<FriendsContent.FriendItem> items) {
        TAG = MyFriendsRecyclerViewAdapter.this.getClass().getName();
        Log.d(TAG, "item count: "+items.size());
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
        holder.mSwitch.setText(mValues.get(position).name);
        holder.mSwitch.setChecked(mValues.get(position).locationShared);
        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLocationSharedState("tarakbhatt", holder.mSwitch.getText().toString(), b);
            }
        });

    }

    private void updateLocationSharedState(final String currentUserId, final String friendId, final Boolean locationShared){
        final DynamoDBMapper dynamoDBMapper;
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());

        dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
        Runnable runnable = new Runnable() {
            public void run() {

                //First get the count of friends for given user id
                FriendCountDO countKey = new FriendCountDO();
                countKey.setUserId(currentUserId);
                DynamoDBQueryExpression<FriendCountDO> queryExpression = new DynamoDBQueryExpression<FriendCountDO>().withHashKeyValues(countKey);
                final List<FriendCountDO> storedEntries = dynamoDBMapper
                        .query(FriendCountDO.class, queryExpression);
                int count = storedEntries.get(0).getCount().intValue();
                Log.d(TAG, "total friends: "+ count);

                for (int i=1; i<= count; i++) {
                    FriendsDO friendKey = new FriendsDO();
                    StringBuilder builder = new StringBuilder();
                    builder.append(currentUserId).append(i);
                    Log.d(TAG, "search key: " + builder.toString());
                    friendKey.setId(builder.toString());

                    DynamoDBQueryExpression<FriendsDO> friendQueryExpression = new DynamoDBQueryExpression<FriendsDO>().withHashKeyValues(friendKey);
                    final List<FriendsDO> friendsList = dynamoDBMapper
                            .query(FriendsDO.class, friendQueryExpression);
                    Log.d(TAG, "friend details: " + friendsList.get(0).getFriendId());
                    if (friendsList.get(0).getFriendId().equals(friendId)){
                        //update location preference
                        friendsList.get(0).setShareLocation(locationShared);
                        dynamoDBMapper.save(friendsList.get(0));
                    }

                }


                    }

            };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final Switch mSwitch;
        public FriendsContent.FriendItem mItem;

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
