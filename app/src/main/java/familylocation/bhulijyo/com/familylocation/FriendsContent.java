package familylocation.bhulijyo.com.familylocation;

import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarak on 17/03/18.
 */

public class FriendsContent {

    DynamoDBMapper dynamoDBMapper;
    static String TAG;

    public FriendsContent(){
        TAG = FriendsContent.this.getClass().getName();
    }

    public List<FriendItem> getFriends(final String userId){
        final List<FriendItem> returnList = new ArrayList<FriendItem>();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
        Runnable runnable = new Runnable() {
            public void run() {

                //First get the count of friends for given user id
                FriendCountDO countKey = new FriendCountDO();
                countKey.setUserId(userId);
                DynamoDBQueryExpression<FriendCountDO> queryExpression = new DynamoDBQueryExpression<FriendCountDO>().withHashKeyValues(countKey);
                final List<FriendCountDO> storedEntries = dynamoDBMapper
                        .query(FriendCountDO.class, queryExpression);
                int count = storedEntries.get(0).getCount().intValue();
                Log.d(TAG, "total friends: "+ count);

                //Once count is received get all the friends details
                for (int i=1; i<= count; i++){
                    FriendsDO friendKey = new FriendsDO();
                    StringBuilder builder = new StringBuilder();
                    builder.append(userId).append(i);
                    Log.d(TAG, "search key: "+ builder.toString());
                    friendKey.setId(builder.toString());

                    DynamoDBQueryExpression<FriendsDO> friendQueryExpression = new DynamoDBQueryExpression<FriendsDO>().withHashKeyValues(friendKey);
                    final List<FriendsDO> friendsList = dynamoDBMapper
                            .query(FriendsDO.class, friendQueryExpression);
                    Log.d(TAG, "friend details: "+ friendsList.get(0).getFriendId());

                    returnList.add(new FriendItem(friendsList.get(0).getFriendId(), friendsList.get(0).getShareLocation()));
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnList;
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class FriendItem {
        public final String name;
        public final Boolean locationShared;


        public FriendItem(String name, Boolean locationShared) {
            this.name = name;
            this.locationShared = locationShared;
        }

        @Override
        public String toString() {
            return name + ":" + locationShared;
        }
    }
}
