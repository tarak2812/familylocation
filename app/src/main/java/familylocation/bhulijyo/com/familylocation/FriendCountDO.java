package familylocation.bhulijyo.com.familylocation;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "familylocation-mobilehub-1634377105-FriendCount")

public class FriendCountDO {
    private String _userId;
    private Double _count;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "count")
    public Double getCount() {
        return _count;
    }

    public void setCount(final Double _count) {
        this._count = _count;
    }

}
