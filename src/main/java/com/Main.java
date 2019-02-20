package com;

import com.elasticsearch.ESClient;
import com.elasticsearch.ESClientStore;
import com.mvc.model.Group;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by linchuanli on 2018/7/4.
 */
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("run main");
        ESClient esClient = ESClientStore.getInstance().getEsClient();
        Group group = new Group("groupTest","llc","getup","2018-07-15", 15, 100, "7:00", "8:00");
        JSONObject json = JSONObject.fromObject(group);
        System.out.println(json.toString());
//  esClient.createGroupInfoIndex(group);
//        esClient.createGroup("llc2", "uJJ_1L-C1xu3M97C");
    }
}
