package org.jcut.gmall.order.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.annotations.LoginRequired;
import org.jcut.gmall.bean.*;
import org.jcut.gmall.service.CartService;
import org.jcut.gmall.service.OrderService;
import org.jcut.gmall.service.UserService;
import org.jcut.gmall.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
public class OrderController {
    @Reference
    UserService userService;
    @Reference
    CartService cartService;
    @Reference
    OrderService orderService;



    @RequestMapping("toTrade")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        return "toTrade";

    }

    @RequestMapping("confirmOrder")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public List<OmsCart> confirmOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String uid = (String) request.getAttribute("uid");

//        //将地址列表存储到集合列表中
//        List<UmsReceiveAddress> umsReceiveAddressList=userService.getReceiveAdddresses(uid);

        //将购物车中选中的商品封装到集合列表中
        List<OmsCart> omsCartList = cartService.cartList(uid);
        //商品结算页只是对购物车中商品信息的确定，可以重新封装个check=1的集合对象
        List<OmsCart> omsCartItems = new ArrayList<>();
        for (OmsCart omsCart : omsCartList) {
            if (omsCart.getIscheck().equals("1")) {
                OmsCart omsItem = new OmsCart();
                omsItem.setPimg(omsCart.getPimg());
                omsItem.setPname(omsCart.getPname());
                omsItem.setPdesc(omsCart.getPdesc());
                omsItem.setPrice(omsCart.getPrice());
                omsCart.setNum(omsCart.getNum());
                omsCartItems.add(omsCart);

            }
        }
        return omsCartItems;
    }

    //提交订单
    @RequestMapping("submitOrder")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    //id为默认地址的id
    public void submitOrder( HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        String uid = (String) request.getAttribute("uid");
        UmsReceiveAddress umsReceiveAddress = userService.getSingleAddress(uid);
        //根据用户id获取用户购物车中选中的商品列表
       List<OmsCart>omsCartItems= cartService.cartList(uid);
       //定义一个购物车集合用来存储选中结算的购物车详细信息
       List<OmsCart> omsCartList=new ArrayList<>();
        for (OmsCart omsCartItem : omsCartItems) {
            if (omsCartItem.getIscheck().equals("1")){
               omsCartList.add(omsCartItem);

            }
        }
        //根据店铺id将订单集合转换成map集合对象
        Map<String,List<OmsCart>> collects = omsCartList.stream().collect(Collectors.groupingBy(OmsCart::getSid));
        System.out.println(collects);
        for (String s : collects.keySet()) {
         //  System.out.println("key : "+s+" value : "+collects.get(s));
         // 调用创建订单的方法
           createOrder(s,collects.get(s),uid,umsReceiveAddress);
        }


    }

    //产生订单,不同店铺的商品产生不同的订单
    private void createOrder(String s, List<OmsCart> omsCarts,String uid,UmsReceiveAddress umsReceiveAddress) {
        //存储每个订单的商品详情信息
        List<OmsOrderDetail> omsOrderDetailList=new ArrayList<>();
        //订单对象
        OmsOrder omsOrder = new OmsOrder();
        //订单id
       String oid= UUID.randomUUID().toString();
        omsOrder.setOid(oid);
        omsOrder.setUid(uid);
        omsOrder.setSid(s);
        //计算单个店铺的订单总价
        Double sumPrice=calSumPriceByStore(omsCarts);
        omsOrder.setMoney(sumPrice);
        //订单生成时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String create_time = dateFormat.format(date);
        omsOrder.setCreate_time(create_time);
        omsOrder.setReceive_name(umsReceiveAddress.getReceive_name());
        omsOrder.setReceive_phone(umsReceiveAddress.getReceive_phone());
        omsOrder.setPost_code(umsReceiveAddress.getPost_code());
        omsOrder.setProvince(umsReceiveAddress.getProvince());
        omsOrder.setCity(umsReceiveAddress.getCity());
        omsOrder.setRegion(umsReceiveAddress.getRegion());
        omsOrder.setDetail_address(umsReceiveAddress.getDetail_address());
        omsOrder.setOrder_status("5");
        for (OmsCart omsCart : omsCarts) {
            if (omsCart.getIscheck().equals("1")){
                OmsOrderDetail omsOrderDetail = new OmsOrderDetail();
                //使用uuid生成随机订单号
                String id = UUID.randomUUID().toString();
                omsOrderDetail.setId(id);
                omsOrderDetail.setPid(omsCart.getPid());
                omsOrderDetail.setPrice(omsCart.getPrice());
                omsOrderDetail.setPdesc(omsCart.getPdesc());
                omsOrderDetail.setOid(omsOrder.getOid());
                omsOrderDetail.setNum(omsCart.getNum());
                omsOrderDetail.setImg(omsCart.getPimg());
                omsOrderDetailList.add(omsOrderDetail);
            }
        }
        omsOrder.setOmsOrderItems(omsOrderDetailList);
       orderService.createOrder(omsOrder);

    }

    private Double calSumPriceByStore(List<OmsCart> omsCarts) {
        double sumprice=0.0;
        for (OmsCart omsCart : omsCarts) {
            String ischeck = omsCart.getIscheck();
            if(ischeck.equals("1")){
                sumprice+=omsCart.getPrice()*omsCart.getNum();
            }
        }
        return sumprice;
    }

    @RequestMapping("getOrderDetailByUser")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public List<OmsOrder> getOrderDetailByUser(HttpServletRequest request, HttpServletResponse response){
        String uid = (String) request.getAttribute("uid");
      List<OmsOrder> omsOrderList= orderService.getOrderDetailByUser(uid);
      System.out.print(omsOrderList);
        return omsOrderList;
    }

    /**
     * 店铺管理员登录态下查看本店所有订单
     * @param request
     * @return
     */
    @RequestMapping("getOrderByAdmin")
    @ResponseBody
    public List<OmsOrder> getOrderByAdmin(HttpServletRequest request) {
        String sid = CookieUtil.checkAdmin(request);
        if (StringUtils.isBlank(sid)) {
            return null;
        }
      List<OmsOrder> omsOrderList= orderService.getOrderByAdmin(sid);
        return omsOrderList;
    }

    /**
     * 查询店铺没有发货的订单
     * @param request
     * @return
     */
    @RequestMapping("getOrderNoSend")
    @ResponseBody
    public List<OmsOrder> getOrderNoSend(HttpServletRequest request){
        String sid = CookieUtil.checkAdmin(request);
        if (StringUtils.isBlank(sid)) {
            return null;
        }
        List<OmsOrder> omsOrderList= orderService.getOrderNoSend(sid);
        return omsOrderList;
    }

    /**
     * 用户支付成功，将该用户订单状态为5的改为1，即将订单状态改为待发货状态
     */
    @RequestMapping("paySuccess")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public void paySuccess(HttpServletRequest request){
        String uid = (String) request.getAttribute("uid");
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setUid(uid);
        orderService.updateOrderState(omsOrder);
    }

    /**
     * 进入订单列表后，查询是否订单状态还有为“5”的，如果有5，将5改为0，即未付款状态
     * @param request
     */
    @RequestMapping("updateOrderStatusZero")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public void updateOrderStatusZero(HttpServletRequest request){
        String uid = (String) request.getAttribute("uid");
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setUid(uid);
        orderService.updateOrderStatusZero(omsOrder);
    }

    /**
     * 管理员处理订单发货
     * @param
     */
    @RequestMapping("sendOrder")
    @ResponseBody
    public void sendOrder(String oid){
        orderService.sendOrder(oid);
    }







}
