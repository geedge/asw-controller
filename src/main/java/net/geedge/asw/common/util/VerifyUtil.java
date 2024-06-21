package net.geedge.asw.common.util;

import java.util.Collection;
import java.util.Map;

import cn.hutool.core.lang.Validator;

/**
 * ValidateUtil
 */
public class VerifyUtil {

	private Object value;

	protected VerifyUtil(Object value) {
		this.value = value;
	}

	/**
	 * 新建校验实例，传入目标对象
	 *
	 * @param value 校验对象
	 * @return ValidateUtils
	 */
	public static VerifyUtil is(Object value) {
		return new VerifyUtil(value);
	}

	/**
	 * 切换目标对象，不重新创建实例
	 *
	 * @param value 校验对象
	 * @return ValidateUtils
	 */
	public VerifyUtil and(Object value) {
		this.value = value;
		return this;
	}

	/**
	 * 非空校验
	 *
	 * @return ValidateUtils
	 */
	public VerifyUtil notNull() {
		return notNull(null);
	}

	/**
	 * 非空校验
	 *
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil notNull(RCode code) {
		if (T.ObjectUtil.isNull(value)) {
			throw ASWException.builder().rcode(code).build();
		}
		return this;
	}

	/**
	 * 非空校验
	 *
	 * @return ValidateUtils
	 */
	public VerifyUtil notEmpty() {
		return notEmpty(null);
	}

	/**
	 * 非空校验
	 *
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil notEmpty(RCode code) {
		notNull(code);
		switch(value) {
		case String v -> {
			if(T.StrUtil.isEmpty(v)) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Collection<?> v -> {
			if(T.CollUtil.isEmpty(v)) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Object[] v -> {
			if(T.ArrayUtil.length(v) == 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		default ->{}
		}
		return this;
	}
	
	public VerifyUtil equalsIgnoreCase(CharSequence sequence) {
		return equalsIgnoreCase(sequence, null);
	}

	public VerifyUtil equalsIgnoreCase(CharSequence sequence, RCode code) {
		if (!T.StrUtil.equalsIgnoreCase(sequence, T.StrUtil.toStringOrNull(value))) {
			throw ASWException.builder().rcode(code).build();
		}
		return this;
	}

	/**
	 * 正则校验
	 *
	 * @param regex 正则表达式
	 * @return ValidateUtils
	 */
	public VerifyUtil regex(String regex) {
		return regex(regex, null);
	}

	/**
	 * 正则校验
	 *
	 * @param regex 正则表达式
	 * @param msg   错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil regex(String regex, RCode code) {
		if (!T.ReUtil.isMatch(regex, T.StrUtil.toStringOrNull(value))) {
			throw ASWException.builder().rcode(code).build();
		}
		return this;
	}

	/**
	 * 最大值校验
	 *
	 * @param max 最大值
	 * @return ValidateUtils
	 */
	public VerifyUtil max(Number max) {
		return max(max, null);
	}

	/**
	 * 最大值校验
	 *
	 * @param max 最大值
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil max(Number max, RCode code) {

		switch (value) {
		case null -> {
		}
		case Integer v -> {
			if (T.NumberUtil.compare(v, max.intValue()) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Long v -> {
			if (T.NumberUtil.compare(v, max.longValue()) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Double v -> {
			if (T.NumberUtil.compare(v, max.doubleValue()) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Float v -> {
			if (T.NumberUtil.compare(v, max.floatValue()) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Short v -> {
			if (T.NumberUtil.compare(v, max.shortValue()) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Byte v -> {
			if (T.NumberUtil.compare(v, max.byteValue()) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		default -> {
		}
		}
		return this;
	}

	/**
	 * 最小值校验
	 *
	 * @param min 最小值
	 * @return ValidateUtils
	 */
	public VerifyUtil min(Number min) {
		return min(min, null);
	}

