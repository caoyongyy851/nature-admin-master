package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CTopicCover;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.dto.TopicCover;

import java.util.List;

/**
 * 话题封面Service接口
 *
 * @author ruoyi
 * @date 2021-12-20
 */
public interface ICTopicCoverService extends IService<CTopicCover> {

    List<TopicCover> getCover();
}
