package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CSwiperTopic;
import com.ruoyi.nature.domain.CTopic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.dto.TopicCover;
import com.ruoyi.nature.dto.TopicDetail;
import com.ruoyi.nature.dto.TopicStatusDto;
import com.ruoyi.nature.dto.TopicUserDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题Service接口
 *
 * @author ruoyi
 * @date 2021-11-03
 */
public interface ICTopicService extends IService<CTopic> {

    ArrayList<TopicUserDto> getTopicList(Integer type, Integer pageNum, Integer pageSize);

    TopicDetail getDetailById(String id);


    ArrayList<TopicUserDto> getTopicBest();

    ArrayList<TopicUserDto> getTopicSelect();

    List<CSwiperTopic> getSwiper();

    TopicStatusDto topicStatus(String id, String uid);

    List<TopicUserDto> getTopicByUid(String uid, Integer pageNum, Integer pageSize);

    Integer getTopicCountByUid(String id);

    Integer countFollow(String topicId, String id);

    void insertFollow(String id, String id1);

    void deleteFollow(String id, String id1);

    ArrayList<TopicUserDto> getFollowTopicList(String uid ,Integer pageNum, Integer pageSize);

    ArrayList<TopicUserDto> getSiftTopicList(Integer type ,Integer pageNum, Integer pageSize);
}
