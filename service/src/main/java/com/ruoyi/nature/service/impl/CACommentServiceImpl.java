package com.ruoyi.nature.service.impl;

import com.ruoyi.nature.dto.CommentDetail;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CACommentMapper;
import com.ruoyi.nature.domain.CAComment;
import com.ruoyi.nature.service.ICACommentService;

import java.util.List;

/**
 * 活动评论Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-08
 */
@Service
public class CACommentServiceImpl extends ServiceImpl<CACommentMapper, CAComment> implements ICACommentService {
    @Override
    public Integer countCZan(String cid, String id) {
        return baseMapper.countCZan(cid, id);
    }

    @Override
    public List<CommentDetail> getCommentsByAid(String aid) {
        return baseMapper.getCommentsByAid(aid);
    }

    @Override
    public void insertCZan(String aid, String id) {
        baseMapper.insertCZan(aid, id);
    }

    @Override
    public Integer delCZan(String aid, String id) {
        return baseMapper.delCZan(aid, id);
    }

    @Override
    public List<CommentDetail> getAllCommentsByAid(String aid, String id) {
        return baseMapper.getAllCommentsByAid(aid, id);
    }
}
