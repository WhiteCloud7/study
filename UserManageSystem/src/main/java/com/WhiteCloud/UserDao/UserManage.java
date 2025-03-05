package com.WhiteCloud.UserDao;
import java.util.List;
import com.WhiteCloud.UserEntity.userInfo;

public interface showUserDao {
	public List<userInfo> selectAll();
	public List<userInfo> selectByUsername(String username);
	public List<userInfo> selectByOtherInfo(String sex,String phone,String email);
}