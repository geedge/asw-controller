package net.geedge.asw.module.file.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;

import net.geedge.asw.module.file.entity.FileContentEntity;
import net.geedge.asw.module.file.entity.FileEntity;

public interface IFileService extends IService<FileEntity> {

	/**
	 * 保存文件信息及内容
	 *
	 * @param remark
	 * @param file
	 * @return
	 * @throws IOException
	 */
	FileEntity saveFile(String remark, MultipartFile file) throws IOException;

	/**
	 * 根据文件 id 获取内容
	 * 
	 * @param id
	 * @return
	 */
	public FileContentEntity getFileContentEntityById(String id);

	/**
	 * 删除文件和文件内容
	 * 
	 * @param ids
	 */
	void deleteFile(String[] ids);

	public void saveFileAndContent(FileEntity entity, File file) throws IOException;

}
