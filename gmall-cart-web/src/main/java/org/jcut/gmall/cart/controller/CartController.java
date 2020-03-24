package org.jcut.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.annotations.LoginRequired;
import org.jcut.gmall.bean.OmsCart;
import org.jcut.gmall.bean.PmsProductInfo;
import org.jcut.gmall.service.CartService;
import org.jcut.gmall.service.ProductService;
import org.jcut.gmall.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class CartController {
    @Reference
    ProductService productService;
    @Reference
    CartService cartService;

    @RequestMapping("cartList")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public List<OmsCart> cartList(HttpServletRequest request, HttpServletResponse response) {
        List<OmsCart> omsCartItems=new ArrayList<>();
        String uid = (String)request.getAttribute("uid");
        if(StringUtils.isNotBlank(uid)){
            omsCartItems=cartService.cartList(uid);
        }else{
            //没有登录，查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                omsCartItems=JSON.parseArray(cartListCookie,OmsCart.class);
            }
        }
        return omsCartItems;
    }

    @RequestMapping("addToCart")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public void addToCart(String pid, int num, HttpServletRequest request, HttpServletResponse response) {
        List<OmsCart> omsCartList = new ArrayList<>();
        //查询商品名为pid的商品
        PmsProductInfo pmsProductInfo = productService.getProductOne(pid);

        //封装购物车对象
        OmsCart omsCartItem = new OmsCart();
        String id= UUID.randomUUID().toString();
        omsCartItem.setId(id);
        omsCartItem.setPid(pmsProductInfo.getPid());
        omsCartItem.setSid(pmsProductInfo.getSid());
        omsCartItem.setNum(num);
        omsCartItem.setPrice(pmsProductInfo.getLprice());
        omsCartItem.setPimg(pmsProductInfo.getPimg());
        omsCartItem.setPname(pmsProductInfo.getPname());
        omsCartItem.setPdesc(pmsProductInfo.getPdesc());
        omsCartItem.setIscheck("0");

        //得到当前用户的uid
        String uid = (String)request.getAttribute("uid");
        if (StringUtils.isBlank(uid)) {
            //用户没有登录，将购物车对象存入到cookie
            //先判断cookie是否存在
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            //如果不存在cookie，则直接添加，如果存在cookie，就更新cookie中的购物车对象数量和价格
            if(StringUtils.isBlank(cartListCookie)){
                omsCartList.add(omsCartItem);
            }else{
                //cookie不为空
                omsCartList = JSON.parseArray(cartListCookie, OmsCart.class);
                //判断cookie的list里是否存在之前添加过的商品
                boolean exist=if_cart_exist(omsCartList,omsCartItem);
                if(exist){
                    for (OmsCart omsCart : omsCartList) {
                        if(omsCart.getPid().equals(omsCartItem.getPid())){
                            omsCart.setNum(omsCart.getNum()+omsCartItem.getNum()
                            );
                        }
                    }
                }else{
                    omsCartList.add(omsCartItem);
                }
            }
//            omsCartList.add(omsCartItem);
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartList), 60 * 60 * 72, true);

        }else{
            //如果登录，则将购物车记录存进DB，然后同步redis缓存
           OmsCart omsCartFromDb = cartService.ifCartExistByUser(uid,pid);
           if(omsCartFromDb==null){
               //数据库不存在数据
               omsCartItem.setUid(uid);
                cartService.addCart(omsCartItem);
           }else{
               //数据库中存在此数据，所以只进行数量的增加
               omsCartFromDb.setNum(omsCartFromDb.getNum()+num);
               cartService.updateCart(omsCartFromDb);
           }
            // 同步缓存
            cartService.flushCartCache(uid);

        }

    }

    /**
     * 用户登录同步购物车中数据至DB和redis缓存
     * @return
     */
    @RequestMapping("sychronizeCart")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public String sychronizeCart(HttpServletRequest request, HttpServletResponse response){
        String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
        if(StringUtils.isBlank(cartListCookie)){
            return "success";
        }
        //得到cookie中的购物车列表
        List<OmsCart> omsCartList = JSON.parseArray(cartListCookie, OmsCart.class);
        //将omsCartList同步到MYSQL.
        String uid = (String)request.getAttribute("uid");
        //查询出该用户的购物车列表，如果cookie中的商品已经存在于mysql,那么则只将mysql中商品数量增加，否则直接加入
        for (OmsCart omsCartItem : omsCartList) {
            OmsCart omsCartFromDb = cartService.ifCartExistByUser(uid,omsCartItem.getPid());
            if(omsCartFromDb==null){
                //数据库不存在数据
                omsCartItem.setUid(uid);
                omsCartItem.setIscheck("1");
                cartService.addCart(omsCartItem);
            }else{
                //数据库中存在此数据，所以只进行数量的增加
                omsCartFromDb.setNum(omsCartFromDb.getNum()+omsCartItem.getNum());
                omsCartFromDb.setIscheck("1");
                cartService.updateCart(omsCartFromDb);
            }

        }
        // 同步缓存
        cartService.flushCartCache(uid);
        return "success";
    }

    private boolean if_cart_exist(List<OmsCart> omsCartList, OmsCart omsCartItem) {
        boolean b=false;
        for (OmsCart omsCart : omsCartList) {
            if(omsCart.getPid().equals(omsCartItem.getPid())){
                b=true;
            }
        }
        return b;
    }

    @RequestMapping("updateCartNum")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public void updateCartNum(int num,String id,HttpServletRequest request, HttpServletResponse response) {
        String uid = (String)request.getAttribute("uid");
        cartService.updateCartNum(num,id);
        cartService.flushCartCache(uid);

    }

    @RequestMapping("checkCart")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public void checkCart(String isChecked,String id,HttpServletRequest request, HttpServletResponse response){
        String uid = (String)request.getAttribute("uid");
        OmsCart omsCart=new OmsCart();
        omsCart.setIscheck(isChecked);
        omsCart.setId(id);
        omsCart.setUid(uid);
        cartService.checkCart(omsCart);
    }

    @RequestMapping("sumPrice")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public double sumPrice(HttpServletRequest request, HttpServletResponse response){
        String uid = (String)request.getAttribute("uid");
        List<OmsCart> omsCartList = cartService.cartList(uid);
        double sumprice=0.0;
        for (OmsCart omsCart : omsCartList) {
            String ischeck = omsCart.getIscheck();
            System.out.println(ischeck);
            if(ischeck.equals("1")){
                System.out.println("xxx");
               sumprice+=omsCart.getPrice()*omsCart.getNum();
            }
        }
        return sumprice;
    }


    //初始化购物车的checked字段
    @RequestMapping("initCart")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public void initCart(HttpServletRequest request, HttpServletResponse response){
        String uid = (String)request.getAttribute("uid");
      OmsCart omsCart=new OmsCart();
      omsCart.setUid(uid);
      omsCart.setIscheck("0");
      cartService.initCart(omsCart);
    }

    //结算方法



}

