package com.yunchuang.artco.controoler.getCourse;

import com.yunchuang.artco.dao.HistoryTodayMapper;
import com.yunchuang.artco.domain.HistoryToday;
import com.yunchuang.artco.util.RedisUtil;
import com.yunchuang.artco.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
@SuppressWarnings("all")
@Api(value="历史的今天",tags={"历史的今天接口"})
public class GetCourse {
    @Autowired
    private HistoryTodayMapper historyTodayMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @RequestMapping("/getDataByDate")
    @ResponseBody
    @ApiOperation(value="根据你搜索的日期查找",notes="",httpMethod="GET")
    @ApiImplicitParams({@ApiImplicitParam(name="date",value="日期,如2020-xx-xx或者2020/xx/xx",dataType="String"),
            @ApiImplicitParam(name="page",value="第几页",dataType="Integer"),
            @ApiImplicitParam(name="limit",value="多少条数据",dataType="Integer")
    })
    @Cacheable(value = "#date")
    public ResponseResult getDataByDate(@RequestParam String date,
                                        @RequestParam Integer page,
                                        @RequestParam Integer limit
    ) {
        System.out.println("缓存");
        //判断时间是否为空 若为空返回今天的数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date==null){
                 Date  date1 = new Date();
                date =  simpleDateFormat.format(date1);
        }else {
            date = date.replace("/","-");
        }
        if(page==null)page=1;
        if(limit==null)limit=10;
        if(redisTemplate.opsForList().range(date,page*limit-limit,limit*page).size()==0){
            Calendar calendar = new GregorianCalendar();
            Date parse =null;
            try {
                 parse = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
               return ResponseResult.error("日期格式不对");
            }
            calendar.setTime(parse);
            int i = calendar.get(Calendar.MONTH)+1;
            Set<String> keys = redisTemplate.keys("*");
            redisTemplate.delete(keys);
            ArrayList<HistoryToday> oneMonth = historyTodayMapper.getOneMonth(i);
                Runnable runnable =(() -> {
                for(int j = 0;j<oneMonth.size();j++){
                    String format = simpleDateFormat.format(oneMonth.get(j).getDate());
                    redisTemplate.opsForList().leftPush(format,oneMonth.get(j));
                }
            });
                RedisUtil redisUtil = new RedisUtil(redisTemplate);
            LinkedBlockingDeque linkedBlockingQuene = new LinkedBlockingDeque();
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                    30,
                    30,
                    TimeUnit.SECONDS,linkedBlockingQuene);
            threadPoolExecutor.execute(runnable);
            for(int j =25;j<oneMonth.size();j++){
                String format = simpleDateFormat.format(oneMonth.get(j).getDate());
                redisTemplate.opsForList().leftPush(format,oneMonth.get(j));
            }
        }
        List<Object> o =redisTemplate.opsForList().range(date,page*limit-limit,limit*page);
        return ResponseResult.success("查询成功",redisTemplate.opsForList().range(date,page*limit-limit,limit*page));
    }

    @RequestMapping("/getGuessLikeList")
    @ResponseBody
    @ApiOperation(value="根据你搜索的内容查找",notes="",httpMethod="GET")
    @ApiImplicitParams({@ApiImplicitParam(name="text",value="搜索内容",dataType="String"),
            @ApiImplicitParam(name="page",value="第几页",dataType="Integer"),
            @ApiImplicitParam(name="limit",value="多少条数据",dataType="Integer")
    })

    public ResponseResult getGuessLikeList(@RequestParam String text,
                                           @RequestParam Integer page,
                                           @RequestParam Integer limit
                                           ){
        page=page*limit-limit+1;
        ArrayList<HistoryToday> historyTodays = historyTodayMapper.GuessLike(text,page,limit);

        return ResponseResult.success("成功",historyTodays);
    }
}
