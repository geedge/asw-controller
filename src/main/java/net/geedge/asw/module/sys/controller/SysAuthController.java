package net.geedge.asw.module.sys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import net.geedge.asw.common.util.R;
import net.geedge.asw.common.util.RCode;
import net.geedge.asw.common.util.T;
import net.geedge.asw.module.sys.entity.SysUserEntity;
import net.geedge.asw.module.sys.service.ISysAuthService;

@RestController
@RequestMapping("/sys")
public class SysAuthController {

	@Autowired
	private ISysAuthService authService;

	record AuthRecord(String userName, String pwd) {}

	@PostMapping("/login")
	public R login(@RequestBody AuthRecord record) {
		T.VerifyUtil.is(record.userName()).notEmpty(RCode.SYS_USER_PWD_ERROR).and(record.pwd())
				.notEmpty(RCode.SYS_USER_PWD_ERROR);
		SysUserEntity userEntity = authService.login(record.userName(), record.pwd());
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		userEntity.setPwd(null);
		return R.ok().putData("tokenInfo", tokenInfo).putData("user", userEntity);
	}

	@RequestMapping("/logout")
	public R logout() {
		authService.logout();
		return R.ok();
	}

	@GetMapping("/permissions")
	public R permissions() {
		Map<String, Object> permissions = authService.userPermissions();
		return R.ok(permissions);
	}
}
