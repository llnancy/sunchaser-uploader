package io.github.llnancy.upload4j.core;

import com.google.common.base.Preconditions;
import io.github.llnancy.upload4j.api.BaseUploader;
import io.github.llnancy.upload4j.api.FileUriGenerator;
import io.github.llnancy.upload4j.api.config.Upload4jConfig;
import io.github.nativegroup.spi.NativeServiceLoader;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * abstract uploader
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/3/12
 */
@Setter
public abstract class AbstractUploader implements BaseUploader {

    protected FileUriGenerator fileUriGenerator;

    public AbstractUploader() {
        this.fileUriGenerator = NativeServiceLoader.getNativeServiceLoader(FileUriGenerator.class).getDefaultNativeService();
    }

    public AbstractUploader(FileUriGenerator fileUriGenerator) {
        this.fileUriGenerator = fileUriGenerator;
    }

    @Override
    public void init() {
        initServeDomain();
        doInit();
    }

    protected void doInit() {
    }

    protected void initServeDomain() {
        Upload4jConfig config = getConfig();
        String serveDomain = config.getServeDomain();
        Preconditions.checkArgument(StringUtils.isNotBlank(serveDomain), "[uploader] - 文件上传器配置 - 服务域名 serveDomain 不能为空");
        config.setServeDomain(serveDomain.endsWith("/") ? StringUtils.chop(serveDomain) : serveDomain);
    }

    public abstract Upload4jConfig getConfig();

    protected String getServeDomain() {
        return getConfig().getServeDomain();
    }

    protected String getProtocolHost() {
        try {
            URL url = new URL(getServeDomain());
            String protocol = url.getProtocol();
            String domain = url.getHost();
            return protocol + "://" + domain;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String formatBasePath(String basePath) {
        if (StringUtils.isNotEmpty(basePath) && !StringUtils.startsWith(basePath, "/")) {
            basePath = "/" + basePath;
        }
        if (StringUtils.isNotEmpty(basePath) && !StringUtils.endsWith(basePath, "/")) {
            basePath = basePath + "/";
        }
        return basePath;
    }

    public boolean supportFileType(String type) {
        return true;
    }
}
