package io.github.llnancy.upload4j.reactive.impl.local;

import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.config.Upload4jConfig;
import io.github.llnancy.upload4j.impl.local.config.LocalConfig;
import io.github.llnancy.upload4j.reactive.core.AbstractReactiveUploaderImpl;
import io.github.nativegroup.spi.NativeServiceLoader;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * local file uploader
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/3/12
 */
@Getter
public class ReactiveLocalUploaderImpl extends AbstractReactiveUploaderImpl {

    private LocalConfig config;

    public ReactiveLocalUploaderImpl() {
    }

    public ReactiveLocalUploaderImpl(LocalConfig properties) {
        this(NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class).getDefaultNativeService(), properties);
    }

    public ReactiveLocalUploaderImpl(FileUriGenerator fileUriGenerator, LocalConfig config) {
        super(fileUriGenerator);
        this.config = config;
        init();
    }

    @Override
    public void setConfig(Upload4jConfig config) {
        this.config = (LocalConfig) config;
    }

    @Override
    protected Mono<Void> doDelete(String path) {
        try {
            FileUtils.delete(new File(path));
            return Mono.empty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Mono<String> doFilePartUpload(FilePart fp, String fileUri) throws Exception {
        String targetUri = this.config.getLocalPath() + fileUri;
        return fp.transferTo(Path.of(targetUri))
                .then(Mono.defer(() -> Mono.just(this.getProtocolHost() + targetUri)));
    }
}
