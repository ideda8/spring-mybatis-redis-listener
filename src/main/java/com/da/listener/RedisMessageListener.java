package com.da.listener;

import com.da.mapper.CouponMapper;
import com.da.po.Coupon;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageListener implements MessageListener {

    @Autowired
    private CouponMapper couponMapper;

    public static Logger log = LogManager.getLogger(RedisMessageListener.class.getName());

    public void onMessage(Message message, byte[] bytes) {
        log.info("-----------------------------listener-------------------------------");
        System.out.println("-----------------------------listener-------------------------------");
        String key = new String(message.getBody());
        if(key.startsWith("coupon")){
            String id = key.split(":")[1];
            Coupon coupon = couponMapper.findCouponById(Integer.parseInt(id));
            coupon.setState(1);
            couponMapper.updateCoupon(coupon);
        }
    }
}
