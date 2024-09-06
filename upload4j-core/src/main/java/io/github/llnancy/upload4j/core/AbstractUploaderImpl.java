package io.github.llnancy.upload4j.core;

import com.google.common.base.Preconditions;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.FileUriGeneratorContext;
import io.github.llnancy.upload4j.api.Uploader;
import io.github.llnancy.upload4j.api.exceptions.Upload4jException;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Objects;

/**
 * abstract file uploader impl
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2021/10/22
 */
@Setter
public abstract class AbstractUploaderImpl extends AbstractUploader implements Uploader {

    public AbstractUploaderImpl() {
    }

    public AbstractUploaderImpl(FileUriGenerator fileUriGenerator) {
        super(fileUriGenerator);
    }

    @Override
    public String upload(MultipartFile mf, String basePath) throws Upload4jException {
        try {
            String type = FilenameUtils.getExtension(Objects.requireNonNull(mf.getOriginalFilename()));
            Preconditions.checkArgument(supportFileType(type), "文件格式有误");
            String fileUri = this.fileUriGenerator.generate(FileUriGeneratorContext.create(mf));
            basePath = formatBasePath(basePath);
            return doMultipartFileUpload(mf, new URL(this.getServeDomain()).getPath() + basePath + fileUri);
        } catch (Exception e) {
            throw new Upload4jException(e);
        }
    }

    @Override
    public String upload(byte[] bytes, String basePath, String extension) throws Upload4jException {
        try {
            String fileUri = this.fileUriGenerator.generate(FileUriGeneratorContext.create(extension));
            basePath = formatBasePath(basePath);
            return doUpload(bytes, new URL(this.getServeDomain()).getPath() + basePath + fileUri);
        } catch (Exception e) {
            throw new Upload4jException(e);
        }
    }

    protected abstract String doUpload(byte[] bytes, String fileUri) throws Exception;

    @Override
    public void delete(String path) throws Upload4jException {
        try {
            doDelete(path);
        } catch (Exception e) {
            throw new Upload4jException(e);
        }
    }

    protected abstract String doMultipartFileUpload(MultipartFile mf, String fileUri) throws Exception;

    protected abstract void doDelete(String path) throws Exception;
}
