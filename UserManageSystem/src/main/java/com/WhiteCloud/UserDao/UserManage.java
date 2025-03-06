package com.WhiteCloud.UserDao;
import java.util.List;
import com.WhiteCloud.UserEntity.userInfo;
import org.apache.ibatis.annotations.Param;

public interface UserManage {
	public List<userInfo> selectAll();
	public List<userInfo> selectByUsername(String username);
	public List<userInfo> selectByOtherInfo(@Param("sex") String sex,@Param("phone") String phone,@Param("email") String email);
}