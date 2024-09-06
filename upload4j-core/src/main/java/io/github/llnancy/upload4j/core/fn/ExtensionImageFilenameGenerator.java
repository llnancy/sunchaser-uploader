package io.github.llnancy.upload4j.core.fn;

import io.github.llnancy.mojian.base.util.IdUtils;
import io.github.llnancy.upload4j.api.FileUriGeneratorContext;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * FileName: height_width-UUID.fileSuffix
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2021/10/22
 */
public class ExtensionImageFilenameGenerator extends AbstractFilenameGenerator {

    @SneakyThrows
    @Override
    protected String doGenerate(FileUriGeneratorContext context) {
        int width = 0;
        int height = 0;
        if (Objects.nonNull(context.is())) {
            BufferedImage bi = ImageIO.read(context.is());
            width = bi.getWidth();
            height = bi.getHeight();
        }
        return height + "_" + width + "-" + IdUtils.simpleUUID();
    }
}
