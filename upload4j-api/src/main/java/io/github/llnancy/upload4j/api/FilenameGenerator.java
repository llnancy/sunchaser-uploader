package io.github.llnancy.upload4j.api;

import io.github.nativegroup.spi.SPI;

/**
 * 文件名生成器
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2021/10/22
 */
@SPI("io.github.llnancy.upload4j.core.fn.OriginalFilenameGenerator")
public interface FilenameGenerator {

    String generate(FileUriGeneratorContext context);
}
