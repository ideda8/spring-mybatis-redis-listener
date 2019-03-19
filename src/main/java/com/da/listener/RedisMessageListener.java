package com.da.listener;

import com.da.mapper.CouponMapper;
import com.da.po.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageListener implements MessageListener {

    @Autowired
    private CouponMapper couponMapper;

    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody());
        if(key.startsWith("coupon")){
            String id = key.split(":")[1];
            Coupon coupon = couponMapper.findCouponById(Integer.parseInt(id));
            coupon.setState(1);
            couponMapper.updateCoupon(coupon);
        }
    }
}
