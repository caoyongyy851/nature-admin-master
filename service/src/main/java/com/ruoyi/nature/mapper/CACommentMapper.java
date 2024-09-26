package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CAComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.CommentDetail;

import java.util.List;

/**
 * 活动评论Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-08
 */
public interface CACommentMapper extends BaseMapper<CAComment> {

    Integer countCZan(String cid, String id);

    List<CommentDetail> getCommentsByAid(String aid);

    void insertCZan(String aid, String id);

    Integer delCZan(String aid, String id);

    List<CommentDetail> getAllCommentsByAid(String aid, String id);
}
