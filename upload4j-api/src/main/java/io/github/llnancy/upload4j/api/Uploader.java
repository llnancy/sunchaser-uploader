package io.github.llnancy.upload4j.api;

import io.github.llnancy.upload4j.api.exceptions.Upload4jException;
import io.github.nativegroup.spi.SPI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Uploader 接口
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/6/27
 */
@SPI
public interface Uploader extends BaseUploader {

    /**
     * upload {@link File}
     *
     * @param file {@link File}
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(File file) throws Upload4jException {
        return upload(file, null);
    }

    /**
     * upload {@link File} with base path
     *
     * @param file     {@link File}
     * @param basePath base path
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(File file, String basePath) throws Upload4jException {
        try {
            return upload(FileUtils.readFileToByteArray(file), basePath);
        } catch (IOException e) {
            throw new Upload4jException(e);
        }
    }

    /**
     * upload {@link MultipartFile}
     *
     * @param mf {@link MultipartFile}
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(MultipartFile mf) throws Upload4jException {
        return upload(mf, null);
    }

    /**
     * upload {@link MultipartFile} with base path
     *
     * @param mf       {@link MultipartFile}
     * @param basePath base path
     * @return server url
     * @throws Upload4jException ex
     */
    String upload(MultipartFile mf, String basePath) throws Upload4jException;

    /**
     * upload {@link InputStream}
     *
     * @param is {@link InputStream}
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(InputStream is) throws Upload4jException {
        return upload(is, null);
    }

    /**
     * upload {@link InputStream} with base path
     *
     * @param is       {@link InputStream}
     * @param basePath base path
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(InputStream is, String basePath) throws Upload4jException {
        return upload(is, basePath, null);
    }

    default String upload(InputStream is, String basePath, String extension) throws Upload4jException {
        try {
            return upload(IOUtils.toByteArray(is), basePath, extension);
        } catch (IOException e) {
            throw new Upload4jException(e);
        }
    }

    /**
     * upload bytes data
     *
     * @param bytes bytes data
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(byte[] bytes) throws Upload4jException {
        return upload(bytes, null);
    }

    /**
     * upload bytes data with base path
     *
     * @param bytes    bytes data
     * @param basePath base path
     * @return server url
     * @throws Upload4jException ex
     */
    default String upload(byte[] bytes, String basePath) throws Upload4jException {
        return upload(bytes, basePath, null);
    }

    /**
     * upload bytes data with base path and file extension
     *
     * @param bytes     bytes data
     * @param basePath  base path
     * @param extension file extension
     * @return server url
     * @throws Upload4jException ex
     */
    String upload(byte[] bytes, String basePath, String extension) throws Upload4jException;

    /**
     * delete file
     * example: new URL(urlString).getPath();
     *
     * @param path url path
     * @throws Upload4jException ex
     */
    void delete(String path) throws Upload4jException;
}
