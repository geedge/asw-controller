package net.geedge.asw.common.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;

@Configuration(proxyBeanMethods = false)
public class SaTokenConfigure implements WebMvcConfigurer {

	/**
	 * satoken timeout
	 * key: satoken.timeout
	 * 单位：分钟
	 * 默认：30天
	 */
	@Value("${satoken.timeout:#{24 * 60 * 30}}")
	private Long timeout;

	// Sa-Token 参数配置，参考文档：https://sa-token.cc
	// 此配置会与 application.yml 中的配置合并 （代码配置优先）
	@Autowired
	public void configSaToken(SaTokenConfig config) {
		config.setTokenName("satoken"); // token 名称（同时也是 cookie 名称）
		Long satokenTimeout = this.timeout == -1L ? -1L : TimeUnit.MINUTES.toSeconds(this.timeout);
		config.setTimeout(satokenTimeout); // token 有效期（单位：秒），默认30天，-1代表永不过期
		config.setActiveTimeout(-1); // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
		config.setIsConcurrent(true); // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
		config.setIsShare(true); // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
		config.setTokenStyle("simple-uuid"); // token 风格
		config.setIsLog(false); // 是否输出操作日志
		config.setIsPrint(false);
//		config.setIsReadCookie(false);
	}

	// 注册 Sa-Token 拦截器，打开注解式鉴权功能
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册 Sa-Token 拦截器，打开注解式鉴权功能
		registry.addInterceptor(new SaInterceptor(handler -> {
			SaRouter.match("/file/**").notMatch("/file/content/*").check(r -> StpUtil.checkLogin());
			SaRouter.match("/sys/**").notMatch("/sys/login").check(r -> StpUtil.checkLogin());
		})).addPathPatterns("/**");
	}
}
