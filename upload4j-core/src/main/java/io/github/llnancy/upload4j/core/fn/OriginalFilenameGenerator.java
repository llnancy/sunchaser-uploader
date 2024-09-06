package io.github.llnancy.upload4j.core.fn;

import io.github.llnancy.mojian.base.util.IdUtils;
import io.github.llnancy.upload4j.api.FileUriGeneratorContext;
import io.github.llnancy.upload4j.api.FilenameGenerator;

import java.util.Optional;

/**
 * FileName: originalFileName
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/7/1
 */
public class OriginalFilenameGenerator implements FilenameGenerator {

    @Override
    public String generate(FileUriGeneratorContext context) {
        return Optional.ofNullable(context.filename())
                .orElse(IdUtils.simpleUUID());
    }
}
