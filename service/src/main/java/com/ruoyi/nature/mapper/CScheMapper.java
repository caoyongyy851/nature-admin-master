package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CSche;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 档期Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-10
 */
public interface CScheMapper extends BaseMapper<CSche> {

    CSche getOneByPlaceId(String id);

    List<Map> getListByPlaceId(String id);
}
