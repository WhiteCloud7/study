package com.WhiteCloud.UserDao;
import java.util.List;
import com.WhiteCloud.UserEntity.userInfo;
import org.apache.ibatis.annotations.Param;

public interface UserManage {
	public List<userInfo> selectAll();
	public List<userInfo> selectOnePara(String para);
	public List<userInfo> selectByManyPara(@Param("ParaList") List<String> ParaList);
	public void addUser(userInfo paraList);
	public void deleteOneUser(int userId);
	public void resetMaxAutoKey();
	public void deleteManyUser(@Param("userIdSet")List<Integer> userIdSet);
	public void updateUserInfo(userInfo updateInfo);
}