package com.ruoyi.nature.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.nature.dto.CommentDetail;
import com.ruoyi.nature.dto.CommentStatusDto;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CCommentMapper;
import com.ruoyi.nature.domain.CComment;
import com.ruoyi.nature.service.ICCommentService;

import java.util.List;

/**
 * 评论Service业务层处理
 *
 * @author ruoyi
 * @date 2021-08-14
 */
@Service
public class CCommentServiceImpl extends ServiceImpl<CCommentMapper, CComment> implements ICCommentService {

    @Override
    public List<CommentDetail> getCommentsByCid(String aid) {
        return baseMapper.getCommentsByCid(aid);
    }

    @Override
    public Integer delCCai(String cid, String id) {
        return baseMapper.delCCai(cid, id);
    }

    @Override
    public void insertCZan(String cid, String id) {
        baseMapper.insertCZan(cid, id);
    }

    @Override
    public Integer delCZan(String cid, String id) {
        return baseMapper.delCZan(cid, id);
    }

    @Override
    public void insertCCai(String cid, String id) {
        baseMapper.insertCCai(cid, id);
    }

    @Override
    public Integer countCCai(String cid, String id) {
        return baseMapper.countCCai(cid, id);
    }

    @Override
    public Integer countCZan(String cid, String id) {
        return baseMapper.countCZan(cid, id);
    }

    @Override
    public CommentDetail getParentByCid(String cid) {
        return baseMapper.getParentByCid(cid);
    }

    @Override
    public List<Boolean> commentStatus(String id, String uid) {
        return baseMapper.commentStatus(id, uid);
    }

    @Override
    public List<CommentDetail> getAllCommentsByCid(String cid, String uid) {
        return baseMapper.getAllCommentsByCid(cid, uid);
    }
}
