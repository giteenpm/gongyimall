package org.jcut.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.bean.PmsProductInfo;
import org.jcut.gmall.manage.mapper.ProductMapper;
import org.jcut.gmall.service.ProductService;
import org.jcut.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductMapper productMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<PmsProductInfo> getAllProduct() {
        List<PmsProductInfo> pmsProductList = productMapper.selectAll();
        return pmsProductList;
    }

    @Override
    public void saveProduct(PmsProductInfo pmsProductInfo) {
        productMapper.insertSelective(pmsProductInfo);

    }

    public PmsProductInfo getProductOneByDb(String pid){
        PmsProductInfo t=new PmsProductInfo();
        t.setPid(pid);
        PmsProductInfo pmsProductInfo = productMapper.selectOne(t);
        return pmsProductInfo;
    }
    /*
    使用redis查询一个记录
     */
    @Override
    public PmsProductInfo getProductOne(String pid) {
        PmsProductInfo pmsProductInfo=new PmsProductInfo();
        //1.连接缓存
        Jedis jedis = redisUtil.getJedis();
        //2.查询缓存
        String stuKey="stu:"+pid+":info";
        String skuJson = jedis.get(stuKey);
        if(StringUtils.isNotBlank(skuJson)){
            pmsProductInfo = JSON.parseObject(skuJson, PmsProductInfo.class);
        }else {
            //3.如果缓存不存在数据就查询数据库
            //设置分布式锁
            //注意这块的v的值，这个是为了解决第一个线程中key已经过期了，但这个请求还没结束，等到这个请求结束以后，回来删除锁，之前的锁已经过期了，所以删除的是第二个人设置的锁吗，这事就得依靠这个value来解决
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + pid + ":lock", token, "nx", "px", 10*1000);
           if (StringUtils.isNotBlank(OK)&&OK.equals("OK")){
               pmsProductInfo = getProductOneByDb(pid);
               //4.如果查询结果不为空，将mysql数据存入到redis
               if (pmsProductInfo!=null){
                   jedis.set("sku:"+pid+":info",JSON.toJSONString(pmsProductInfo));
               }else{
                   //5.数据库中同样没有这条数据，为了防止缓存穿透，将null值或空字符串设置给redis
                   jedis.setex("sku:"+pid+":info",60*3,JSON.toJSONString(""));
               }
               //访问完mysql后，释放分布式锁
               //释放时，要先判断是自己的锁
               String redisToken = jedis.get("sku:" + pid + ":lock");
               if(StringUtils.isNotBlank(redisToken)&&redisToken.equals(token)){
                   jedis.del("sku:" + pid + ":lock");
               }

           }else {
               //设置分布式锁失败，则自旋（该线程睡眠几秒后和重新访问本方法
               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               //注意这种写法才叫自旋，而不是直接调用
               return  getProductOne(pid);

           }



        }

        jedis.close();

        return pmsProductInfo;
    }

    @Override
    public List<PmsProductInfo> getProductRandom() {
       List<PmsProductInfo> productInfoList= productMapper.selectProductRandom();
        return productInfoList;
    }

    @Override
    public List<PmsProductInfo> getProductBySid(String sid) {
        PmsProductInfo pmsProductInfo=new PmsProductInfo();
        pmsProductInfo.setSid(sid);
        List<PmsProductInfo> productInfos = productMapper.select(pmsProductInfo);
        return productInfos;
    }


}
