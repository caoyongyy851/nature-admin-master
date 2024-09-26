package com.ruoyi.nature.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.nature.config.WxMaConfiguration;
import com.ruoyi.nature.config.WxMineProperties;
import com.ruoyi.nature.domain.*;
import com.ruoyi.nature.dto.*;
import com.ruoyi.nature.service.*;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.*;

/*
 *@DESCRIPTION
 *@author Ye
 *@create 2020/11/17 23:03
 */
@RestController
@RequestMapping("nature/nocheck")
public class FrontNoCheckController {
    @Autowired
    private ICActivityService icActivityService;
    @Autowired
    private ICCommentService icCommentService;
    @Autowired
    private ICACommentService icaCommentService;
    @Autowired
    private ICCardService icCardService;
    @Autowired
    private ICPlaceService icPlaceService;
    @Autowired
    private ICScheService icScheService;
    @Autowired
    private ICCategoryService icCategoryService;
    @Autowired
    private ICUserService icUserService;
    @Autowired
    private ICTopicService icTopicService;
    @Autowired
    private ICDocService icDocService;
    @Autowired
    private ICTopicCoverService icTopicCoverService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WxMineProperties wxMineProperties;

    @GetMapping("getHotActivityList/{pageNum}/{pageSize}")
    @Cacheable(value = "getHotActivityList", key = "'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getHotActivityList(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        ArrayList<CActivity> list = icActivityService.getHotActivityList(pageNum, pageSize);
        return R.success("activityList", list);
    }

    @GetMapping("getActivityDetailById/{id}")
//    @Cacheable(value = "getActivityDetailById", key = "'id_'+#id", cacheManager = "redisExpire")
    public R getActivityDetailById(@PathVariable String id) {
        CActivity cActivity = icActivityService.getById(id);
        if (StringUtils.isNotNull(cActivity)) {
            cActivity.setViews(cActivity.getViews() + 1);
            icActivityService.updateById(cActivity);
            ActivityDetail activityDetail = icActivityService.getDetailById(id);
            return R.success("获取成功", activityDetail);
        }
        return R.error("查询失败");
    }

    /**
     * ********************************* 自然玩主 ********************************
     */

    @GetMapping("getSwiperActivity")
    @Cacheable(value = "getSwiperActivity", key = "'swiper'", cacheManager = "redisExpire")
    public R getSwiperActivity() {
        ArrayList<CActivity> list = icActivityService.getSwiperActivity();
        return R.success("swiperList", list);
    }

    @GetMapping("getActivityList/{pageNum}/{pageSize}")
    @Cacheable(value = "getActivityList", key = "'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getActivityList(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        ArrayList<CActivity> list = icActivityService.getActivityList(pageNum, pageSize);
        return R.success("activityList", list);
    }

