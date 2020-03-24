package org.jcut.gmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.bean.SmsStore;
import org.jcut.gmall.bean.UmsAdmin;
import org.jcut.gmall.bean.UmsReceiveAddress;
import org.jcut.gmall.bean.UmsUser;
import org.jcut.gmall.service.UserService;
import org.jcut.gmall.user.mapper.ReceiveAddressMapper;
import org.jcut.gmall.user.mapper.StoreMapper;
import org.jcut.gmall.user.mapper.AdminMapper;
import org.jcut.gmall.user.mapper.UserMapper;
import org.jcut.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ReceiveAddressMapper receiveAddressMapper;
    @Autowired
    StoreMapper storeMapper;
    @Autowired
    AdminMapper adminMapper;

    @Override
    public List<UmsUser> getAllUser() {
        List<UmsUser> umsUserList=userMapper.selectAll();
        return umsUserList;
    }

    /*
     * 查询当前用户的所以地址列表
     */
    @Override
    public List<UmsReceiveAddress> getReceiveAddresses(String uid) {
        UmsReceiveAddress t=new UmsReceiveAddress();
       t.setUid(uid);
        List<UmsReceiveAddress> umsReceiveAddresses=receiveAddressMapper.select(t);
        return umsReceiveAddresses;
    }
    /*
     * 查询指定地址id的地址列表
     */


    /*
    * 根据用户id查询用户信息
    * */
    @Override
    public UmsUser getSingleUser(String uid) {
        UmsUser t=new UmsUser();
        t.setUid(uid);
        UmsUser umsUser = userMapper.selectOne(t);
        return umsUser;
    }

    @Override
    public void deleteUserByUid(String uid) {
        UmsUser t=new UmsUser();
        t.setUid(uid);
        userMapper.delete(t);
    }

    /*
    * 修改用户信息
    * */
    @Override
    public void updataUser(UmsUser umsUser) {
        System.out.println(umsUser);
        /*
        * 先这样写，根据需求情况来更改这个函数，力求一个更改函数完成所以的更改修改
        * */
        String uid=umsUser.getUid();
        Date birthday = umsUser.getBirthday();

        String create_time = umsUser.getCreate_time();
        String email = umsUser.getEmail();
        String icon = umsUser.getIcon();
        String password = umsUser.getPassword();
        String phone = umsUser.getPhone();
        String sex = umsUser.getSex();
        String username = umsUser.getUsername();



        Example example=new Example(UmsUser.class);
        example.createCriteria().andEqualTo("uid",umsUser.getUid());
        userMapper.updateByExampleSelective(umsUser,example);

    }

    @Override
    public List<UmsUser> selectUserByXml() {
        return null;
    }

    @Override
    public UmsUser login(UmsUser umsUser) {
        //先从redis中查询，如果没有查询到结果，再从mysql中查询
        Jedis jedis=null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis!=null){
                String s = jedis.get("user:" + umsUser.getPassword() + ":info");
                if (StringUtils.isNotBlank(s)){
                    //账号密码正确
                    UmsUser umsUmsFromCache = JSON.parseObject(s, UmsUser.class);
                    return  umsUmsFromCache;
                }
            }
           UmsUser umsUmsFromDb= loginFromDb(umsUser);
            if (umsUmsFromDb!=null){
                jedis.setex("user:" + umsUser.getPassword() + ":info",60*60*24,JSON.toJSONString(umsUmsFromDb));
            }
            return umsUmsFromDb;


        }finally {
            jedis.close();
        }


    }

    @Override
    public void addUserToken(String token, String uid) {
        Jedis jedis=null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis!=null){
                jedis.setex("user:"+uid+":token",60*60*2,token);
            }
        }finally {
            jedis.close();
        }

    }

    //设置地址为默认
    @Override
    public void setDefaultAddress(String id) {
        //先将全部的地址设置为非默认地址，再将这个id的地址设置为默认地址
      UmsReceiveAddress umsReceiveAddress2=new UmsReceiveAddress();
      umsReceiveAddress2.setIsdefault("0");
      Example example2=new Example(UmsReceiveAddress.class);
      example2.createCriteria().andNotEqualTo("id",id);
      receiveAddressMapper.updateByExampleSelective(umsReceiveAddress2,example2);


        UmsReceiveAddress umsReceiveAddress = new UmsReceiveAddress();
        umsReceiveAddress.setIsdefault("1");
        Example example=new Example(UmsReceiveAddress.class);
        example.createCriteria().andEqualTo("id",id);
        receiveAddressMapper.updateByExampleSelective(umsReceiveAddress,example);


    }

    /*
     * 获取默认地址信息
     */
    @Override
    public UmsReceiveAddress getSingleAddress(String uid) {

        UmsReceiveAddress umsReceiveAddress=new UmsReceiveAddress();
//        umsReceiveAddress.setId(uid);
//        umsReceiveAddress.setIsdefault("1");
        Example example=new Example(UmsReceiveAddress.class);
        example.createCriteria().andEqualTo("uid",uid).andEqualTo("isdefault","1");
        UmsReceiveAddress umsReceiveAddress1 = receiveAddressMapper.selectOneByExample(example);
        return umsReceiveAddress1;
    }

    /**
     * 店铺管理员登录
     * @param smsStore
     * @return
     */
    @Override
    public SmsStore loginByAdmin(SmsStore smsStore) {
        SmsStore smsStore1 = storeMapper.selectOne(smsStore);
        if (smsStore1!=null){
            return smsStore1;
        }
        return null;

    }

    @Override
    public UmsAdmin loginByMallAdmin(UmsAdmin umsAdmin) {
        UmsAdmin umsAdmin1 = adminMapper.selectOne(umsAdmin);
        if (umsAdmin1!=null){
            return umsAdmin1;
        }
        return null;
    }

    @Override
    public void register(UmsUser umsUser) {
        userMapper.insertSelective(umsUser);
    }

    /**
     * 从数据库中查询用户信息
     * @param umsUser
     * @return
     */
    private UmsUser loginFromDb(UmsUser umsUser) {
       UmsUser umsUserFromDb = userMapper.selectOne(umsUser);
       if (umsUserFromDb!=null){
           return umsUserFromDb;
       }
       return null;

    }


}
