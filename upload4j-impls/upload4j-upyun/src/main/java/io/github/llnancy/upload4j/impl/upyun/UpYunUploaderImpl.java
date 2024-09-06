package io.github.llnancy.upload4j.impl.upyun;

import com.upyun.RestManager;
import com.upyun.UpYunUtils;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.config.Upload4jConfig;
import io.github.llnancy.upload4j.api.exceptions.Upload4jException;
import io.github.llnancy.upload4j.core.AbstractUploaderImpl;
import io.github.llnancy.upload4j.impl.upyun.config.UpYunConfig;
import io.github.nativegroup.spi.NativeServiceLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

/**
 * 又拍云文件上传器
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/6/27
 */
@Getter
@Slf4j
public class UpYunUploaderImpl extends AbstractUploaderImpl {

    private UpYunConfig config;

    private RestManager restManager;

    public UpYunUploaderImpl() {
    }

    public UpYunUploaderImpl(UpYunConfig properties) {
        this(NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class).getDefaultNativeService(), properties);
    }

    public UpYunUploaderImpl(FileUriGenerator fileUriGenerator, UpYunConfig config) {
        super(fileUriGenerator);
        this.config = config;
        init();
    }

    @Override
    public void setConfig(Upload4jConfig config) {
        this.config = (UpYunConfig) config;
    }

    @Override
    protected void doInit() {
        this.restManager = Utils.initRestManager(this.config);
    }

    @Override
    protected String doMultipartFileUpload(MultipartFile mf, String fileUri) throws Exception {
        return doUpload(mf.getBytes(), fileUri);
    }

    @Override
    protected String doUpload(byte[] bytes, String fileUri) throws Exception {
        Response fileInfo = null;
        Response response = null;
        try {
            fileInfo = this.restManager.getFileInfo(fileUri);
            Headers headers = fileInfo.headers();
            String fileSize = headers.get(RestManager.PARAMS.X_UPYUN_FILE_SIZE.getValue());
            String newMd5 = UpYunUtils.md5(bytes);
            if (StringUtils.isNotBlank(fileSize) && newMd5.equals(headers.get(RestManager.PARAMS.CONTENT_MD5.getValue()))) {
                log.warn("[upyun] - 又拍云文件上传，文件名：{}，md5 值相同，上传文件重复", fileUri);
                return this.getProtocolHost() + fileUri;
            }
            response = this.restManager.writeFile(fileUri, bytes, Map.of(RestManager.PARAMS.CONTENT_MD5.getValue(), newMd5));
            if (response.isSuccessful()) {
                return this.getProtocolHost() + fileUri;
            }
            log.error("[upyun] - 又拍云文件上传失败，response={}", response);
            throw new Upload4jException("[upyun] - 又拍云文件上传失败，fileUri=" + fileUri);
        } finally {
            if (Objects.nonNull(fileInfo)) {
                fileInfo.close();
            }
            if (Objects.nonNull(response)) {
                response.close();
            }
        }
    }

    @Override
    protected void doDelete(String path) throws Exception {
        try (Response response = this.restManager.deleteFile(path, null)) {
            if (!response.isSuccessful()) {
                throw new Upload4jException(response.message());
            }
        }
    }
}