    /**
     * 获取帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getCardList/{pageNum}/{pageSize}")
    @Cacheable(value = "getCardList", key = "'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getCardList(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        ArrayList<CardUserDto> cardList = icCardService.getCardList(pageNum, pageSize);
        cardList.forEach(e -> {
            if (StringUtils.isNotNull(e.getImgs())) {
                ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                e.setImgList(imgList);
                e.setImgs(null);
            }
            if (StringUtils.isNotNull(e.getVids())) {
                ArrayList<String> vidList = new ArrayList<>(Arrays.asList(e.getVids().split(",")));
                e.setVidList(vidList);
                e.setVids(null);
            }
        });
        return R.success("getCardList", cardList);
    }

    @GetMapping("getCardDetailById/{id}")
    @Cacheable(value = "getCardDetailById", key = "'id_'+#id", cacheManager = "redisExpire")
    public R getCardDetailById(@PathVariable String id) {
        CCard cCard = icCardService.getById(id);
        if (StringUtils.isNotNull(cCard)) {
            cCard.setViews(cCard.getViews() + 1);
            icCardService.updateById(cCard);
            CardDetail cardDetail = icCardService.getDetailById(id);
            List<String> avatarList = icCardService.getLikes(id);
            cardDetail.setAvatarLikes(avatarList);
            return R.success("获取成功", cardDetail);
        }
        return R.error("查询失败");
    }

    @GetMapping("getCommentsByCid/{cid}")
    @Cacheable(value = "getCommentsByCid", key = "'cid_'+#cid", cacheManager = "redisExpire")
    @ApiOperation(value = "根据帖子id查询评论")
    public R getCommentsByCid(@PathVariable String cid) {
        List<CommentDetail> commentDetailList = icCommentService.getCommentsByCid(cid);
        return R.success("获取成功", commentDetailList);
    }

    @GetMapping("getCommentsByAid/{aid}")
    @Cacheable(value = "getCommentsByAid", key = "'aid_'+#aid", cacheManager = "redisExpire")
    @ApiOperation(value = "根据帖子id查询评论")
    public R getCommentsByAid(@PathVariable String aid) {
        List<CommentDetail> commentDetailList = icaCommentService.getCommentsByAid(aid);
        return R.success("获取成功", commentDetailList);
    }


    @GetMapping("getPositionList")
    @Cacheable(value = "getPositionList", key = "'position'", cacheManager = "redisExpire")
    @ApiOperation(value = "查询场地用于select")
    public R getPositionList() {
        List<CPlace> cPlaceList = icPlaceService.getPositionList();
        return R.success("获取成功", cPlaceList);
    }


    /**
     * 获取推荐场地列表     *
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getReferPlaceList/{pageNum}/{pageSize}")
    @Cacheable(value = "getReferPlaceList", key = "'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getReferPlaceList(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        ArrayList<PlaceDto> placeList = icPlaceService.getReferPlaceList(pageNum, pageSize);
        placeList.forEach(e -> {
            if (StringUtils.isNotEmpty(e.getImages())) {
                ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImages().split(",")));
                e.setImageList(imgList);
                e.setImages(null);
            }
            if (StringUtils.isNotEmpty(e.getVideos())) {
                ArrayList<String> vidList = new ArrayList<>(Arrays.asList(e.getVideos().split(",")));
                e.setVideoList(vidList);
                e.setVideos(null);
            }
            if (StringUtils.isNotEmpty(e.getLabel())) {
                e.setLabelList(new ArrayList<>(Arrays.asList(e.getLabel().split(","))));
            }
        });
        return R.success("getReferPlaceList", placeList);
    }

    /**
     * 根据场地id获取场地详情
     *
     * @param id
     * @return
     */
    @GetMapping("getPlaceDetailById/{id}")
//    @Cacheable(value = "getPlaceDetailById", key = "'id_'+#id", cacheManager = "redisExpire")
    public R getPlaceDetailById(@PathVariable String id) {
        CPlace cPlace = icPlaceService.getById(id);
        if (StringUtils.isNotNull(cPlace)) {
            cPlace.setViews(cPlace.getViews() + 1);
            icPlaceService.updateById(cPlace);
            PlaceDetail placeDetail = icPlaceService.getDetailById(id);
            if (StringUtils.isNotEmpty(placeDetail.getLabel())) {
                placeDetail.setTags(new ArrayList<>(Arrays.asList(placeDetail.getLabel().split(","))));
            }
            if (StringUtils.isNotEmpty(placeDetail.getUsed())) {
                ArrayList<String> usedList = new ArrayList<>(Arrays.asList(placeDetail.getUsed().split(",")));
                ArrayList<Map> useds = new ArrayList<>();
                for (String used : usedList) {
                    HashMap<String, Object> usedMap = new HashMap<>(2);
                    usedMap.put("selected", false);
                    usedMap.put("used", used);
                    useds.add(usedMap);
                }
                placeDetail.setUseds(useds);
            }
            if (StringUtils.isNotEmpty(placeDetail.getImages())) {
                placeDetail.setImageList(new ArrayList<>(Arrays.asList(placeDetail.getImages().split(","))));
            }
            if ("0".equals(placeDetail.getDayType())) {
                placeDetail.setDayTypeStr("全天");
            } else if ("1".equals(placeDetail.getDayType())) {
                placeDetail.setDayTypeStr("白天");
            } else if ("2".equals(placeDetail.getDayType())) {
                placeDetail.setDayTypeStr("晚上");
            }
            if ("0".equals(placeDetail.getScheType())) {
                // 自动续期
                CSche sche = icScheService.getOneByPlaceId(id);
                if (sche != null) {
                    List<Map> scheMapList = new ArrayList<>();
                    if (sche.getDays() != null) {
                        for (int i = 0; i < sche.getDays(); i++) {
                            Date date = DateUtil.date();
                            Date offsetDay = DateUtil.offsetDay(date, i + 1);
                            Integer times = 0;
                            Object timesObj = redisCache.getCacheObject("date:" + DateUtil.formatDate(offsetDay) + ":" + id);
                            if (timesObj != null) {
                                times = Integer.parseInt(timesObj.toString());
                            }
                            Map s = new HashMap<>(5);
                            s.put("ytd", DateUtil.formatDate(offsetDay));
                            s.put("days", sche.getDays());
                            s.put("placeId", sche.getPlaceId());
                            s.put("surplus", sche.getSurplus() - times);
                            s.put("price", sche.getPrice());
                            scheMapList.add(s);
                        }
                        placeDetail.setSches(scheMapList);
                    }
                }

            } else {
                // 手动获取
                List<Map> sches = icScheService.getListByPlaceId(id);
                sches.forEach(e -> {
                    String date = e.get("ytd").toString();
                    Integer times = 0;
                    Object timesObj = redisCache.getCacheObject("date:" + date + ":" + id);
                    if (timesObj != null) {
                        times = Integer.parseInt(timesObj.toString());
                    }
                    e.put("surplus",  Integer.valueOf(e.get("surplus").toString()) - times);
                });
                placeDetail.setSches(sches);
            }
            return R.success("获取成功", placeDetail);
        }
        return R.error("查询失败");
    }

