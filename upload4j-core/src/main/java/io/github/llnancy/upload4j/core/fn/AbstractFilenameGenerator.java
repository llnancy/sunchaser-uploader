package io.github.llnancy.upload4j.core.fn;

import io.github.llnancy.mojian.base.util.Optionals;
import io.github.llnancy.upload4j.api.FileUriGeneratorContext;
import io.github.llnancy.upload4j.api.FilenameGenerator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * an abstract {@link FilenameGenerator } implementation.
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2021/10/22
 */
public abstract class AbstractFilenameGenerator implements FilenameGenerator {

    @Override
    public String generate(FileUriGeneratorContext context) {
        String extension = Optionals.of(FilenameUtils.getExtension(context.filename()), context.extension());
        String fileSuffix = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(extension)) {
            fileSuffix = "." + extension;
        }
        return doGenerate(context) + fileSuffix;
    }

    protected abstract String doGenerate(FileUriGeneratorContext context);
}
