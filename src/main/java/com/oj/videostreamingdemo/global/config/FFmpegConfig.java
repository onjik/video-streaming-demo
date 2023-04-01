package com.oj.videostreamingdemo.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FFmpegConfig {
    @Value("${ffmpeg-tools.ffmpeg.location}")
    private String ffmpegLocation;

    @Value("${ffmpeg-tools.ffprobe.location}")
    private String ffprobeLocation;

    @Bean(name = "ffmpeg")
    public FFmpeg ffmpeg() throws IOException{
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(ffmpegLocation);
        return new FFmpeg(resource.getURL().getPath());
    }

    @Bean( name = "ffprobe")
    public FFprobe ffprobe() throws IOException{
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(ffprobeLocation);
        return new FFprobe(resource.getURL().getPath());
    }

}
