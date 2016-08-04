import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class BulkLoadData {

    public static void main(String args[]) {
        BulkLoadData blk = new BulkLoadData();
        try {
            blk.indexData();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void indexData() throws java.io.IOException {
        Client client = TransportClientSingleton.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
// either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("testindex", "testtype1", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("code", "BLK_ABC123_DT")
                        .field("post_date", getDateinFormat("yyyy-MM-dd"))
                        .field("productname", "baby cream")
                        .field("productcomment", "this data was loaded using bulk api")
                        .endObject()));

        bulkRequest.add(client.prepareIndex("testindex", "testtype1", "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("code", "BLK_ABC456_DT")
                        .field("post_date", getDateinFormat("yyyy-MM-dd"))
                        .field("productname", "baby cream2")
                        .field("productcomment", "this data was loaded using bulk api")
                        .endObject()));

        BulkResponse bulkResponse = bulkRequest.get();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bulkResponse.getItems().length; ++i) {
            BulkItemResponse response = bulkResponse.getItems()[i];
            sb.append("\n[").append(i).append("]: index [")
                    .append(response.getIndex()).append("], type [")
                    .append(response.getType()).append("], id [")
                    .append(response.getId()).append("], message [")
                    .append(response.getFailureMessage()).append("]");
        }

        System.out.println(sb);
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            //System.out.println(bulkResponse.buildFailureMessage());
        }

    }

    public String getDateinFormat(String format) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        return sdfDate.format(new Date());
    }
}
