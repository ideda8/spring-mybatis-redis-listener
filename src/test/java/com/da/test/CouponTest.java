package com.da.test;

import com.da.mapper.CouponMapper;
import com.da.po.Coupon;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class CouponTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void bef() throws Exception{
        //获得主配置文件流信息
        InputStream is = Resources.getResourceAsStream("sqlMapConfig.xml");
        //获取SqlSessionFaction工厂对象
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    }

    @Test
    public void findCouponById() throws Exception{

        //通过获取SqlSessionFaction工厂对象工厂生产SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //使用SqlSession对象来生成接口代理对象
        CouponMapper couponMapper = sqlSession.getMapper(CouponMapper.class);

        //调用mapper中的方法
        Coupon coupon = couponMapper.findCouponById(6);

        System.out.println(coupon);

        sqlSession.close();
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void insertCoupon() throws Exception{

        //通过获取SqlSessionFaction工厂对象工厂生产SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //使用SqlSession对象来生成接口代理对象
        CouponMapper couponMapper = sqlSession.getMapper(CouponMapper.class);

        Date date = new Date();
        int timeOut = 1;

        Coupon c = new Coupon();
        c.setName("20元优惠券");
        c.setMoney(20.0);
        c.setCouponDesc("优惠20元");
        c.setCreateTime(date);
        c.setExpireTime(addDate(date, 2, timeOut));
        c.setState(0);

        //调用mapper中的方法
        int i = couponMapper.insertCoupon(c);

        sqlSession.commit();
        System.out.println(i + " ID: " + c.getId());

        sqlSession.close();

        redisTemplate.opsForValue().set("coupon:" + c.getId(), c.getId()+"" ,timeOut, TimeUnit.MINUTES);

    }

    /**
     * 加减时间
     * @param date 需要加减的时间
     * @param cal 1为秒，2为分，3为小时，4为天，5为月，6为年
     * @param num 加减的数量
     * @return 加减后的时间
     */
    public static Date addDate(Date date, Integer cal, Integer num){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        if(cal == 1){
            //秒
            rightNow.add(Calendar.SECOND, num);
        }else if(cal == 2){
            //分
            rightNow.add(Calendar.MINUTE, num);
        }else if(cal == 3){
            //小时
            rightNow.add(Calendar.HOUR, num);
        }else if(cal == 4){
            //天
            rightNow.add(Calendar.DATE, num);
        }else if(cal == 5){
            //月
            rightNow.add(Calendar.MONTH, num);
        }else if(cal == 6){
            //年
            rightNow.add(Calendar.YEAR, num);
        }
        return rightNow.getTime();
    }



}
