package io.github.llnancy.upload4j.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * file generate context
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/8/17
 */
public record FileUriGeneratorContext(String filename, InputStream is, String extension) {

    public static FileUriGeneratorContext create(MultipartFile mf) throws IOException {
        return new FileUriGeneratorContext(mf.getOriginalFilename(), mf.getInputStream(), null);
    }

    public static FileUriGeneratorContext create(String filename, InputStream is) throws IOException {
        return new FileUriGeneratorContext(filename, is, null);
    }

    public static FileUriGeneratorContext create(String extension) {
        return new FileUriGeneratorContext(null, null, extension);
    }
}
