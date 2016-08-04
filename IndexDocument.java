import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
public class IndexDocument {

    public void indexDocumentExample() throws IOException {
        Client client = TransportClientSingleton.getClient();
        IndexResponse response = client.prepareIndex("testindex", "testtype1", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("code", "IDX_ABC123_DT")
                        .field("post_date", getDateinFormat("yyyy-MM-dd"))
                        .field("productname", "baby cream")
                        .field("productcomment", "this data was loaded using index api")
                        .endObject()).get();
        System.out.println(response.toString());

    }

   private String getDateinFormat(String format) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        return sdfDate.format(new Date());
    }

    public static void main(String args[]){
        IndexDocument idx = new IndexDocument();
        try {
            idx.indexDocumentExample();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
