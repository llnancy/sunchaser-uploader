package io.github.llnancy.upload4j.api;

import io.github.nativegroup.spi.SPI;

/**
 * 文件路径生成器
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2021/10/22
 */
@SPI("io.github.llnancy.upload4j.core.fu.SpecifyPathFileUriGenerator")
public interface FileUriGenerator {

    String generate(FileUriGeneratorContext context);

    void setFileNameGenerator(FilenameGenerator fileNameGenerator);
}
