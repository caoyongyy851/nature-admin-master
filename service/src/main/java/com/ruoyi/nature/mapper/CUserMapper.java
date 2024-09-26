package com.ruoyi.nature.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.ContextDetail;
import com.ruoyi.nature.dto.UserDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信用户Mapper接口
 *
 * @author ruoyi
 * @date 2020-10-28
 */
public interface CUserMapper extends BaseMapper<CUser> {

    Integer countFollow(String followUid, String id);

    void insertFollow(String followUid, String id);

    void deleteFollow(String followUid, String id);

    BigDecimal getRatioById(String id);

    int addPoints(String uid, Integer points);

    UserDetail getUserByOpenId(String openid);

    List<UserDetail> getTopicUserList(String topicId);

    List<ContextDetail> getContextByUid(int i, Integer pageSize, String id);

    int getContextCountByUid(String id);

    Map getCountByUid(String id);

    List<UserDetail> getFollower(String id);

    List<UserDetail> getFanser(String id);

    List<UserDetail> getCeller(Integer size);
}
