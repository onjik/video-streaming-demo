package com.oj.videostreamingdemo.global.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;


@Order(Ordered.LOWEST_PRECEDENCE)
public class ProfileResolverEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
    private final ClassPathResource ffmpegYml = new ClassPathResource("application-ffmpeg.yml");
    private enum OsName {WINDOW,MAC,LINUX_ARM64,LINUX_AMD64};
    private final String OS_NAME_PROPERTY = "os-name";
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //실행중인 OS 감지
        OsName osName = null;
        final String systemOs = System.getProperty("os.name").toLowerCase();
        final String arch = System.getProperty("os.arch").toLowerCase();

        if (systemOs.contains("mac")){
            osName = OsName.MAC;
        } else if (systemOs.contains("win")) {
            osName = OsName.WINDOW;
        } else if (systemOs.contains("linux")) {
            if (arch.contains("aarch64")){
                osName = OsName.LINUX_ARM64;
            } else if (arch.contains("amd64")) {
                osName = OsName.LINUX_AMD64;
            } else throw new IllegalStateException("Unsupported Linux Architecture");
        } else {
            throw new IllegalStateException("Unsupported Operating System");
        }

        //propertySources 만들기
        List<PropertySource<?>> ymlPropertySources;
        try {
            ymlPropertySources = yamlPropertySourceLoader.load(ffmpegYml.getFilename(), ffmpegYml);
        } catch (IOException e){
            throw new IllegalStateException(e);
        }

        MutablePropertySources environmentPropertySources = environment.getPropertySources();

        final String matchingWord = osName.name();
        //공통설정
        ymlPropertySources.stream()
                .filter(source -> ObjectUtils.isEmpty(source.getProperty(OS_NAME_PROPERTY)))
                .forEach(environmentPropertySources::addLast);
        //OS에 맞는 설정 주입
        ymlPropertySources.stream()
                .filter(source -> matchingWord.equals(source.getProperty(OS_NAME_PROPERTY)))
                .forEach(environmentPropertySources::addLast);
    }
}
