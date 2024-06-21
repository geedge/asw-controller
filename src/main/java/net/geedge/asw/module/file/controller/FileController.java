package net.geedge.asw.module.file.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.hutool.log.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.geedge.asw.common.util.ASWException;
import net.geedge.asw.common.util.R;
import net.geedge.asw.common.util.RCode;
import net.geedge.asw.common.util.T;
import net.geedge.asw.module.file.entity.FileContentEntity;
import net.geedge.asw.module.file.entity.FileEntity;
import net.geedge.asw.module.file.service.IFileService;
import net.geedge.asw.module.sys.controller.BaseController;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
	private static final Log log = Log.get();
	@Autowired
	private IFileService fileService;
	// 单位: s，默认：1天
	@Value("${file.expires:86400}")
	private Integer fileExpires;

	@GetMapping("/{id}")
	public R detail(@PathVariable("id") String id) {
		FileEntity entity = fileService.getById(id);
		return R.ok().putData("record", entity);
	}

	@GetMapping
	public R list(String q, @RequestParam(defaultValue = "1") Integer current,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "name") String orderBy) {
		QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<FileEntity>();
		queryWrapper.and(T.StrUtil.isNotBlank(q), wrapper -> {
			wrapper.like("name", q).or().like("remark", q).or().like("path", q);
		});
		Page<FileEntity> page = Page.of(current, size);
		page.addOrder(T.PageUtil.decodeOrderByStr(orderBy));
		page = fileService.page(page, queryWrapper);
		page.getRecords().forEach(entity -> {
			entity.setUri(T.StrUtil.concat(true, "/file/content/", entity.getId(), ".", entity.getSuffix()));
		});
		return R.ok(page);
	}

	@PostMapping
	public R add(String remark, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		if (T.ObjectUtil.isNull(file)) {
			throw ASWException.builder().rcode(RCode.PARAM_CANNOT_EMPTY).build();
		}
		FileEntity savedFile = fileService.saveFile(remark, file);
		return R.ok().putData("record", savedFile);
	}

	@DeleteMapping
	public R delete(String[] ids) {
		T.VerifyUtil.is(ids).notEmpty();
		fileService.deleteFile(ids);
		log.info("delete files, ids: {}", T.ArrayUtil.toString(ids));
		return R.ok();
	}

	/**
	 * 下载文件内容
	 * 
	 * @param name     name = id + suffix , eq: adfadfadf.txt
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/content/{name}")
	public void download(@PathVariable("name") String name, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String id = T.FileNameUtil.getPrefix(name);
		FileEntity fileEntity = fileService.getById(id);
		if (T.ObjectUtil.isNull(fileEntity)) {
			response.sendError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
		} else {
			FileContentEntity fileContentEntity = fileService.getFileContentEntityById(id);
			response.setStatus(200);
			response.setContentType(fileEntity.getContentType());
			response.setContentLength(fileEntity.getSize());
			response.setHeader("Content-disposition", "attachment; filename=" + fileEntity.getName());
			// 设置缓存响应头
			response.setDateHeader("Expires", T.DateUtil.current() + fileExpires * 1000);
			response.setHeader("Cache-Control", "public, max-age=" + fileExpires);
			response.getOutputStream().write(fileContentEntity.getContent());
			response.flushBuffer();
		}
	}

}
