package com.carlos.es.impl;

import com.carlos.es.IndexCore;
import com.carlos.es.TransportClientFactoryBean;
import com.carlos.utils.CheckUtil;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Carlos on 2017/3/23.
 */
@Service("indexCore")
public class IndexCoreImpl implements IndexCore{
    private static final Logger logger = Logger.getLogger(IndexCoreImpl.class);
    @Autowired
    private TransportClientFactoryBean transportClientFactoryBean;


    /**
     * function:索引一个单独的文档
     * @param indexName 索引名称
     * @param typeName 类型
     * @param id 指定一个id作为主键，以后根据id来唯一标示文档，对文档进行操作
     * @param indexDocs 文档
     * @return 成功返回true,失败返回false
     */
    public boolean indexSingleDoc(String indexName,String typeName,String id,String indexDocs){
        try {
            //检查doc是否为空
            if (CheckUtil.isEmpty(indexName) || CheckUtil.isEmpty(typeName) || indexDocs == null) {
                logger.info("入参异常！");
                return false;
            }
            //索引文档是否成功
            boolean indexDoc = transportClientFactoryBean.getClient().prepareIndex(indexName,typeName,id)
                    .setSource(indexDocs).setRefresh(true)
                    .execute().actionGet().isCreated();
            return indexDoc;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * function:索引一个文档，如果文档已经存在则被更新
     * @param indexName 索引名称
     * @param typeName 类型名称
     * @param indexDocs 文档
     * @return 成功返回true，失败返回false
     */
    public boolean indexUpdateAndInsert(String indexName,String typeName,String id,String indexDocs){
        try {
            if (CheckUtil.isEmpty(indexName) || CheckUtil.isEmpty(typeName) || indexDocs ==null) {
                logger.info("入参异常！");
                return false;
            }
            IndexRequest indexRequest = new IndexRequest(indexName, typeName, id)
                    .source(indexDocs);
            UpdateRequest updateRequest = new UpdateRequest(indexName, typeName, id)
                    .doc(indexDocs)
                    .upsert(indexRequest);
            transportClientFactoryBean.getClient().update(updateRequest).get();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * function:删除一个文档
     * @param indexName 索引名称
     * @param typeName 类型名称
     * @param deleteId 要删除文档的id
     * @param idKey id是那一列
     * @return 成功返回true,失败返回false
     */
    public boolean deleteSingleDoc(String indexName, String typeName,String idKey,String deleteId) {
        try{
            QueryBuilder queryBuilder = QueryBuilders.termQuery(idKey, deleteId);
            DeleteByQueryResponse deleteResponse = transportClientFactoryBean.getClient()
                    .prepareDeleteByQuery(indexName).setQuery(queryBuilder).setTypes(typeName).execute().actionGet();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * function:批量删除
     * @param indexName
     * @param typeName
     * @param deleteIds
     * @return 成功返回true，失败返回false
     */
    public boolean deletebulk (String indexName,String typeName,List<String> deleteIds ){
        try {
            if (CheckUtil.isEmpty(indexName) || CheckUtil.isEmpty(typeName)) {
                return false;
            }

            BulkRequestBuilder bulkDelete = transportClientFactoryBean.getClient().prepareBulk();
            for (String sid:deleteIds){
                bulkDelete.add(transportClientFactoryBean.getClient()
                        .prepareDelete(indexName, typeName, sid)
                        .request());
            }
            bulkDelete.execute().actionGet();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * function:批量插入数据,会根据id完全覆盖以前的文档
     * @param indexName 索引名称
     * @param typeName 类型名称
     * @param indexDocs 文档集合
     * @return 成功返回true，失败返回false
     */
    public boolean indexBulkInsert(String indexName,String typeName,Map<String,String> indexDocs){
        try {
            if (CheckUtil.isEmpty(indexName) || CheckUtil.isEmpty(typeName) || indexDocs ==null||indexDocs.size()==0) {
                logger.info("入参异常！");
                return false;
            }

            List<IndexRequest> requests = new ArrayList<IndexRequest>();
            for (Map.Entry<String,String> entry : indexDocs.entrySet()) {
                if(entry.getKey() != null && entry.getValue()!= null){
                    IndexRequest request = transportClientFactoryBean.getClient()
                            .prepareIndex(indexName, typeName, entry.getKey())
                            .setSource(entry.getValue())
                            .request();
                    requests.add(request);
                }
            }
            //批量插入请求
            BulkRequestBuilder bulkRequestBuilder = transportClientFactoryBean.getClient().prepareBulk();
            for (IndexRequest request : requests) {
                bulkRequestBuilder.add(request);
            }

            try {
                BulkResponse bulkResponse = bulkRequestBuilder.setRefresh(true).execute().actionGet();
                if(bulkResponse.hasFailures()) {
                    System.out.println(bulkResponse.buildFailureMessage());
                }
                return true;
            } catch (ElasticsearchException e){
                e.printStackTrace();
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * function : 批量更新式插入，不覆盖原有的文档，对已经存在的文档只做更新
     * @param indexName 索引名称
     * @param typeName 类型名
     * @param indexDocs 文档集合
     * @return 成功返回true，其他返回false
     */
    public boolean indexBulkUpsert(String indexName, String typeName, Map<String, Object> indexDocs)  {
        try {
            if (CheckUtil.isEmpty(indexName) || CheckUtil.isEmpty(typeName) || indexDocs ==null) {
                logger.info("入参异常！");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        try {
            //批量添加索引请求
            List<UpdateRequest> requests = new ArrayList<>();
            for (Map.Entry<String,Object> entry : indexDocs.entrySet()) {
                if(entry.getKey() != null && entry.getValue()!= null){
                    IndexRequest indexRequest = new IndexRequest(indexName, typeName, entry.getKey())
                            .source(String.valueOf(entry.getValue()));
                    UpdateRequest updateRequest = new UpdateRequest(indexName, typeName, entry.getKey())
                            .doc(String.valueOf(entry.getValue()))
                            .upsert(indexRequest);

                    requests.add(updateRequest);
                }
            }

            BulkRequestBuilder bulkRequestBuilder = transportClientFactoryBean.getClient().prepareBulk();
            for (UpdateRequest request : requests) {
                bulkRequestBuilder.add(request);
            }

            BulkResponse bulkResponse = bulkRequestBuilder.setRefresh(true).execute().actionGet();
            if (bulkResponse.hasFailures()) {
                logger.error("error indexBulkUpsert "+bulkResponse.buildFailureMessage());
                return false;
            }
        }catch (Exception e){
            logger.error("bulk upsert error",e);
        }
        return true;
    }

    /**
     * function:根据一个字段的值查找需要的域的值
     * @param indexName
     * @param typeName
     * @param keyField
     * @param key
     * @param field
     * @return
     */
    public String getFieldValue(String indexName,String typeName,String keyField,String key,String field){
        String value ="";
        try {
            SearchResponse searchResponse = transportClientFactoryBean.getClient()
                    .prepareSearch(indexName).setTypes(typeName)
                    .setQuery(QueryBuilders.termQuery(keyField, key))
                    .execute().actionGet();
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits() > 0){
                for (SearchHit hit : hits) {
                    value = (String)hit.getSource().get(field);
                }
            }
            logger.info("getFieldValue = "+value);
        }catch (ElasticsearchException e){
            e.printStackTrace();
        }
        return value;
    }

    /**
     * function:根据一批key到es中查询对应的value,返回查询到的Map<key,value>
     * @param indexName 索引名称
     * @param typeName 类型
     * @param key 待查的一批key
     * @param keyField 要查询的字段
     * @param valuefield 要返回的字段
     * @return 返回结果
     */
    public Map<String,String> getKeyValueMap(String indexName, String typeName, Map<String,Object> key,String keyField,String valuefield){
        //到ES查询key
        Map<String,String> map_old = new HashMap<>();
        List<String> keys = new ArrayList<>();
        int count = 0;

        for (Map.Entry<String,Object> entry:key.entrySet()){
            if(entry.getKey() != null && entry.getValue()!= null) {
                keys.add(entry.getKey());
            }
            count++;
            if(count==1000){
                break;
            }
        }
        TermsQueryBuilder query = new TermsQueryBuilder(keyField,keys);
        try{
            SearchResponse response = transportClientFactoryBean.getClient()
                    .prepareSearch(indexName).setTypes(typeName).setQuery(query).setSize(keys.size()).execute().actionGet();
            SearchHits hits = response.getHits();
            if (hits.getTotalHits() > 0){
                for (SearchHit hit : hits) {
                    String k = (String)hit.getSource().get(keyField);
                    String v = (String)hit.getSource().get(valuefield);
                    map_old.put(k,v);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map_old;
    }

}