    /**
     * @return
     */
    @GetMapping("getPlaceCategoryList")
    @Cacheable(value = "getPlaceCategoryList", key = "'category'", cacheManager = "redisExpire")
    public R getPlaceCategoryList() {
        QueryWrapper<CCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("order_num");
        List<CCategory> list = icCategoryService.list(queryWrapper);
        return R.success(list);
    }

    @GetMapping("getPlaceByCategoryId/{categoryId}")
    @Cacheable(value = "getPlaceByCategoryId", key = "'id_'+#categoryId", cacheManager = "redisExpire")
    public R getPlaceByCategoryId(@PathVariable String categoryId) {
        if (StringUtils.isNotEmpty(categoryId)) {
            ArrayList<PlaceDto> placeList = icPlaceService.getPlaceByCategoryId(categoryId);
            placeList.forEach(e -> {
                if (StringUtils.isNotEmpty(e.getImages())) {
                    e.setImageList(Arrays.asList(e.getImages().split(",")));
                    e.setImages(null);
                }
                if (StringUtils.isNotEmpty(e.getVideos())) {
                    e.setVideoList(Arrays.asList(e.getVideos().split(",")));
                    e.setVideos(null);
                }
                if (StringUtils.isNotEmpty(e.getLabel())) {
                    e.setLabelList(new ArrayList<>(Arrays.asList(e.getLabel().split(","))));
                }
            });
            return R.success("查询成功", placeList);
        }
        return R.error("查询出错");
    }

    @ApiOperation(value = "获取个人中心")
    @GetMapping("getUserSpace/{uid}")
    @Cacheable(value = "getUserSpace", key = "'uid_'+#uid", cacheManager = "redisExpire")
    public R getUserSpace(@PathVariable String uid) {
        if (StringUtils.isNotEmpty(uid)) {
            UserSpaceInfo userSpaceInfo = new UserSpaceInfo();
            CUser user = icUserService.getById(uid);
            BeanUtils.copyProperties(user, userSpaceInfo);
            Map cradMap = icCardService.getCountByUid(uid);
            Integer follows = icCardService.getFollowsCountByUid(uid);
            userSpaceInfo.setCards(Integer.valueOf(cradMap == null ? "0" : cradMap.get("cards").toString()));
            userSpaceInfo.setFans(Integer.valueOf(cradMap == null ? "0" : cradMap.get("fans").toString()));
            userSpaceInfo.setFollows(follows);
            return R.success("查询成功", userSpaceInfo);
        }
        return R.error("查询出错");
    }

