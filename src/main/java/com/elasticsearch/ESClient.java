package com.elasticsearch;

import com.mvc.model.Group;
import net.sf.json.JSONObject;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by linchuanli on 2018/7/16.
 */
public class ESClient {
    private static final String GROUPS      = "groups";
    private static final String MEMBERS     = "members";
    private static final String INDEX_USER  = "user";
    private static final String INDEX_GROUP = "group";
    private static final String TYPE_INFO   = "info";
    private static final String TYPE_MEMBER = "member";
    private static final String TYPE_JOINED = "joinedGroup";
    private static final Logger logger = LoggerFactory.getLogger(ESClient.class);
    private static final String serverIp = "127.0.0.1";      // 内网ip
    private static TransportClient client;


    static {
        try{
            Settings settings = Settings.builder()
                    .put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中.build();
                    .build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), 9300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createGroupInfoIndex(Group group) {
        String groupId = group.getGroupId();
        JSONObject json = JSONObject.fromObject(group);
        IndexResponse response = null;
        try {
            response = client.prepareIndex(INDEX_GROUP, TYPE_INFO, groupId)
                    .setSource(json.toString(), XContentType.JSON)
                    .get();
        } catch (Exception e) {
            logger.error(e.getMessage() + "caused by: " + response.toString());
        }
        String _index = response.getIndex();
        String _type  = response.getType();
        String _id    = response.getId();
        logger.info("Index a document successfully, index: " + _index + ", type: " + _type + ", id:" + _id);
    }

    public void createGroupMemberIndex(Group group) {
        String groupId = group.getGroupId();
        String groupOwnerWxid = group.getGroupOwnerWxid();
        List<String> memberList = new ArrayList<>();
        memberList.add(groupOwnerWxid);
        IndexResponse response = null;
        try {
            response = client.prepareIndex(INDEX_GROUP, TYPE_MEMBER, groupId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field(MEMBERS, memberList)
                            .endObject())
                    .get();
        } catch (Exception e) {
            logger.error(e.getMessage() + "caused by: " + response.toString());
        }
    }

    public boolean indexExists(String index){
        IndicesAdminClient adminClient = client.admin().indices();
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }
        return false;
    }

    public boolean joinInGroup(String userWxid, String groupId) {
        Set<String> groupSet = new HashSet<>();
        groupSet.add(groupId);
        IndexResponse response = null;
        // group member index adds this user
        if (indexExists(INDEX_GROUP)) {
            GetResponse getResponse = client.prepareGet(INDEX_GROUP, TYPE_MEMBER, groupId).execute().actionGet();

        }
        try {
            response = client.prepareIndex(INDEX_GROUP, TYPE_MEMBER, groupId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field(MEMBERS, memberList)
                            .endObject())
                    .get();
        } catch (Exception e) {
            logger.error(e.getMessage() + "caused by: " + response.toString());
        }
        // user joined group index adds this group
        if (indexExists(INDEX_USER)) {
            GetResponse getResponse = client.prepareGet(INDEX_USER, TYPE_JOINED, userWxid).execute().actionGet();
            String result = getResponse.getSourceAsString();
            if (null != result) {
                Map<String, Object > resultMap = getResponse.getSourceAsMap();
                List<String> groups = (List) resultMap.get(GROUPS);
                groupSet.addAll(groups);
            }
        }
        try {
            response = client.prepareIndex(INDEX_USER, TYPE_JOINED, userWxid)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field(GROUPS, groupSet)
                            .endObject())
                    .get();
        } catch (Exception e) {
            logger.error(e.getMessage() + "caused by: " + response.toString());
            return false;
        }

        return true;
    }
}
