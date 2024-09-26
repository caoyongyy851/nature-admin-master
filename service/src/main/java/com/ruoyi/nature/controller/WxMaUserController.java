package com.ruoyi.nature.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.WxLoginService;
import com.ruoyi.framework.web.service.WxUser;
import com.ruoyi.nature.config.WxMaConfiguration;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.UserDetail;
import com.ruoyi.nature.service.ICUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user/{appid}")
@Slf4j
public class WxMaUserController{

    @Autowired
    private WxLoginService wxLoginService;
    @Autowired
    private ICUserService icUserService;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public R login(@PathVariable String appid, String code, String token) {

        WxUser wxUser = null;
        if (StringUtils.isNotEmpty(token)) {
            log.info("token:{}", token);
            wxUser = wxLoginService.getWxUser(token);
        }
        if (StringUtils.isEmpty(token) || StringUtils.isNull(wxUser)) {
            if (StringUtils.isBlank(code)) {
                return R.error("empty jscode");
            }
            final WxMaService wxService = WxMaConfiguration.getMaService(appid);
            try {
                WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
                log.info(session.getSessionKey());
                log.info(session.getOpenid());
                wxUser = new WxUser();
                wxUser.setOpenid(session.getOpenid());
                UserDetail cUser = icUserService.getUserByOpenId(session.getOpenid());
                if (StringUtils.isNotNull(cUser)) {
                    if (StringUtils.isNotEmpty(cUser.getNickname())) {
                        wxUser.setNickname(cUser.getNickname());
                        String newToken = wxLoginService.createToken(wxUser);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", newToken);
                        map.put("user", cUser);
                        return R.success("登录成功", map);

                    } else {
                        //非首次,未授权
                        String newToken = wxLoginService.createToken(wxUser);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", newToken);
                        map.put("user", cUser);
                        return R.error("非首次,未授权", map);
                    }
                } else {
                    //首次尝试登陆
                    String newToken = wxLoginService.createToken(wxUser);
                    CUser user = new CUser();
                    user.setOpenid(session.getOpenid());
                    icUserService.save(user);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("token", newToken);
                    map.put("user", user);
                    return R.error("未登录,请授权", map);
                }
            } catch (WxErrorException e) {
                log.error(e.getMessage());
                return R.error(e.toString());
            }

        } else {
            String nickname = wxUser.getNickname();
            UserDetail cUser = icUserService.getUserByOpenId(wxUser.getOpenid());
            if (StringUtils.isNotBlank(nickname)) {
                //更新缓存
                wxLoginService.setWxUser(wxUser);
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                map.put("user", cUser);
                return R.success("登录成功", map);
            } else {
                wxLoginService.setWxUser(wxUser);
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                map.put("user", cUser);
                return R.error("非首次,未授权",map);
            }
        }
    }

    @PostMapping("postAuth")
    public R postAuth(@RequestBody CUser cUser, HttpServletRequest request) {
        String token = wxLoginService.getToken(request);
        WxUser wxUser = wxLoginService.getWxUser(token);
        wxUser.setNickname(cUser.getNickname());
        wxLoginService.setWxUser(wxUser);
        cUser.setOpenid(wxUser.getOpenid());
        QueryWrapper<CUser> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", wxUser.getOpenid());
        icUserService.update(cUser, wrapper);
        return R.success("授权成功");
    }


//    /**
//     * <pre>
//     * 获取用户信息接口
//     * </pre>
//     */
//    @GetMapping("/info")
//    public String info(@PathVariable String appid, String sessionKey,
//                       String signature, String rawData, String encryptedData, String iv) {
//        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
//
//        // 用户信息校验
//        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
//            return "user check failed";
//        }
//
//        // 解密用户信息
//        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
//
//        return JsonUtils.toJson(userInfo);
//    }
//

}
