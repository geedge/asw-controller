package net.geedge.asw.common.config.exception;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.log.Log;
import jakarta.servlet.http.HttpServletRequest;
import net.geedge.asw.common.util.ASWException;
import net.geedge.asw.common.util.R;
import net.geedge.asw.common.util.RCode;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class ASWExceptionHandler {

	private static final Log log = Log.get();

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(ASWException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public R handleDHException(ASWException e, HttpServletRequest request) {
		log.warn(e, "Request uri: {}", request.getRequestURI());
		return R.error(e.getCode(), e.getMsg());
	}

	/**
	 * 用户未登录
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(NotLoginException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public R handleNotLoginException(NotLoginException e, HttpServletRequest request) {
		log.warn(e, "Request uri: {}", request.getRequestURI());
		return R.error(RCode.USER_NO_LOGIN);
	}
	
	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public R handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
		log.error(e, "Request uri: {}", request.getRequestURI());
		return R.error(RCode.SYS_DUPLICATE_RECORD);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleException(Exception e, HttpServletRequest request) {
		if (e instanceof ClientAbortException) {
			return null;
		}
		log.error(e, "Request uri: {}", request.getRequestURI());
		return R.error().put("msg", e.getMessage());
	}
	
}