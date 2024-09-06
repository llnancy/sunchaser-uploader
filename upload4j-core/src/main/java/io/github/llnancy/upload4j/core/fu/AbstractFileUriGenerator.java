package io.github.llnancy.upload4j.core.fu;

import io.github.llnancy.upload4j.api.FileUriGeneratorContext;
import io.github.llnancy.upload4j.api.FilenameGenerator;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.nativegroup.spi.NativeServiceLoader;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * an abstract {@link FileUriGenerator } implementation.
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2023/1/4
 */
@Setter
public abstract class AbstractFileUriGenerator implements FileUriGenerator {

    private FilenameGenerator fileNameGenerator;

    public AbstractFileUriGenerator() {
        this.fileNameGenerator = NativeServiceLoader.getNativeServiceLoader(FilenameGenerator.class).getDefaultNativeService();
    }

    public AbstractFileUriGenerator(FilenameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }

    @Override
    public String generate(FileUriGeneratorContext context) {
        String fileUri = doGenerate(context);
        if (StringUtils.isNotBlank(fileUri)) {
            return fileUri + "/" + fileNameGenerator.generate(context);
        } else {
            return fileNameGenerator.generate(context);
        }
    }

    protected abstract String doGenerate(FileUriGeneratorContext context);
}