	/**
	 * 最小值校验
	 *
	 * @param min 最小值
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil min(Number min, RCode code) {
		switch (value) {
		case null -> {
		}
		case Integer v -> {
			if (T.NumberUtil.compare(min.intValue(), v) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Long v -> {
			if (T.NumberUtil.compare(min.longValue(), v) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Double v -> {
			if (T.NumberUtil.compare(min.doubleValue(), v) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Float v -> {
			if (T.NumberUtil.compare(min.floatValue(), v) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Short v -> {
			if (T.NumberUtil.compare(min.shortValue(), v) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Byte v -> {
			if (T.NumberUtil.compare(min.byteValue(), v) > 0) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		default -> {
		}
		}
		return this;
	}

	/**
	 * 最大长度校验
	 *
	 * @param max 最大长度
	 * @return ValidateUtils
	 */
	public VerifyUtil maxLength(int max) {
		return maxLength(max, null);
	}

	/**
	 * 最大长度校验
	 *
	 * @param max 最大长度
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil maxLength(int max, RCode code) {
		switch (value) {
		case null -> {
		}
		case String v -> {
			if (T.StrUtil.length(v) > max) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Collection v -> {
			if (T.CollectionUtil.size(v) > max) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Object[] v -> {
			if (T.ArrayUtil.length(v) > max) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		default -> {
		}
		}
		return this;
	}

	/**
	 * 最小长度校验
	 *
	 * @param min 最小长度
	 * @return ValidateUtils
	 */
	public VerifyUtil minLength(int min) {
		return minLength(min, null);
	}

	/**
	 * 最小长度校验
	 *
	 * @param min 最小长度
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil minLength(int min, RCode code) {
		switch (value) {
		case null -> {
		}
		case String v -> {
			if (T.StrUtil.length(v) < min) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Collection v -> {
			if (T.CollectionUtil.size(v) < min) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		case Object[] v -> {
			if (T.ArrayUtil.length(v) < min) {
				throw ASWException.builder().rcode(code).build();
			}
		}
		default -> {
		}
		}
		return this;
	}

	/**
	 * 自定义日期格式校验
	 *
	 * @param format 格式
	 * @return ValidateUtils
	 */
	public VerifyUtil date(String format) {
		return date(format, null);
	}

	/**
	 * 自定义日期格式校验
	 *
	 * @param format 格式
	 * @param msg    错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil date(String format, RCode code) {
		try {
			T.DateUtil.parse(T.StrUtil.toStringOrNull(value), format);
		} catch (Exception e) {
			throw ASWException.builder().rcode(code).build();
		}

		return this;
	}

	/**
	 * IP地址校验
	 *
	 * @return ValidateUtils
	 */
	public VerifyUtil ip() {
		return ip(null);
	}

	/**
	 * IP地址校验
	 *
	 * @param msg 错误信息
	 * @return ValidateUtils
	 */
	public VerifyUtil ip(RCode code) {
		if (!Validator.isIpv4(T.StrUtil.toStringOrNull(value)) && !Validator.isIpv6(T.StrUtil.toStringOrNull(value))) {
			throw ASWException.builder().rcode(code).build();
		}
		return this;
	}

	public VerifyUtil port(RCode code) {
		if (!T.NetUtil.isValidPort(T.NumberUtil.parseInt(T.StrUtil.toStringOrNull(value)))) {
			throw ASWException.builder().rcode(code).build();
		}
		return this;
	}

	/**
	 * 多参数校验，不能同时为空
	 * 
	 * @param code
	 * @param validates
	 */
	public static VerifyUtil notAllNull(RCode code, Object... validates) {
		boolean allNull = true;
		for (Object validate : validates) {
			if (validate != null) {
				if (validate instanceof String) {
					if (T.ObjectUtil.isNull((String) validate))
						continue;
				} else if (validate instanceof Number) {
					if (((Number) validate) == null)
						continue;
				} else if (validate instanceof Collection) {
					if (T.ObjectUtil.isNull((Collection) validate))
						continue;
				} else if (validate instanceof Map) {
					if (T.ObjectUtil.isNull((Map) validate))
						continue;
				} else if (validate instanceof Object[]) {
					if (T.ObjectUtil.isNull((Object[]) validate))
						continue;
				}
				allNull = false;
				break;
			}
		}
		if (allNull) {
			throw new ASWException(code);
		}
		return new VerifyUtil(validates);
	}

}
