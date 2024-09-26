package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CTopicCover;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.TopicCover;

import java.util.List;

/**
 * 话题封面Mapper接口
 *
 * @author ruoyi
 * @date 2021-12-20
 */
public interface CTopicCoverMapper extends BaseMapper<CTopicCover> {

    List<TopicCover> getCover();
}
