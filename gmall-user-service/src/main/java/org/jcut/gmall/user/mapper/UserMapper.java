package org.jcut.gmall.user.mapper;
import org.jcut.gmall.bean.UmsUser;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;


public interface UserMapper extends Mapper<UmsUser> {

    List<UmsUser> selectAllUser();
}
