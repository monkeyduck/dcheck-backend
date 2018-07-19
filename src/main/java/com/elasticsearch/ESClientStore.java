package com.elasticsearch;

/**
 * Created by linchuanli on 2018/7/16.
 */
public class ESClientStore {
    private static final ESClientStore instance = new ESClientStore();
    private ESClient esClient = null;
    public static ESClientStore getInstance() {
        return instance;
    }

    public ESClient getEsClient() {
        if (esClient == null) {
            synchronized (this) {
                if (esClient == null) {
                    esClient = new ESClient();
                }
            }
        }
        return esClient;
    }
}
