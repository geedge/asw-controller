package net.geedge.asw.common.util;

import java.io.IOException;

import org.springframework.http.MediaType;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtil {

    /**
     * downloadFile
     *
     * @param response
     * @param contentType
     * @param filename
     * @param data
     * @throws IORuntimeException
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse response, String contentType, String filename, byte[] data) throws IORuntimeException, IOException {
        String fileName = T.URLUtil.encode(filename, T.CharsetUtil.CHARSET_UTF_8);
        ReflectUtil.invoke(response, "addHeader", "Content-Disposition", "attachment; filename=" + fileName);
        ReflectUtil.invoke(response, "addHeader", "Content-Length", "" + data.length);
        ReflectUtil.invoke(response, "setHeader", "Access-Control-Expose-Headers", "Content-Disposition");
        ReflectUtil.invoke(response, "setContentType", StrUtil.emptyToDefault(contentType, MediaType.APPLICATION_OCTET_STREAM_VALUE));

        T.IoUtil.write(response.getOutputStream(), false, data);
    }


    /**
     * reponse 下载 byte数据
     * @param response
     * @param filename
     * @param data
     * @throws IORuntimeException
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse response, String filename, byte[] data)
            throws IORuntimeException, IOException {
        response.setContentType(ResponseUtil.getDownloadContentType(filename));
        String fileName = T.URLUtil.encode(filename, T.CharsetUtil.CHARSET_UTF_8);
//		response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
//		response.addHeader("Content-Length", "" + data.length);
//		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        ReflectUtil.invoke(response, "addHeader", "Content-Disposition", "attachment; filename=" + fileName);
        ReflectUtil.invoke(response, "addHeader", "Content-Length", "" + data.length);
        ReflectUtil.invoke(response, "setHeader", "Access-Control-Expose-Headers", "Content-Disposition");
        T.IoUtil.write(response.getOutputStream(), false, data);
    }

    public static String getDownloadContentType(String filename){
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(filename);
        return T.ObjectUtil.isEmpty(extensionMatch) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : extensionMatch.getMimeType();
    }

}
