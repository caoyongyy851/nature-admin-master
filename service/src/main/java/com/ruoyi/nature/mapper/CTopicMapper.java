package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CSwiperTopic;
import com.ruoyi.nature.domain.CTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.TopicCover;
import com.ruoyi.nature.dto.TopicDetail;
import com.ruoyi.nature.dto.TopicUserDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-03
 */
public interface CTopicMapper extends BaseMapper<CTopic> {

    ArrayList<TopicUserDto> getTopicList(Integer type, int i, Integer pageSize);

    TopicDetail getDetailById(String id);

    ArrayList<TopicUserDto> getTopicBest();

    ArrayList<TopicUserDto> getTopicSelect();

    List<CSwiperTopic> getSwiper();

    List<CSwiperTopic> getSwiperTwo();

    List<TopicUserDto> getTopicByUid(String uid, int i, Integer pageSize);

    Integer getTopicCountByUid(String id);

    Integer countFollow(String topicId, String id);

    void insertFollow(String topicId, String id);

    void deleteFollow(String topicId, String id);

    ArrayList<TopicUserDto> getFollowTopicList(String uid,int i, Integer pageSize);

    ArrayList<TopicUserDto> getSiftTopicList(Integer type, int i, Integer pageSize);
}