    /**
     * 获取帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getCardListByUid/{uid}/{pageNum}/{pageSize}")
    @Cacheable(value = "getCardListByUid", key = "'uid_'+#uid+'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getCardList(@PathVariable String uid, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        ArrayList<CardUserDto> cardList = icCardService.getCardListByUid(uid, pageNum, pageSize);
        if (StringUtils.isNotNull(cardList)) {
            cardList.forEach(e -> {
                if (StringUtils.isNotNull(e.getImgs())) {
                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                    e.setImgList(imgList);
                    e.setImgs(null);
                }
            });
        }
        return R.success("getCardList", cardList);
    }
    /**
     * 获取帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getTopicListByUid/{uid}/{pageNum}/{pageSize}")
    @Cacheable(value = "getTopicListByUid", key = "'uid_'+#uid+'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getTopicListByUid(@PathVariable String uid, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        List<TopicUserDto> topicByUid = icTopicService.getTopicByUid(uid, pageNum, pageSize);
        return R.success("topicList", topicByUid);
    }

    /**
     * 获取帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getActivityListByUid/{uid}/{pageNum}/{pageSize}")
    @Cacheable(value = "getActivityListByUid", key = "'uid_'+#uid+'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getActivityListByUid(@PathVariable String uid, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        List<ActivityDetail> activityByUid = icActivityService.getActivityByUid(uid, pageNum, pageSize);
        if (StringUtils.isNotNull(activityByUid)) {
            activityByUid.forEach(e -> {
                if (StringUtils.isNotNull(e.getImg())) {
                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImg().split(",")));
                    e.setImgList(imgList);
                    e.setImg(null);
                }
            });
        }
        return R.success("activityList", activityByUid);
    }


    /**
     * 获取话题列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getTopicList/{type}/{pageNum}/{pageSize}")
    @Cacheable(value = "getTopicList", key = "'type_'+#type+'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getTopicList(@PathVariable Integer type, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        ArrayList<TopicUserDto> topicList = icTopicService.getTopicList(type, pageNum, pageSize);
        return R.success("getTopicList", topicList);
    }

    @GetMapping("getTopicDetailById/{id}")
    @Cacheable(value = "getTopicDetailById", key = "'id_'+#id", cacheManager = "redisExpire")
    public R getTopicDetailById(@PathVariable String id) {
        CTopic cTopic = icTopicService.getById(id);
        if (StringUtils.isNotNull(cTopic)) {
            cTopic.setViews(cTopic.getViews() + 1);
            icTopicService.updateById(cTopic);
            TopicDetail topicDetail = icTopicService.getDetailById(id);
            return R.success("获取成功", topicDetail);
        }
        return R.error("查询失败");
    }


    /**
     * 根据话题查询热门帖子
     *
     * @param id
     * @return
     */
    @GetMapping("getHotCardByTopicId/{id}/{pageNum}/{pageSize}")
    @Cacheable(value = "getHotCardByTopicId", key = "'id_'+#id+'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getHotCardByTopicId(@PathVariable String id, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        List<CardUserDto> cardUserDtos = icCardService.getHotCard(id, pageNum, pageSize);
        if (cardUserDtos != null) {
            cardUserDtos.forEach(e -> {
                if (StringUtils.isNotNull(e.getImgs())) {
                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                    e.setImgList(imgList);
                    e.setImgs(null);
                }
                if (StringUtils.isNotNull(e.getVids())) {
                    ArrayList<String> vidList = new ArrayList<>(Arrays.asList(e.getVids().split(",")));
                    e.setVidList(vidList);
                    e.setVids(null);
                }
            });
        }
        return R.success("获取成功", cardUserDtos);
    }

    /**
     * 根据话题查询最新帖子
     *
     * @param id
     * @return
     */
    @GetMapping("getNewCardByTopicId/{id}/{pageNum}/{pageSize}")
    @Cacheable(value = "getNewCardByTopicId", key = "'id_'+#id+'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getNewCardByTopicId(@PathVariable String id, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        List<CardUserDto> cardUserDtos = icCardService.getNewCard(id, pageNum, pageSize);
        if (cardUserDtos != null) {
            cardUserDtos.forEach(e -> {
                if (StringUtils.isNotNull(e.getImgs())) {
                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                    e.setImgList(imgList);
                    e.setImgs(null);
                }
                if (StringUtils.isNotNull(e.getVids())) {
                    ArrayList<String> vidList = new ArrayList<>(Arrays.asList(e.getVids().split(",")));
                    e.setVidList(vidList);
                    e.setVids(null);
                }
            });
        }
        return R.success("获取成功", cardUserDtos);
    }

