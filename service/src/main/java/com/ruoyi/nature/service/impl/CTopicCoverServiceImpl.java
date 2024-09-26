package com.ruoyi.nature.service.impl;

import com.ruoyi.nature.dto.TopicCover;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CTopicCoverMapper;
import com.ruoyi.nature.domain.CTopicCover;
import com.ruoyi.nature.service.ICTopicCoverService;

import java.util.List;

/**
 * 话题封面Service业务层处理
 *
 * @author ruoyi
 * @date 2021-12-20
 */
@Service
public class CTopicCoverServiceImpl extends ServiceImpl<CTopicCoverMapper, CTopicCover> implements ICTopicCoverService {

    @Override
    public List<TopicCover> getCover() {
        return baseMapper.getCover();
    }
}
