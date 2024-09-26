package com.ruoyi.nature.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.CashsForm;
import com.ruoyi.nature.dto.ContextDetail;
import com.ruoyi.nature.dto.UserDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 微信用户Service接口
 *
 * @author ruoyi
 * @date 2020-10-28
 */
public interface ICUserService extends IService<CUser> {

    Integer countFollow(String followUid, String id);

    void insertFollow(String followUid, String id);

    void deleteFollow(String followUid, String id);

    BigDecimal getRatioById(String id);

    int addPoints(String uid, Integer points);

    void modifyCashs(CashsForm form);

    UserDetail getUserByOpenId(String openid);

    List<UserDetail> getTopicUserList(String topicId);

    List<ContextDetail> getContextByUid(Integer pageNum, Integer pageSize, String id);

    int getContextCountByUid(String id);

    List<UserDetail> getFollower(String id);

    List<UserDetail> getFanser(String id);

    Map getCountByUid(String id);

    List<UserDetail> getCeller(Integer size);

    Boolean followStatus(String id, String fid);
}
