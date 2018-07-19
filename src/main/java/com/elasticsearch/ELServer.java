package com.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by linchuanli on 2018/7/11.
 */
public class ELServer {
    private static final Logger logger = LoggerFactory.getLogger(ELServer.class);
    private static final String serverIp = "127.0.0.1";      // 内网ip
//        private static final String serverIp = "101.201.103.114";
    private static TransportClient client;

    static {
        try{
            Settings settings = Settings.builder()
//                    .put("cluster.name", "my-application")
                    .put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中.build();
                    .build();
// Settings settings = Settings.builder().build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), 9300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doIndex(){
        String index = "group";
        String type = "info";
        String groupId = "djlafje";
        IndexResponse response = null;
        try {
            response = client.prepareIndex(index, type, groupId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "llc")
                            .field("postDate", new DateTime())
                            .field("message", "{\"cmd\":\"\",\"memberId\":\"c6a845ef875144b980cb915c3a0e0a140.8537223\",\"replyModule\":\"combinator\",\"to\":\"user\",\"link\":\"combinator\",\"replyContent\":\"歌谣do re mi\",\"replyType\":\"text\",\"replyConfidence\":1,\"packageId\":1482739135303,\"deviceId\":\"18:97:ff:04:55:c0\"}")
                            .endObject()
                    )
                    .get();
        } catch (Exception e) {
            logger.error(e.getMessage() + "caused by: " + response.toString());
        }
        logger.info("Index a document successfully, index: " + index + ", type: " + type);
    }

    public void doGet() {
        GetResponse response = client.prepareGet("twitter", "tweet", "2").get();
//        System.out.println(response.getSourceAsString());
    }

    public void doSearch() {
        SearchResponse response = client.prepareSearch("twitter")
                .setTypes("tweet")
                .setQuery(QueryBuilders.termQuery("user", ""))   // Query
                .setQuery(QueryBuilders.termQuery("postDate", "2016-12-21T05:49:58.774Z"))
                .setQuery(QueryBuilders.termQuery("message", "trying out Elasticsearch"))
//                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .get();
//        logger.info("Response: " + response.toString());
    }

    public List<String> getComplexLogByDate(String date, String memberId, String deviceId, String module, String level, String env,
                                            String src) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        Map<String, String> fields = new HashMap<>();
        if (!memberId.equals("all")) {
            fields.put("memberId", memberId);
        }
        if (!deviceId.equals("all")) {
            fields.put("deviceId", deviceId);
        }
        if (!module.equals("all") && src.equals("xiaole")) {
            fields.put("module", module);
        }
        if (!level.equals("all")) {
            fields.put("level", level);
        }
        if (!env.equals("all")) {
            fields.put("environment", env);
        }
        if (src.equals("beiwa")) {
            fields.put("module", "cockroach");
        }
        for (Map.Entry<String, String> entry : fields.entrySet()){
            boolQuery.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }
        List<String> logList = new ArrayList<>();
        try {
            SearchResponse scrollResp = client.prepareSearch("el-" + date)
                    .setTypes("stat")
                    .setQuery(boolQuery)
                    .setScroll(new TimeValue(60000))
                    .setQuery(boolQuery)
                    .setSize(1000)
                    .get();

            //Scroll until no hits are returned
            do {
                String sourceAsString = "";
                for (SearchHit hit : scrollResp.getHits().getHits()) {
                    //Handle the hit...
                    sourceAsString = hit.getSourceAsString();
                    if (sourceAsString != null) {
                        logList.add(sourceAsString);
                    }
                }
                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
            } while(scrollResp.getHits().getHits().length != 0);

        } catch (Exception e) {
//            logger.error(e.getMessage() + "index: " + "el-" + date);
        }
        return logList;
    }



//    private JSONArray convert2JsonArray(SearchResponse response) {
////        logger.info("Start to convert response to json ...");
//        if (response == null)
////            logger.info("response is null.");
//        List<String> list = new ArrayList<>();
//        try {
//            SearchHit[] results = response.getHits().getHits();
//            String sourceAsString = "";
//            for (SearchHit hit : results) {
//                sourceAsString = hit.getSourceAsString();
//                if (sourceAsString != null) {
//                    list.add(sourceAsString);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Convert to JsonArray error: " + e.getMessage());
//        }
//        JSONArray jsonArray = JSONArray.fromObject(list);
//        return jsonArray;
//    }

    public static void main(String[] args) {
        logger.info("Run...");
        ELServer server = new ELServer();
        server.doIndex();
    }
}
