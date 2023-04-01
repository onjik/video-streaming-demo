package com.oj.videostreamingdemo.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class FFmpegUtil {
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;



    public FFmpegProbeResult getProbeResult(Resource resource){
        FFmpegProbeResult ffmpegProbeResult = null;

        try {
            ffmpegProbeResult = ffprobe.probe(resource.getURL().getPath());
        } catch (IOException e) {}
        return ffmpegProbeResult;
    }

    public boolean convertVideoProp(FFmpegProbeResult probeResult,Path outputPath, String codec, int audioChannel, double targetHeight){
        double ogHeight = probeResult.getStreams().get(0).height;
        double ogWidth = probeResult.getStreams().get(0).width;

        FFmpegBuilder builder = new FFmpegBuilder()
                //common
                .setInput(probeResult)
                .overrideOutputFiles(true)
                .addOutput(outputPath.toAbsolutePath().toString())
                .setFormat("mp4")
                .setStrict(FFmpegBuilder.Strict.NORMAL)
                .disableSubtitle()
                //video
                .setVideoCodec(codec)
                .setVideoResolution((int) (ogWidth * (targetHeight / ogHeight)) ,(int) targetHeight)
                .setVideoFrameRate(30,1)
                //audio
                .setAudioChannels(audioChannel) //모노? 스테레오?
                .setAudioSampleRate(48_000)
                .setAudioBitRate(32768)
                .done()
                .setVerbosity(FFmpegBuilder.Verbosity.QUIET);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg,ffprobe);
        FFmpegJob job = executor.createJob(builder);

        job.run();

        if (job.getState() == FFmpegJob.State.FINISHED){
            return true;
        } else {
            return false;
        }
    }

}
