package io.github.llnancy.upload4j.reactive.impl.upyun;

import com.upyun.RestManager;
import com.upyun.UpException;
import com.upyun.UpYunUtils;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.config.Upload4jConfig;
import io.github.llnancy.upload4j.api.exceptions.Upload4jException;
import io.github.llnancy.upload4j.impl.upyun.Utils;
import io.github.llnancy.upload4j.impl.upyun.config.UpYunConfig;
import io.github.llnancy.upload4j.reactive.core.AbstractReactiveUploaderImpl;
import io.github.nativegroup.spi.NativeServiceLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

/**
 * 又拍云文件上传器
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/3/12
 */
@Getter
@Slf4j
public class ReactiveUpYunUploaderImpl extends AbstractReactiveUploaderImpl {

    private UpYunConfig config;

    private RestManager restManager;

    public ReactiveUpYunUploaderImpl() {
    }

    public ReactiveUpYunUploaderImpl(UpYunConfig properties) {
        this(NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class).getDefaultNativeService(), properties);
    }

    public ReactiveUpYunUploaderImpl(FileUriGenerator fileUriGenerator, UpYunConfig config) {
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
    protected Mono<Void> doDelete(String path) {
        try (Response response = this.restManager.deleteFile(path, null)) {
            if (!response.isSuccessful()) {
                throw new Upload4jException(response.message());
            }
        } catch (UpException | IOException e) {
            throw new Upload4jException(e);
        }
        return Mono.empty();
    }

    @Override
    protected Mono<String> doFilePartUpload(FilePart fp, String fileUri) throws Exception {
        return Mono.fromCallable(() -> this.restManager.getFileInfo(fileUri))
                .flatMap(fileInfo -> {
                    Headers headers = fileInfo.headers();
                    String fileSize = headers.get(RestManager.PARAMS.X_UPYUN_FILE_SIZE.getValue());
                    return DataBufferUtils.join(fp.content())
                            .flatMap(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                DataBufferUtils.release(dataBuffer);

                                String newMd5 = UpYunUtils.md5(bytes);
                                if (StringUtils.isNotBlank(fileSize) && newMd5.equals(headers.get(RestManager.PARAMS.CONTENT_MD5.getValue()))) {
                                    log.warn("[upyun] - 又拍云文件上传，文件名：{}，md5 值相同，上传文件重复", fileUri);
                                    return Mono.just(this.getProtocolHost() + fileUri);
                                }
                                return Mono.fromCallable(() -> this.restManager.writeFile(fileUri, bytes, Map.of(RestManager.PARAMS.CONTENT_MD5.getValue(), newMd5)))
                                        .flatMap(response -> {
                                            if (response.isSuccessful()) {
                                                return Mono.just(this.getProtocolHost() + fileUri);
                                            } else {
                                                log.error("[upyun] - 又拍云文件上传失败，response={}", response);
                                                return Mono.error(new Upload4jException("[upyun] - 又拍云文件上传失败，fileUri=" + fileUri));
                                            }
                                        });
                            });
                });
    }
}