    /**
     * 查询话题封面
     *
     * @return
     */
    @GetMapping("getTopicSelect")
    @Cacheable(value = "getTopicSelect", cacheManager = "redisExpire")
    public R getTopicSelect() {
        Map select = new HashMap<String, Object>(2);
        ArrayList<TopicUserDto> topicBest = icTopicService.getTopicBest();
        ArrayList<TopicUserDto> topicSelect = icTopicService.getTopicSelect();
        select.put("best", topicBest);
        select.put("select", topicSelect);
        return R.success("查询成功", select);
    }

    /**
     * 查询话题轮播图
     *
     * @return
     */
    @GetMapping("getTopicSwiper")
    @Cacheable(value = "getTopicSwiper", cacheManager = "redisExpire")
    public R getTopicSwiper() {
        List<CSwiperTopic> topicList = icTopicService.getSwiper();
        return R.success("查询成功", topicList);
    }

    /**
     * 查询文档
     *
     * @return
     */
    @GetMapping("getDoc/{type}")
    @Cacheable(value = "getDoc", key = "'type_'+#type", cacheManager = "redisExpire")
    public R getDoc(@PathVariable Integer type) {
        QueryWrapper<CDoc> wrapper = new QueryWrapper<CDoc>();
        wrapper.eq("type", type);
        CDoc cDoc = icDocService.getOne(wrapper);
        return R.success(cDoc);
    }

    /**
     * 查询话题轮播图
     *
     * @return
     */
    @GetMapping("getTopicCover")
    @Cacheable(value = "getTopicCover", cacheManager = "redisExpire")
    public R getTopicCover() {
        List<TopicCover> topicList = icTopicCoverService.getCover();
        return R.success("查询成功", topicList);
    }

    /**
     * 查询话题用户列表
     *
     * @return
     */
    @GetMapping("getTopicUserList/{topicId}")
    @Cacheable(value = "getTopicUserList",key = "'topicId_'+#topicId", cacheManager = "redisExpire")
    public R getTopicUserList(@PathVariable String topicId){
        List<UserDetail> topicUserList = icUserService.getTopicUserList(topicId);
        return R.success("查询成功", topicUserList);
    }

    /**
     * 查询标签
     *
     * @return
     */
    @GetMapping("getTopicName/{type}")
    @Cacheable(value = "getTopicName", key = "'type_'+#type",cacheManager = "redisExpire")
    public R getTopicName(@PathVariable String type){
        QueryWrapper<CTopicCover> wrapper = new QueryWrapper();
        wrapper.eq("topic_type", type);
        wrapper.eq("deleted", 0);
        CTopicCover one = icTopicCoverService.getOne(wrapper);
        return R.success("查询成功", one);
    }

    /**
     * 话题图片上传
     */
    @PostMapping("/avatar")
    public R topicFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String avatar = FileUploadUtils.upload(RuoYiConfig.getCAvatarPath(), file);
            R r = R.success();
            r.put("imgUrl", avatar);
            return r;
        }
        return R.error("上传视频异常");
    }

    @GetMapping("getQrCode/{aid}")
    public R getQrCode(@PathVariable String aid) throws WxErrorException, IOException {
        return R.success("生成二维码成功",getQrcode("pages/activity-detail/detail","id="+aid));
    }

    /**
     * 获取二维码
     */
    private String getQrcode(String path, String scene) throws WxErrorException, IOException {
        final WxMaService wxService = WxMaConfiguration.getMaService(wxMineProperties.getAppId());
        WxMaCodeLineColor wxMaCodeLineColor = new WxMaCodeLineColor("138", "250", "178");
        File qrFile = wxService.getQrcodeService().createWxaCodeUnlimit(scene, path, 400, false, wxMaCodeLineColor, true);
        MultipartFile multipartFile = FrontNoCheckController.getMultipartFile(qrFile);
        String url = FileUploadUtils.upload(RuoYiConfig.getActivityPath(), multipartFile);
        return url;
    }

    // 第一种方式
    public static MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }

}
