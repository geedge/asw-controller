package net.geedge.asw.module.sys.controller;

import cn.dev33.satoken.stp.StpUtil;

public abstract class BaseController {

	protected String getSysUserId() {
		return StpUtil.getLoginIdAsString();
	}
	
}
