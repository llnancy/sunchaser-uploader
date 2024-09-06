package io.github.llnancy.upload4j.impl.upyun.test;

import com.upyun.RestManager;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.Uploader;
import io.github.llnancy.upload4j.core.fu.SpecifyPathFileUriGenerator;
import io.github.llnancy.upload4j.impl.upyun.UpYunUploaderImpl;
import io.github.llnancy.upload4j.impl.upyun.config.UpYunConfig;
import io.github.nativegroup.spi.NativeServiceLoader;
import okhttp3.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

/**
 * Test {@link UpYunUploaderImpl}
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2023/1/4
 */
public class UpYunUploaderTest {

    @Test
    public void test() throws Exception {
        UpYunConfig properties = new UpYunConfig();
        properties.setServeDomain("https://images.lilu.org.cn");
        properties.setBucketName("llnancy-images");
        properties.setUserName("sunchaser");
        properties.setPassword("xxxxxx");
        SpecifyPathFileUriGenerator fileUriGenerator = (SpecifyPathFileUriGenerator) NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class).getNativeService("io.github.llnancy.upload4j.core.fu.SpecifyPathFileUriGenerator");
        // fileUriGenerator.setSpecifyPath("/test");
        UpYunUploaderImpl uploader = (UpYunUploaderImpl) NativeServiceLoader.getNativeServiceLoader(Uploader.class).getNativeService("io.github.llnancy.upload4j.impl.upyun.UpYunUploaderImpl");
        uploader.setFileUriGenerator(fileUriGenerator);
        uploader.setConfig(properties);
        uploader.init();
        File file = new File("/Users/llnancy/workspace/data/4d2fec59f254487bad04bca59816edf0.png");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
        MockMultipartFile mockMultipartFile = new MockMultipartFile(file.getAbsolutePath(), file.getName(), MediaType.APPLICATION_OCTET_STREAM_VALUE, Files.newInputStream(file.toPath()));
        String upload = uploader.upload(mockMultipartFile, "top");
        System.out.println(upload);
    }

    @Test
    public void testRm() throws Exception {
        UpYunConfig properties = new UpYunConfig();
        properties.setServeDomain("https://images.lilu.org.cn");
        properties.setBucketName("llnancy-cdn");
        properties.setUserName("sunchaser");
        properties.setPassword("xx");
        UpYunUploaderImpl uploader = (UpYunUploaderImpl) NativeServiceLoader.getNativeServiceLoader(Uploader.class).getNativeService("io.github.llnancy.upload4j.impl.upyun.UpYunUploaderImpl");
        uploader.setConfig(properties);
        uploader.init();
        RestManager restManager = uploader.getRestManager();
        Response dir = restManager.readDirIter("wallpaper/bp", null);
        // System.out.println(dir.body().string());
        String[] split = dir.body().string().split("\n");
        dir.close();
        for (String s : split) {
            System.out.println(s);
            String[] split1 = s.split("\t");
            String subpath = split1[0];
            System.out.println("wallpaper/bp/" + subpath);
            Response subpathResponse = restManager.readDirIter("wallpaper/bp/" + subpath, null);
            String[] split2 = subpathResponse.body().string().split("\n");
            subpathResponse.close();
            for (String string : split2) {
                String[] split3 = string.split("\t");
                String s1 = split3[0];
                Response response = restManager.deleteFile("wallpaper/bp/" + subpath + "/" + s1, null);
                response.close();
            }
            Response response = restManager.rmDir("wallpaper/bp/" + subpath);
            System.out.println(response.body().string());
            response.close();
        }
    }

    @Test
    public void testDelete() {
        UpYunConfig properties = new UpYunConfig();
        properties.setServeDomain("https://images.lilu.org.cn");
        properties.setBucketName("llnancy-images");
        properties.setUserName("sunchaser");
        properties.setPassword("xxxxxx");
        SpecifyPathFileUriGenerator fileUriGenerator = (SpecifyPathFileUriGenerator) NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class)
                .getNativeService("io.github.llnancy.upload4j.core.fu.SpecifyPathFileUriGenerator");
        // fileUriGenerator.setSpecifyPath("/test");
        Uploader uploader = NativeServiceLoader.getNativeServiceLoader(Uploader.class)
                .getNativeService("io.github.llnancy.upload4j.impl.upyun.UpYunUploaderImpl");
        uploader.setFileUriGenerator(fileUriGenerator);
        uploader.setConfig(properties);
        uploader.init();
        uploader.delete("/test/tmksz59tJFIP066bcc6e69b32f7ae80f2b5ad18139ef.png");
    }
}
