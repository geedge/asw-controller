package net.geedge.asw.module.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.geedge.asw.common.util.T;
import net.geedge.asw.module.file.dao.FileContentDao;
import net.geedge.asw.module.file.dao.FileDao;
import net.geedge.asw.module.file.entity.FileContentEntity;
import net.geedge.asw.module.file.entity.FileEntity;
import net.geedge.asw.module.file.service.IFileService;

@Service
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements IFileService {
	
	@Autowired
	private FileContentDao fileContentDao;

	@Override
	@Transactional
	public FileEntity saveFile(String remark, MultipartFile file) throws IOException {
		String md5 = T.DigestUtil.md5Hex(file.getBytes());
		// 根据md5判断是否已经存在，存在直接响应
		FileEntity entity = this.getOne(new QueryWrapper<FileEntity>().lambda().eq(FileEntity::getId, md5));
		if (T.ObjectUtil.isNull(entity)) {
			entity = new FileEntity();
			entity.setId(md5);
			entity.setSize((int) file.getSize());
			entity.setName(file.getOriginalFilename());
			entity.setSuffix(T.FileNameUtil.getSuffix(entity.getName()));
			entity.setCreateTimestamp(T.DateUtil.current());
			Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(entity.getName());
			if (mediaType.isPresent()) {
				entity.setContentType(mediaType.get().toString());
			} else {
				entity.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			}
			entity.setRemark(remark);
			this.saveFileAndContent(entity, file);
		}
		entity.setUri(T.StrUtil.concat(true, "/file/content/", entity.getId(), ".", entity.getSuffix()));
		return entity;
	}

	public void saveFileAndContent(FileEntity entity, MultipartFile file) throws IOException {
		// 默认统一保存到数据库中
		entity.setPath(T.StrUtil.concat(true, "db:", entity.getId()));
		this.save(entity);
		FileContentEntity content = new FileContentEntity();
		content.setId(entity.getId());
		content.setContent(file.getBytes());
		fileContentDao.insert(content);
	}
	@Override
	public void saveFileAndContent(FileEntity entity, File file) throws IOException {
		// 默认统一保存到数据库中
		entity.setPath(T.StrUtil.concat(true, "db:", entity.getId()));
		this.save(entity);
		FileContentEntity content = new FileContentEntity();
		content.setId(entity.getId());
		content.setContent(T.FileUtil.readBytes(file));
		fileContentDao.insert(content);
	}
	
	@Override
	@Transactional
	public void deleteFile(String[] ids) {
		List<String> list = T.ListUtil.of(ids);
		this.removeByIds(list);
		fileContentDao.deleteBatchIds(list);
	}

	@Override
	public FileContentEntity getFileContentEntityById(String id) {
		return fileContentDao.selectById(id);
	}

}
