package com.oj.videostreamingdemo.domain.video.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
public class VideoDownloadController {
    private final String FILE_NAME = "file_example_MP4_1920_18MG.mp4";

    //이렇게 동영상을 응답하면 브라우저에서 해당 동영상을 보여주지만 특정 플레이 위치로 이동하거나 할 수 없고 용량이 일정 크기를 넘어버리면 브라우저가 알아서 초반부터 데이터를 순차적으로 가져온다.
    @GetMapping("/video/srb/v1")
    public ResponseEntity<StreamingResponseBody> videoWithStreamingResponseBodyV1() throws IOException{
        //파일 불러오기
        FileSystemResource file = new FileSystemResource(Paths.get("video-files",FILE_NAME));
        //파일 존재하는지
        if (!file.isFile()){
            return ResponseEntity.notFound().build();
        }
         StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
             @Override
             public void writeTo(OutputStream outputStream) throws IOException {
                 try {
                     final InputStream inputStream = file.getInputStream();
                     byte[] bytes = new byte[1024];
                     int length;
                     while ((length = inputStream.read(bytes)) >= 0){
                         outputStream.write(bytes,0,length);
                     }
                     inputStream.close();
                     outputStream.flush();
                 } catch (Exception e){
                     log.error("Exception while reading and streaming data {} ", e);
                 }
             }
         };

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .contentLength(file.contentLength()) //throw IOException
                .body(streamingResponseBody);
    }
    //이렇게 동영상을 응답하면 브라우저에서 해당 동영상을 보여주지만 특정 플레이 위치로 이동하거나 할 수 없고 용량이 일정 크기를 넘어버리면 브라우저가 알아서 초반부터 데이터를 순차적으로 가져온다.
    @GetMapping("/video/srb/v2")
    public ResponseEntity<StreamingResponseBody> videoWithStreamingResponseBodyV2() throws IOException{
        //파일 불러오기
        FileSystemResource file = new FileSystemResource(Paths.get("video-files",FILE_NAME));
        //파일 존재하는지
        if (!file.isFile()){
            return ResponseEntity.notFound().build();
        }
        StreamingResponseBody streamingResponseBody = outputStream -> FileCopyUtils.copy(file.getInputStream(),outputStream);
        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .contentLength(file.contentLength()) //throw IOException
                .body(streamingResponseBody);
    }

    @GetMapping("/video/region/v1")
    public ResponseEntity<ResourceRegion> videoWithResourceRegionV1(@RequestHeader(HttpHeaders.ACCEPT_RANGES) HttpHeaders httpHeaders) throws IOException {
        //파일 불러오기
        FileSystemResource file = new FileSystemResource(Paths.get("video-files",FILE_NAME));
        ResourceRegion resourceRegion = this.resourceRegion(file,httpHeaders);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }

    /*
    이 메서드는 HttpRange.toResourceRegions() static method로 대체 가능
     */
    private ResourceRegion resourceRegion(final Resource video, HttpHeaders httpHeaders)throws IOException{
        final Long chunkSize = 1000000L;
        long contentLength = video.contentLength();
        if (httpHeaders.getRange().isEmpty()){
            return new ResourceRegion(video,0,Math.min(chunkSize, contentLength));
        }
        HttpRange httpRange = httpHeaders.getRange().stream().findFirst().get();
        long rangeStart = httpRange.getRangeStart(contentLength);
        long rangeEnd = httpRange.getRangeEnd(contentLength);
        long rangeLength = Math.min(chunkSize, rangeEnd - rangeStart+1);
        return new ResourceRegion(video, rangeStart, rangeLength);
    }

    //더 간단하게 같은 기능 구현
    @GetMapping("/video/region/v2")
    public ResponseEntity<List<ResourceRegion>> videoWithResourceRegionV2(@RequestHeader(HttpHeaders.ACCEPT_RANGES) HttpHeaders httpHeaders) throws IOException {
        //파일 불러오기
        FileSystemResource file = new FileSystemResource(Paths.get("video-files",FILE_NAME));
        //ResourceRegions
        final Long chunkSize = 1000000L;
        long contentLength = file.contentLength();
        List<ResourceRegion> resourceRegions;
        if (httpHeaders.getRange().isEmpty()){
            resourceRegions = List.of(new ResourceRegion(file, 0, Math.min(chunkSize, contentLength)));
        } else {
            resourceRegions = HttpRange.toResourceRegions(httpHeaders.getRange(), file);
        }
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegions);
    }
}
