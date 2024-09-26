package com.ruoyi.nature.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.CashsForm;
import com.ruoyi.nature.dto.ContextDetail;
import com.ruoyi.nature.dto.UserDetail;
import com.ruoyi.nature.mapper.CUserMapper;
import com.ruoyi.nature.service.ICUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信用户Service业务层处理
 *
 * @author ruoyi
 * @date 2020-10-28
 */
@Service
public class CUserServiceImpl extends ServiceImpl<CUserMapper, CUser> implements ICUserService {
    @Override
    public Integer countFollow(String followUid, String id) {
        return baseMapper.countFollow(followUid, id);
    }

    @Override
    public void insertFollow(String followUid, String id) {
        baseMapper.insertFollow(followUid, id);
    }

    @Override
    public void deleteFollow(String followUid, String id) {
        baseMapper.deleteFollow(followUid, id);
    }

    @Override
    public BigDecimal getRatioById(String id) {
        return baseMapper.getRatioById(id);
    }

    @Override
    public int addPoints(String uid, Integer points) {
        return baseMapper.addPoints(uid, points);
    }

    @Override
    public void modifyCashs(CashsForm form) {
        CUser cUser = getById(form.getId());
        if(cUser == null){
            throw new CustomException("找不到该用户");
        }
        if (form.getType() == 1){
//            加款
            BigDecimal nowCashs = cUser.getCashs() == null ? BigDecimal.ZERO : cUser.getCashs();
            cUser.setCashs(nowCashs.add(form.getCashs()));

        }else if(form.getType() == 2){
//            减款
            BigDecimal nowCashs = cUser.getCashs() == null ? BigDecimal.ZERO : cUser.getCashs();
            BigDecimal thisCashs = nowCashs.subtract(form.getCashs());
            if (thisCashs.compareTo(BigDecimal.ZERO) == -1){
                cUser.setCashs(BigDecimal.ZERO);
            }else{
                cUser.setCashs(thisCashs);
            }
        }
        updateById(cUser);
    }

    @Override
    public UserDetail getUserByOpenId(String openid) {
        return baseMapper.getUserByOpenId(openid);
    }

    @Override
    public List<UserDetail> getTopicUserList(String topicId) {
        return baseMapper.getTopicUserList(topicId);
    }

    @Override
    public List<ContextDetail> getContextByUid(Integer pageNum, Integer pageSize, String id) {
        return baseMapper.getContextByUid((pageNum - 1) * pageSize, pageSize, id);
    }

    @Override
    public int getContextCountByUid(String id) {
        return baseMapper.getContextCountByUid(id);
    }

    @Override
    public List<UserDetail> getFollower(String id) {
        return baseMapper.getFollower(id);
    }

    @Override
    public List<UserDetail> getFanser(String id) {
        return baseMapper.getFanser(id);
    }

    @Override
    public Map getCountByUid(String id) {
        return baseMapper.getCountByUid(id);
    }

    @Override
    public List<UserDetail> getCeller(Integer size) {
        return baseMapper.getCeller(size);
    }

    @Override
    public Boolean followStatus(String id, String fid) {
        Integer isFollow = baseMapper.countFollow(fid, id);
        return isFollow > 0 ? true : false;
    }
}
