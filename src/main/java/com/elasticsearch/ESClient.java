package com.elasticsearch;

import com.mvc.model.CheckRecord;
import com.mvc.model.CheckStatus;
import com.mvc.model.Group;
import com.utils.Utils;
import net.sf.json.JSONObject;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
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
    private static final String CHECK_DETAIL= "checkDetail";
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

    public void createGroup(Group group) {
        createGroupInfoIndex(group);
        updateUserJoinedGroups(group.getGroupOwnerWxid(), group.getGroupId());
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


    public boolean indexExists(String index){
        IndicesAdminClient adminClient = client.admin().indices();
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }
        return false;
    }

    public List<String> getListByIndexType(String index, String type, String id, String fields) {
        if (indexExists(index)) {
            GetResponse getResponse = client.prepareGet(index, type, id).execute().actionGet();
            String result = getResponse.getSourceAsString();
            if (null != result) {
                Map<String, Object > resultMap = getResponse.getSourceAsMap();
                return (List) resultMap.get(fields);
            }
        }
        return null;
    }


    public boolean updateGroupMembers(String groupId, String wxId) throws Exception{
        if (indexExists(INDEX_GROUP)) {
            GetResponse getResponse = client.prepareGet(INDEX_GROUP, TYPE_INFO, groupId).execute().actionGet();
            Map<String, Object> resultMap = getResponse.getSourceAsMap();
            Group group = (Group) Utils.map2Object(resultMap, Group.class);
            group.addMember(wxId);
            JSONObject json = JSONObject.fromObject(group);

            //delete id first
            DeleteResponse dResponse = client.prepareDelete(INDEX_GROUP, TYPE_INFO, groupId).execute().actionGet();

            //create id
            IndexResponse response = client.prepareIndex(INDEX_GROUP, TYPE_INFO, groupId)
                    .setSource(json.toString(), XContentType.JSON)
                    .get();

            logger.info("update getResult: {} successfully", response.getResult());
            return true;
        }
        logger.error("Index: %s not exists", INDEX_GROUP);
        return false;
    }

    public boolean updateUserJoinedGroups(String wxId, String groupId) {
        List<String> groupList = getListByIndexType(INDEX_USER, TYPE_JOINED, wxId, GROUPS);
        Set<String> groupSet = new HashSet<>();
        groupSet.add(groupId);
        IndexResponse response = null;
        if (null != groupList) {
            groupSet.addAll(groupList);
        }
        try {
            response = client.prepareIndex(INDEX_USER, TYPE_JOINED, wxId)
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

    public boolean joinInGroup(String groupId, String wxId) throws Exception{
        return updateGroupMembers(groupId, wxId) && updateUserJoinedGroups(wxId, groupId);
    }

    public void check(String groupId, String wxId, String date, String checkTime, int value) throws Exception{
        if (indexExists(INDEX_GROUP)) {
            GetResponse getResponse = client.prepareGet(INDEX_GROUP, TYPE_INFO, groupId).execute().actionGet();
            Map<String, Object> resultMap = getResponse.getSourceAsMap();
            Map<String, Map<String, CheckRecord>> detailMap = (Map) resultMap.get(CHECK_DETAIL);
            CheckRecord checkRecord = detailMap.get(date).get(wxId);
            checkRecord.setCheckStatus(CheckStatus.Checked);
            checkRecord.setCheckTime(checkTime);
            checkRecord.setValue(value);
            detailMap.get(date).put(wxId, checkRecord);
            resultMap.put(CHECK_DETAIL, detailMap);
            UpdateResponse updateResponse = client.prepareUpdate(INDEX_GROUP, TYPE_INFO, groupId)
                    .setDoc(resultMap).get();
            logger.info("update getResult: {} successfully", updateResponse.getGetResult());
        }
    }

}
