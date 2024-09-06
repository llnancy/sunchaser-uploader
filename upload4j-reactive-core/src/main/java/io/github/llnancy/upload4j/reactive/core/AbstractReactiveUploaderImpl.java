package io.github.llnancy.upload4j.reactive.core;

import com.google.common.base.Preconditions;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.FileUriGeneratorContext;
import io.github.llnancy.upload4j.api.exceptions.Upload4jException;
import io.github.llnancy.upload4j.core.AbstractUploader;
import io.github.llnancy.upload4j.core.fu.SpecifyPathFileUriGenerator;
import io.github.llnancy.upload4j.reactive.api.ReactiveUploader;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * abstract file uploader
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/3/12
 */
public abstract class AbstractReactiveUploaderImpl extends AbstractUploader implements ReactiveUploader {

    public AbstractReactiveUploaderImpl() {
    }

    public AbstractReactiveUploaderImpl(FileUriGenerator fileUriGenerator) {
        super(fileUriGenerator);
    }

    @Override
    public Mono<String> upload(FilePart fp) throws Upload4jException {
        return upload(fp, SpecifyPathFileUriGenerator.DEFAULT_SPECIFY_PATH);
    }

    @Override
    public Mono<String> upload(FilePart fp, String basePath) throws Upload4jException {
        try {
            String type = FilenameUtils.getExtension(Objects.requireNonNull(fp.filename()));
            Preconditions.checkArgument(supportFileType(type), "文件格式有误");
            String fileUri = this.fileUriGenerator.generate(FileUriGeneratorContext.create(fp.filename(), getIs(fp)));
            basePath = formatBasePath(basePath);
            return doFilePartUpload(fp, new URL(this.getServeDomain()).getPath() + basePath + fileUri);
        } catch (Exception e) {
            throw new Upload4jException(e);
        }
    }

    protected ByteArrayInputStream getIs(FilePart fp) {
        return null;
    }

    protected static ByteArrayInputStream mapFilePartIs(FilePart fp) {
        return fp.content()
                .publishOn(Schedulers.boundedElastic())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .reduce(new ByteArrayOutputStream(), (baos, bytes) -> {
                    try {
                        baos.write(bytes);
                    } catch (IOException e) {
                        throw new Upload4jException(e);
                    }
                    return baos;
                })
                .map(ByteArrayOutputStream::toByteArray)
                .map(ByteArrayInputStream::new)
                .block();
    }

    @Override
    public Mono<Void> delete(String path) throws Upload4jException {
        return Mono.defer(() -> doDelete(path))
                .onErrorMap(Exception.class, Upload4jException::new);
    }

    protected abstract Mono<Void> doDelete(String path);


    protected abstract Mono<String> doFilePartUpload(FilePart fp, String fileUri) throws Exception;
}
