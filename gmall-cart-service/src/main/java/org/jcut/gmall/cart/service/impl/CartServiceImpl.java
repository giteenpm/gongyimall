package org.jcut.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.bean.OmsCart;
import org.jcut.gmall.cart.mapper.CartMapper;
import org.jcut.gmall.service.CartService;
import org.jcut.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public OmsCart ifCartExistByUser(String uid, String pid) {
        OmsCart omsCart = new OmsCart();
        omsCart.setUid(uid);
        omsCart.setPid(pid);
        OmsCart omsCart1 = cartMapper.selectOne(omsCart);
        return omsCart1;
    }

    @Override
    public void addCart(OmsCart omsCartItem) {
        if(StringUtils.isNotBlank(omsCartItem.getUid())){
            cartMapper.insert(omsCartItem);
        }

    }

    @Override
    public void updateCart(OmsCart omsCartFromDb) {
        Example example = new Example(OmsCart.class);
        example.createCriteria().andEqualTo("id",omsCartFromDb.getId());
        cartMapper.updateByExampleSelective(omsCartFromDb,example);
    }

    @Override
    public void flushCartCache(String uid) {
        //查询当前用户的购物车列表数据
        OmsCart omsCartItem=new OmsCart();
        omsCartItem.setUid(uid);
        List<OmsCart> omsCartItems = cartMapper.select(omsCartItem);

        //同步缓存
        Jedis jedis = redisUtil.getJedis();

        Map<String,String> map=new HashMap<>();
        for (OmsCart cartItem : omsCartItems) {
            map.put(cartItem.getPid(), JSON.toJSONString(cartItem));
        }
        jedis.del("user:"+uid+":cart");
        jedis.hmset("user:"+uid+":cart",map);
        jedis.close();





    }

    @Override
    public List<OmsCart> cartList(String uid) {

        Jedis jedis=null;
        List<OmsCart> omsCartItems=new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();
            List<String> hvals = jedis.hvals("user:" + uid + ":cart");
            for (String hval : hvals) {
                OmsCart omsCart=JSON.parseObject(hval,OmsCart.class);
                omsCartItems.add(omsCart);

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }


        return omsCartItems;
    }

    @Override
    public void updateCartNum(int num, String id) {
        OmsCart omsCartItem=new OmsCart();
        omsCartItem.setNum(num);
        Example example = new Example(OmsCart.class);
        example.createCriteria().andEqualTo("id",id);
        cartMapper.updateByExampleSelective(omsCartItem,example);

    }

    @Override
    public void checkCart(OmsCart omsCart) {
        Example example = new Example(OmsCart.class);
        example.createCriteria().andEqualTo("uid",omsCart.getUid()).andEqualTo("id",omsCart.getId());
        cartMapper.updateByExampleSelective(omsCart,example);
        flushCartCache(omsCart.getUid());
    }

    //进入购物车列表，将该用户的checked字段都置为非选中状态
    @Override
    public void initCart(OmsCart omsCart) {

        Example example = new Example(OmsCart.class);
        example.createCriteria().andEqualTo("uid",omsCart.getUid());
        cartMapper.updateByExampleSelective(omsCart,example);

    }

    @Override
    public void delCart(String pid,String uid) {
        OmsCart omsCart=new OmsCart();
        omsCart.setPid(pid);
        omsCart.setUid(uid);
        cartMapper.delete(omsCart);
    }
}
