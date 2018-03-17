package familylocation.bhulijyo.com.familylocation;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "familylocation-mobilehub-1634377105-Friends")

public class FriendsDO {
    private String _id;
    private String _userId;
    private String _friendId;
    private Boolean _shareLocation;

    @DynamoDBHashKey(attributeName = "Id")
    @DynamoDBAttribute(attributeName = "Id")
    public String getId() {
        return _id;
    }

    public void setId(final String _id) {
        this._id = _id;
    }
    @DynamoDBRangeKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "friendId")
    public String getFriendId() {
        return _friendId;
    }

    public void setFriendId(final String _friendId) {
        this._friendId = _friendId;
    }
    @DynamoDBAttribute(attributeName = "shareLocation")
    public Boolean getShareLocation() {
        return _shareLocation;
    }

    public void setShareLocation(final Boolean _shareLocation) {
        this._shareLocation = _shareLocation;
    }

}
