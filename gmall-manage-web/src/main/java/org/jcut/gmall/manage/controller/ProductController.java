package org.jcut.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.annotations.LoginRequired;
import org.jcut.gmall.bean.PmsProductInfo;
import org.jcut.gmall.manage.util.PmsUploadUtil;
import org.jcut.gmall.service.ProductService;
import org.jcut.gmall.util.AESUtil;
import org.jcut.gmall.util.CookieUtil;
import org.jcut.gmall.util.HttpclientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class ProductController {
    @Reference
    ProductService productService;


    @RequestMapping("getAllProduct")
    @ResponseBody
    public List<PmsProductInfo> getAllProduct() {
        List<PmsProductInfo> pmsProductList = productService.getAllProduct();
        return pmsProductList;
    }

    /*
     * 上传文件方法
     * */
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) {
        //将图片或音视频上传都分布式文件存储服务器
        System.out.println(multipartFile);
        //将图片的路径返回给页面
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }

    /*
     * 管理员添加商品方法
     * */
    @RequestMapping("saveProduct")
    @ResponseBody
    public String saveProduct(PmsProductInfo pmsProductInfo, HttpServletRequest request) {
        String sid= CookieUtil.checkAdmin(request);
        if (StringUtils.isBlank(sid)){
            return "fail";
        }
        System.out.println(sid);
        /*
        以下四个属性非ajax传过来的，所以在调用service时，将这个对象属性补全
         */
        //使用uuid生成随即的数
        String pid = UUID.randomUUID().toString();
        pmsProductInfo.setPid(pid);

        //店铺id是在店铺管理员登录以后的会话里获得的，这里先用随机数来代替。
//        String sid = UUID.randomUUID().toString();
        pmsProductInfo.setSid(sid);
        //商家日期
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String create_time = dateFormat.format(date);
        pmsProductInfo.setCreate_time(create_time);
        //商品状态默认为1
        pmsProductInfo.setStatus(1);
        pmsProductInfo.setSale_num(0);
        //以下是几个需要转换类型的参数
        productService.saveProduct(pmsProductInfo);
        return "success";
    }

//    /**
//     * 验证店铺管理员是否登录
//     * @param request
//     */
//    private String checkAdmin(HttpServletRequest request) {
//        String storeInfo = CookieUtil.getCookieValue(request, "storeInfo", true);
//        try {
//
//            String content = AESUtil.decrypt(storeInfo, "key_value_length");
//            if (StringUtils.isNotBlank(content)){
//                String[] split = content.split(",");
//                String sid=split[0];
//                System.out.println(sid);
//                return sid;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    //随机查询数据用于商城主页的数据显示
    @RequestMapping("getProductRandom")
    @ResponseBody
    public List<PmsProductInfo> getProductRandom(){
        List<PmsProductInfo> pmsProductList=productService.getProductRandom();
        return pmsProductList;
    }

    @RequestMapping("toIndex")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public void toIndex(){

    }


    /*
        查询该店铺商品
     */
    @RequestMapping("getProductBySid")
    @ResponseBody
    public List<PmsProductInfo> getProductBySid(HttpServletRequest request){
        String sid = CookieUtil.checkAdmin(request);
        if (sid.equals("")){
          return null;
        }
       List<PmsProductInfo> productInfoList= productService.getProductBySid(sid);
        return productInfoList;
    }
}
