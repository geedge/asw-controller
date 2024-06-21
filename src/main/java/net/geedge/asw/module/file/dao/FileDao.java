package net.geedge.asw.module.file.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.geedge.asw.module.file.entity.FileEntity;

@Mapper
public interface FileDao extends BaseMapper<FileEntity> {

//	public void saveFileContent(FileContentEntity entity);
//
//	public FileContentEntity getFileContentById(String id);
//
//	public boolean deleteFileContentById(List<String> ids);
}
