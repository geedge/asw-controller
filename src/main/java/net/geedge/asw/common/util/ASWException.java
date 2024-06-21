package net.geedge.asw.common.util;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ASWException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String msg = RCode.ERROR.getMsg();
	private int code = RCode.ERROR.getCode();
	private Object[] param = new Object[] {};

	public ASWException(RCode rCode) {
		super(rCode.getMsg());
		this.code = rCode.getCode();
		this.msg = rCode.getMsg();
		this.param = rCode.getParam();
	}

	public ASWException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public ASWException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public ASWException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public ASWException(String msg, int code, Object[] param, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
		this.param = param;
	}

	public ASWException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public void throwException() {
		throw this;
	}
	
	public static ASWExceptionBuilder builder() {
		return new ASWExceptionBuilder();
	}

	public static class ASWExceptionBuilder {
		private int code = RCode.ERROR.getCode();
		private String msg = RCode.ERROR.getMsg();
		private Throwable e;
		private Object[] param;

		private ASWExceptionBuilder() {
		}

		public ASWExceptionBuilder rcode(RCode rcode) {
			if (T.ObjectUtil.isNotNull(rcode)) {
				this.code = rcode.getCode();
				this.msg = rcode.getMsg();
				this.param = rcode.getParam();
			}
			return this;
		}

		public ASWExceptionBuilder code(int code) {
			this.code = code;
			return this;
		}

		public ASWExceptionBuilder msg(String msg) {
			this.msg = msg;
			return this;
		}

		public ASWExceptionBuilder param(Object[] param) {
			this.param = param;
			return this;
		}

		public ASWExceptionBuilder throwable(Throwable e) {
			this.e = e;
			return this;
		}

		public ASWException build() {
			return new ASWException(this.msg, this.code, this.param, this.e);
		}
	}

}