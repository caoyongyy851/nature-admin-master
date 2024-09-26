package com.ruoyi.nature.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.domain.CActivity;
import com.ruoyi.nature.dto.ActivityDetail;
import com.ruoyi.nature.dto.JoinDto;
import com.ruoyi.nature.dto.UserDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动Mapper接口
 *
 * @author ruoyi
 * @date 2020-11-17
 */
public interface CActivityMapper extends BaseMapper<CActivity> {

  ArrayList<CActivity> getSwiperActivity();

  ArrayList<CActivity> getActivityList(int i, Integer pageSize);

    ActivityDetail getDetailById(String id);

  ArrayList<CActivity> getHotActivityList(int i, Integer pageSize);

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

  void insertJoin(String aid, String id, Date date, Integer person, String remark);

  void deleteJoin(String aid, String id);

  List<ActivityDetail> getActivityByUid(String uid, int i, Integer pageSize);

  List<UserDetail> getActivityUserList(String id);

  JoinDto getJoin(String aid, String id);

    List<UserDetail> getJoinList(String id);
}
