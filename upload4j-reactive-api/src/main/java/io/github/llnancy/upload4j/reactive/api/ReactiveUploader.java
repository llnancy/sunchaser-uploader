package io.github.llnancy.upload4j.reactive.api;

import io.github.llnancy.upload4j.api.BaseUploader;
import io.github.llnancy.upload4j.api.exceptions.Upload4jException;
import io.github.nativegroup.spi.SPI;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

/**
 * reactive api
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/3/12
 */
@SPI
public interface ReactiveUploader extends BaseUploader {

    /**
     * upload {@link FilePart}
     *
     * @param fp {@link FilePart}
     * @return server url
     * @throws Upload4jException ex
     */
    Mono<String> upload(FilePart fp) throws Upload4jException;

    /**
     * upload {@link FilePart} with base path
     *
     * @param fp       {@link FilePart}
     * @param basePath base path
     * @return server url
     * @throws Upload4jException ex
     */
    Mono<String> upload(FilePart fp, String basePath) throws Upload4jException;

    /**
     * delete file
     * example: new URL(urlString).getPath();
     *
     * @param path url path
     * @return success or fail
     * @throws Upload4jException ex
     */
    Mono<Void> delete(String path) throws Upload4jException;
}
