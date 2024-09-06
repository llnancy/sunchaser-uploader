package io.github.llnancy.upload4j.core.fn;

import io.github.llnancy.mojian.base.util.IdUtils;
import io.github.llnancy.upload4j.api.FileUriGeneratorContext;

/**
 * FileName: UUID.fileSuffix
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2021/10/22
 */
public class UUIDFilenameGenerator extends AbstractFilenameGenerator {

    @Override
    protected String doGenerate(FileUriGeneratorContext context) {
        return IdUtils.simpleUUID();
    }
}
