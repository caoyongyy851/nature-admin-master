package com.ruoyi.nature.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.domain.CActivity;
import com.ruoyi.nature.dto.ActivityDetail;
import com.ruoyi.nature.dto.JoinDto;
import com.ruoyi.nature.dto.UserDetail;
import com.ruoyi.nature.mapper.CActivityMapper;
import com.ruoyi.nature.service.ICActivityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动Service业务层处理
 *
 * @author ruoyi
 * @date 2020-11-17
 */
@Service
public class CActivityServiceImpl extends ServiceImpl<CActivityMapper, CActivity> implements ICActivityService {


  @Override
  public ArrayList<CActivity> getSwiperActivity() {
    return baseMapper.getSwiperActivity();
  }

  @Override
  public ArrayList<CActivity> getActivityList(Integer pageNum, Integer pageSize) {
    return baseMapper.getActivityList((pageNum-1)*pageSize,pageSize);
  }

  @Override
  public ActivityDetail getDetailById(String id) {
    return baseMapper.getDetailById(id);
  }

  @Override
  public ArrayList<CActivity> getHotActivityList(Integer pageNum, Integer pageSize) {
    return baseMapper.getHotActivityList((pageNum-1)*pageSize,pageSize);
  }

  @Override
  public void insertCollections(String aid, String id) {
    baseMapper.insertCollections(aid, id);
  }

  @Override
  public Integer delCollections(String aid, String id) {
    return baseMapper.delCollections(aid, id);
  }

  @Override
  public void insertAZan(String aid, String id) {
    baseMapper.insertAZan(aid, id);
  }

  @Override
  public Integer delAZan(String aid, String id) {
    return baseMapper.delAZan(aid, id);
  }

  @Override
  public void insertACai(String aid, String id) {
    baseMapper.insertACai(aid, id);
  }

  @Override
  public Integer delACai(String aid, String id) {
    return baseMapper.delACai(aid, id);
  }

  @Override
  public Integer countCollections(String aid, String id) {
    return baseMapper.countCollections(aid, id);
  }

  @Override
  public Integer countAppreciate(String aid, String id) {
    return baseMapper.countAppreciate(aid, id);
  }

  @Override
  public Integer countTread(String aid, String id) {
    return baseMapper.countTread(aid, id);
  }

  @Override
  public Integer countJoin(String aid, String id) {
    return baseMapper.countJoin(aid, id);
  }

  @Override
  public void insertJoin(String aid, String id, Integer person, String remark) {
     baseMapper.insertJoin(aid, id, new Date(), person, remark);
  }

  @Override
  public void deleteJoin(String aid, String id) {
     baseMapper.deleteJoin(aid, id);
  }

  @Override
  public List<ActivityDetail> getActivityByUid(String uid, Integer pageNum, Integer pageSize) {
    return baseMapper.getActivityByUid(uid, (pageNum - 1) * pageSize, pageSize);
  }

  @Override
  public List<UserDetail> getActivityUserList(String id) {
    return baseMapper.getActivityUserList(id);
  }

  @Override
  public JoinDto getJoin(String aid, String id) {
    return baseMapper.getJoin(aid, id);
  }

  @Override
  public List<UserDetail> getJoinList(String id) {
    return  baseMapper.getJoinList(id);
  }
}
