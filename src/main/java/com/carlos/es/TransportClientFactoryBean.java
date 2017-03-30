package com.carlos.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Carlos on 2017/3/23.
 */

public class TransportClientFactoryBean {
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;
    @Value("${elasticsearch.cluster.host}")
    private String esHost;

    private TransportClient client;

    //设置为1.7.1版本的客户端连接方式
    public void initClient()  {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName) //设置集群名称
                .put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中
                .build();
        client = new TransportClient(settings);
        try {
            //获取所有节点
            String []nodes = esHost.split(",");
            for (String node:nodes){
                //跳过为空的node（当开头、结尾有逗号或多个连续逗号时会出现空node）
                if (node.length()>0){
                    //获取每个节点的ip和port
                    String []hostPort = node.split(":");
                    //每个节点加入客户端
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostPort[0]), Integer.parseInt(hostPort[1])));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public TransportClient getClient(){
        return client;
    }
}
