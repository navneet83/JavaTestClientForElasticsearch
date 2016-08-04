import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

public class QueryDemo {

    public void queryDemo() throws  java.net.UnknownHostException{
        //query dsl -https://gist.github.com/navneet83/d8b162af3a9005793d2d

        MultiMatchQueryBuilder mlt1 = QueryBuilders
                .multiMatchQuery("IDA","code.uppercaseraw", "code.reverse", "code.analyzed")
                .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);

        MatchQueryBuilder mlt2 = QueryBuilders.matchQuery("productname","baby shampoo")
                .type(MatchQueryBuilder.Type.PHRASE_PREFIX);

        QueryBuilder qb = QueryBuilders.boolQuery()
                .must(mlt1)
                .must(mlt2);

        SearchResponse response = TransportClientSingleton.getClient()
                .prepareSearch("testindex")
                .setTypes("testtype1")
                .setQuery(qb)
                .setSize(10)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();

        for (SearchHit hit: hits) {
          System.out.println(hit.getSource().toString());
        }

    }

    public static void main (String args[]){
        try
        {
            new QueryDemo().queryDemo();
        }catch(Exception ex){}
    }
}
