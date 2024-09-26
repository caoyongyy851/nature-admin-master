package com.ruoyi.nature.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CScheMapper;
import com.ruoyi.nature.domain.CSche;
import com.ruoyi.nature.service.ICScheService;

import java.util.List;
import java.util.Map;

/**
 * 档期Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-10
 */
@Service
public class CScheServiceImpl extends ServiceImpl<CScheMapper, CSche> implements ICScheService {
    @Override
    public CSche getOneByPlaceId(String id) {
        return baseMapper.getOneByPlaceId(id);
    }

    @Override
    public List<Map> getListByPlaceId(String id) {
        return baseMapper.getListByPlaceId(id);
    }
}
