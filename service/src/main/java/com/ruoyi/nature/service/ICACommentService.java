package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CAComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.dto.CommentDetail;

import java.util.List;

/**
 * 活动评论Service接口
 *
 * @author ruoyi
 * @date 2021-10-08
 */
public interface ICACommentService extends IService<CAComment> {

    Integer countCZan(String cid, String id);

    List<CommentDetail> getCommentsByAid(String aid);

    void insertCZan(String aid, String id);

    Integer delCZan(String aid, String id);

    List<CommentDetail> getAllCommentsByAid(String aid, String id);
}
