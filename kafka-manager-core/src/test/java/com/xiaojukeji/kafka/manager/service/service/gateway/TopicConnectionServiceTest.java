package com.xiaojukeji.kafka.manager.service.service.gateway;

import com.xiaojukeji.kafka.manager.common.entity.ao.topic.TopicConnection;
import com.xiaojukeji.kafka.manager.common.entity.pojo.gateway.TopicConnectionDO;
import com.xiaojukeji.kafka.manager.service.config.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuguang
 * @Date 2021/12/7
 */
public class TopicConnectionServiceTest extends BaseTest {

    @Autowired
    private TopicConnectionService topicConnectionService;

    private static final String TOPIC_NAME = "moduleTest";

    private static final Long CLUSTER_ID = 1L;

    private static final String APP_ID = "dkm_admin";

    @DataProvider(name = "provideTopicConnection")
    public static Object[][] provideTopicConnection() {
        TopicConnectionDO topicConnectionDO = new TopicConnectionDO();
        topicConnectionDO.setId(13L);
        topicConnectionDO.setAppId(APP_ID);
        topicConnectionDO.setClusterId(CLUSTER_ID);
        topicConnectionDO.setTopicName(TOPIC_NAME);
        topicConnectionDO.setType("fetch");
        topicConnectionDO.setIp("172.23.142.253");
        topicConnectionDO.setClientVersion("2.4");
        topicConnectionDO.setCreateTime(new Date(1638786493173L));
        return new Object[][] {{topicConnectionDO}};
    }

    // 测试批量插入为空的情况
    @Test
    private void batchAdd2EmptyTest() {
        topicConnectionService.batchAdd(new ArrayList<>());
    }

    // 测试批量插入成功的情况，通过调整list的数量和TopicConnectionServiceImpl中splitInterval的数量，使每个流程都测试一遍
    @Test(dataProvider = "provideTopicConnection")
    private void batchAdd2SuccessTest(TopicConnectionDO topicConnectionDO) {
        List<TopicConnectionDO> list = new ArrayList<>();
        list.add(topicConnectionDO);
        list.add(topicConnectionDO);
        list.add(topicConnectionDO);
        topicConnectionService.batchAdd(list);
    }

    @Test(dataProvider = "provideTopicConnection")
    public void getByTopicName2Test(TopicConnectionDO topicConnectionDO) {
        List<TopicConnection> result = topicConnectionService.getByTopicName(CLUSTER_ID, TOPIC_NAME, new Date(0L), new Date());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).toString(), topicConnectionDO.toString());
    }

    // 测试获取数据时为空
    @Test
    public void getByTopicName2EmptyTest() {
        List<TopicConnection> result = topicConnectionService.getByTopicName(100L, "xgTestxxx", new Date(0L), new Date());
        Assert.assertTrue(result.isEmpty());
    }

    // 测试获取数据,clusterId不为null，TODO
    @Test(dataProvider = "provideTopicConnection")
    public void getByTopicName2SuccessTest(TopicConnectionDO topicConnectionDO) {
        List<TopicConnectionDO> list = new ArrayList<>();
        list.add(topicConnectionDO);
        topicConnectionService.batchAdd(list);
        
        List<TopicConnection> result = topicConnectionService.getByTopicName(CLUSTER_ID, TOPIC_NAME, new Date(0L), new Date());
        Assert.assertTrue(result.stream().anyMatch(topicConnection -> topicConnection.getTopicName().equals(TOPIC_NAME)
                                                            && topicConnection.getClusterId().equals(CLUSTER_ID)));
    }
}