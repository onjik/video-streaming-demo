package com.oj.videostreamingdemo.global.util;

import com.oj.videostreamingdemo.test_super.IntegrationTest;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FFmpegUtilTest extends IntegrationTest {
    @Autowired
    FFmpegUtil ffmpegUtil;

    @Test
    public void getProbeResult(){
        FileSystemResource file = new FileSystemResource(Paths.get("video-files","file_example_MP4_1920_18MG.mp4"));
        FFmpegProbeResult probeResult = ffmpegUtil.getProbeResult(file);
        System.out.println(probeResult.format.bit_rate);
        assertNotNull(probeResult);
    }

    @Test
    public void convertVideoProp(){
        //given
        FileSystemResource file = new FileSystemResource(Paths.get("video-files","file_example_MP4_1920_18MG.mp4"));
        FFmpegProbeResult inputResource = ffmpegUtil.getProbeResult(file);
        Path outputPath = Paths.get("video-files", "temp", "testVideo.mp4");
        final int targetHeight = 360;

        //when
        boolean result = ffmpegUtil.convertVideoProp(inputResource, outputPath, "libx264", 1, targetHeight);

        //then
        //검증1 성공?
        assertTrue(result);
        //검증2 파일 저장됨?
        FileSystemResource fileSystemResource = new FileSystemResource(outputPath);
        assertTrue(fileSystemResource.isFile());
        //검증3 파일이 설정한 height로 됨?
        FFmpegProbeResult outputResource = ffmpegUtil.getProbeResult(fileSystemResource);
        assertEquals(targetHeight,outputResource.getStreams().get(0).height);
    }
}