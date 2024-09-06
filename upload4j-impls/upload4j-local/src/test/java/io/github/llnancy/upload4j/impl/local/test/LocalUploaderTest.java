package io.github.llnancy.upload4j.impl.local.test;

import io.github.llnancy.upload4j.api.Uploader;
import io.github.llnancy.upload4j.impl.local.LocalUploaderImpl;
import io.github.llnancy.upload4j.impl.local.config.LocalConfig;
import io.github.nativegroup.spi.NativeServiceLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Files;

/**
 * Test {@link LocalUploaderImpl}
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2023/1/4
 */
public class LocalUploaderTest {

    @Test
    public void test() throws Exception {
        LocalConfig config = new LocalConfig();
        config.setServeDomain("file://");
        config.setLocalPath("/Users/llnancy/workspace/data/test");
        Uploader uploader = new LocalUploaderImpl(config);
        File file = new File("/Users/llnancy/workspace/data/1.txt");
        MockMultipartFile mmf = new MockMultipartFile(file.getAbsolutePath(), file.getName(), MediaType.APPLICATION_OCTET_STREAM_VALUE, Files.newInputStream(file.toPath()));
        String upload = uploader.upload(mmf);
        System.out.println(upload);
    }

    @Test
    public void testSpi() throws Exception {
        LocalConfig config = new LocalConfig();
        config.setServeDomain("file://");
        config.setLocalPath("/Users/llnancy/workspace/data/test/");
        Uploader local = NativeServiceLoader.getNativeServiceLoader(Uploader.class).getNativeService("io.github.llnancy.upload4j.impl.local.LocalUploaderImpl");
        local.setConfig(config);
        File file = new File("/Users/llnancy/workspace/money-projects/123.py");
        MockMultipartFile mmf = new MockMultipartFile(file.getAbsolutePath(), file.getName(), MediaType.APPLICATION_OCTET_STREAM_VALUE, Files.newInputStream(file.toPath()));
        String upload = local.upload(mmf);
        System.out.println(upload);
    }

    @Test
    public void testDelete() {
        LocalConfig config = new LocalConfig();
        config.setServeDomain("file://");
        config.setLocalPath("/Users/llnancy/workspace/data/test/");
        Uploader local = NativeServiceLoader.getNativeServiceLoader(Uploader.class).getNativeService("io.github.llnancy.upload4j.impl.local.LocalUploaderImpl");
        local.setConfig(config);
        local.delete("/Users/llnancy/workspace/data/test/123.py");
    }
}
