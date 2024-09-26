package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.CommentDetail;
import com.ruoyi.nature.dto.CommentStatusDto;

import java.util.List;

/**
 * 评论Mapper接口
 *
 * @author ruoyi
 * @date 2021-08-14
 */
public interface CCommentMapper extends BaseMapper<CComment> {

    List<CommentDetail> getCommentsByCid(String cid);

    Integer delCCai(String cid, String id);

    void insertCZan(String cid, String id);

    Integer delCZan(String cid, String id);

    void insertCCai(String cid, String id);

    Integer countCCai(String cid, String id);

    Integer countCZan(String cid, String id);

    CommentDetail getParentByCid(String cid);

    List<Boolean> commentStatus(String id, String uid);

    List<CommentDetail> getAllCommentsByCid(String cid, String uid);
}
