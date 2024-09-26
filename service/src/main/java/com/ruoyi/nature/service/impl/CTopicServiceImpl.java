package com.ruoyi.nature.service.impl;

import com.ruoyi.nature.domain.CCard;
import com.ruoyi.nature.domain.CSwiperTopic;
import com.ruoyi.nature.dto.*;
import com.ruoyi.nature.mapper.CUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CTopicMapper;
import com.ruoyi.nature.domain.CTopic;
import com.ruoyi.nature.service.ICTopicService;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-03
 */
@Service
public class CTopicServiceImpl extends ServiceImpl<CTopicMapper, CTopic> implements ICTopicService {

    @Override
    public ArrayList<TopicUserDto> getTopicList(Integer type, Integer pageNum, Integer pageSize) {
        return baseMapper.getTopicList(type,(pageNum - 1) * pageSize, pageSize);
    }

    @Override
    public TopicDetail getDetailById(String id) {
        return baseMapper.getDetailById(id);
    }


    @Override
    public ArrayList<TopicUserDto> getTopicBest() {
        return baseMapper.getTopicBest();
    }

    @Override
    public ArrayList<TopicUserDto> getTopicSelect() {
        return baseMapper.getTopicSelect();
    }

    @Override
    public List<CSwiperTopic> getSwiper() {
        List<CSwiperTopic> swiper = baseMapper.getSwiper();
        if (swiper != null && swiper.size() > 0){
            return swiper;
        }
        return baseMapper.getSwiperTwo();
    }

    @Override
    public TopicStatusDto topicStatus(String id, String uid) {
        CTopic cTopic = getById(id);
        TopicStatusDto topicStatusDto = new TopicStatusDto();
        Integer isFollow = baseMapper.countFollow(cTopic.getId(), uid);
        topicStatusDto.setIsFollow(isFollow > 0 ? true : false);
        return topicStatusDto;
    }

    @Override
    public List<TopicUserDto> getTopicByUid(String uid, Integer pageNum, Integer pageSize) {
        return baseMapper.getTopicByUid(uid, (pageNum - 1) * pageSize, pageSize);
    }

    @Override
    public Integer getTopicCountByUid(String id) {
        return baseMapper.getTopicCountByUid(id);
    }

    @Override
    public Integer countFollow(String topicId, String id) {
        return baseMapper.countFollow(topicId, id);
    }

    @Override
    public void insertFollow(String topicId, String id) {
         baseMapper.insertFollow(topicId, id);
    }

    @Override
    public void deleteFollow(String topicId, String id) {
         baseMapper.deleteFollow(topicId, id);
    }

    @Override
    public ArrayList<TopicUserDto> getFollowTopicList(String uid,Integer pageNum, Integer pageSize) {
        return  baseMapper.getFollowTopicList(uid,(pageNum - 1) * pageSize, pageSize);
    }

    @Override
    public ArrayList<TopicUserDto> getSiftTopicList(Integer type, Integer pageNum, Integer pageSize) {
        return baseMapper.getSiftTopicList(type,(pageNum - 1) * pageSize, pageSize);
    }
}
