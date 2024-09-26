package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CSche;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 档期Service接口
 *
 * @author ruoyi
 * @date 2021-10-10
 */
public interface ICScheService extends IService<CSche> {

    CSche getOneByPlaceId(String id);

    List<Map> getListByPlaceId(String id);
}
