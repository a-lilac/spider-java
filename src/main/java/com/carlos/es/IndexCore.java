package com.carlos.es;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Carlos on 2017/3/23.
 */
public interface IndexCore {
    /**
     * function:索引一个单独的文档
     * @param indexName 索引名称
     * @param typeName 类型
     * @param id 指定一个id作为主键，以后根据id来唯一标示文档，对文档进行操作
     * @param indexDocs 文档
     * @return 成功返回true,失败返回false
     */
    public boolean indexSingleDoc(String indexName, String typeName, String id, String indexDocs);

    /**
     * function:索引一个文档，如果文档已经存在则被更新
     * @param indexName 索引名称
     * @param typeName 类型名称
     * @param indexDocs 文档
     * @return 成功返回true，失败返回false
     */
    public boolean indexUpdateAndInsert(String indexName, String typeName, String id, String indexDocs);

    /**
     * function:删除一个文档
     * @param indexName 索引名称
     * @param typeName 类型名称
     * @param deleteId 要删除文档的id
     * @param idKey id是那一列
     * @return 成功返回true,失败返回false
     */
    public boolean deleteSingleDoc(String indexName, String typeName, String idKey, String deleteId);

    /**
     * function:批量删除
     * @param indexName
     * @param typeName
     * @param deleteIds
     * @return 成功返回true，失败返回false
     */
    public boolean deletebulk(String indexName, String typeName, List<String> deleteIds);

    /**
     * function:批量插入数据,会根据id完全覆盖以前的文档
     * @param indexName 索引名称
     * @param typeName 类型名称
     * @param indexDocs 文档集合
     * @return 成功返回true，失败返回false
     */
    public boolean indexBulkInsert(String indexName, String typeName, Map<String, String> indexDocs);

    /**
     * function : 批量更新式插入，不覆盖原有的文档，对已经存在的文档只做更新
     * @param indexName 索引名称
     * @param typeName 类型名
     * @param indexDocs 文档集合
     * @return 成功返回true，其他返回false
     */
    public boolean indexBulkUpsert(String indexName, String typeName, Map<String, Object> indexDocs);
    /**
     * function:根据一个字段的值查找需要的域的值
     * @param indexName
     * @param typeName
     * @param keyField
     * @param key
     * @param field
     * @return
     */
    public String getFieldValue(String indexName, String typeName, String keyField, String key, String field);

    /**
     * function:根据一批key到es中查询对应的value,返回查询到的Map<key,value>
     * @param indexName 索引名称
     * @param typeName 类型
     * @param key 待查的一批key
     * @param keyField 要查询的字段
     * @param valuefield 要返回的字段
     * @return 返回结果
     */
    public Map<String,String> getKeyValueMap(String indexName, String typeName, Map<String, Object> key, String keyField, String valuefield);

}
