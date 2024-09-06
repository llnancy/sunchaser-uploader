package io.github.llnancy.upload4j.impl.local;

import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.config.Upload4jConfig;
import io.github.llnancy.upload4j.core.AbstractUploaderImpl;
import io.github.llnancy.upload4j.impl.local.config.LocalConfig;
import io.github.nativegroup.spi.NativeServiceLoader;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 本地文件上传器
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/6/27
 */
@Getter
public class LocalUploaderImpl extends AbstractUploaderImpl {

    private LocalConfig config;

    public LocalUploaderImpl() {
    }

    public LocalUploaderImpl(LocalConfig properties) {
        this(NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class).getDefaultNativeService(), properties);
    }

    public LocalUploaderImpl(FileUriGenerator fileUriGenerator, LocalConfig config) {
        super(fileUriGenerator);
        this.config = config;
        init();
    }

    @Override
    protected String doMultipartFileUpload(MultipartFile mf, String fileUri) throws Exception {
        return doUpload(mf.getBytes(), fileUri);
    }

    @Override
    protected String doUpload(byte[] bytes, String fileUri) throws Exception {
        String targetUri = this.config.getLocalPath() + fileUri;
        FileUtils.writeByteArrayToFile(new File(targetUri), bytes);
        return this.getProtocolHost() + targetUri;
    }

    @Override
    protected void doDelete(String path) throws Exception {
        FileUtils.delete(new File(path));
    }

    @Override
    public void setConfig(Upload4jConfig config) {
        this.config = (LocalConfig) config;
    }
}
