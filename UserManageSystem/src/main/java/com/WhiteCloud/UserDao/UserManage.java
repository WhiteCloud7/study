package com.WhiteCloud.UserDao;
import java.util.List;
import com.WhiteCloud.UserEntity.userInfo;
import org.apache.ibatis.annotations.Param;

public interface UserManage {
	public List<userInfo> selectAll();
	public List<userInfo> selectOnePara(String para);
	public List<userInfo> selectByManyPara(List<String> ParaList);
}