package net.geedge.asw.module.sys.service;

import java.util.Map;

import net.geedge.asw.module.sys.entity.SysUserEntity;

public interface ISysAuthService{

	public SysUserEntity login(String userName, String pwd);
	
	public void logout();

	public Map<String, Object> userPermissions();
	
}
