package com.ruoyi.nature.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.domain.CActivity;
import com.ruoyi.nature.dto.ActivityDetail;
import com.ruoyi.nature.dto.JoinDto;
import com.ruoyi.nature.dto.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动Service接口
 *
 * @author ruoyi
 * @date 2020-11-17
 */
public interface ICActivityService extends IService<CActivity> {

  ArrayList<CActivity> getSwiperActivity();

  ArrayList<CActivity> getActivityList(Integer pageNum, Integer pageSize);

  ActivityDetail getDetailById(String id);

  ArrayList<CActivity> getHotActivityList(Integer pageNum, Integer pageSize);

  void insertCollections(String aid, String id);

  Integer delCollections(String aid, String id);

  void insertAZan(String aid, String id);

  Integer delAZan(String aid, String id);

  void insertACai(String aid, String id);

  Integer delACai(String aid, String id);

  Integer countCollections(String aid, String id);

  Integer countAppreciate(String aid, String id);

  Integer countTread(String aid, String id);

  Integer countJoin(String aid, String id);

  void insertJoin(String aid, String id, Integer person, String remark);

  void deleteJoin(String aid, String id);

    List<ActivityDetail> getActivityByUid(String id, Integer pageNum, Integer pageSize);

  List<UserDetail> getActivityUserList(String id);

  JoinDto getJoin(String aid, String id);

    List<UserDetail> getJoinList(String id);
}
