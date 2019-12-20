package org.jcut.gmall.user.mapper;

import org.jcut.gmall.user.bean.UmsUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<UmsUser> {

    List<UmsUser> selectAllUser();
}
