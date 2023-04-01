package com.oj.videostreamingdemo.global.config;

import com.oj.videostreamingdemo.test_super.IntegrationTest;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ffmpeg config 테스트")
class FFmpegConfigTest extends IntegrationTest {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("ffmpeg 빈 정상 생성")
    public void ffmpegBeanLoadTest(){
        Object bean = context.getBean("ffmpeg");

        assertInstanceOf(FFmpeg.class,bean);
        assertNotNull(bean);
    }

    @Test
    @DisplayName("ffprobe 빈 정상 생성")
    public void ffprobeBeanLoadTest(){
        Object bean = context.getBean("ffprobe");

        assertInstanceOf(FFprobe.class,bean);
        assertNotNull(bean);
    }

}