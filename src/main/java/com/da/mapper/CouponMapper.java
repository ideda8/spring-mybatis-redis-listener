package com.da.mapper;

import com.da.po.Coupon;

import java.util.List;

public interface CouponMapper {

    public Coupon findCouponById(int id);

    public List<Coupon> findCouponByName(String name);

    public int insertCoupon(Coupon coupon);

    public int updateCoupon(Coupon coupon);

    public int deleteCoupon(int id);

}
