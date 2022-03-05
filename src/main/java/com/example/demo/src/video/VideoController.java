package com.example.demo.src.video;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.video.model.GetMoviesRes;
import com.example.demo.src.video.model.GetSeriesRes;
import com.example.demo.src.video.model.GetVideoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/videos")
public class VideoController {
    final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final VideoProvider videoProvider;
    private final VideoService videoService;

   @Autowired
    public VideoController(VideoProvider videoProvider, VideoService videoService) {
        this.videoProvider = videoProvider;
        this.videoService = videoService;
    }

    /**
     *
     * [GET]
     * 전체 비디오 조회 및 특정 비디오 조회 API
     * [GET] ?VideoId=
     * @return BaseResponse<List<GetVideoRes>>
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetVideoRes>> getVideos(@RequestParam(required=false) Integer videoId){
        try{
            System.out.println("videoId = " + videoId);
            if(videoId==null){
                List<GetVideoRes> getVideosRes=videoProvider.getVideoList();
                return new BaseResponse<>(getVideosRes);
            }
            List <GetVideoRes> getVideoRes =videoProvider.getVideo(videoId);
            return new BaseResponse<>(getVideoRes);
        }
        catch(BaseException exception){
            return new BaseResponse<>(GET_VIDEO_LIST_FAIL);
        }
    }

    /**
     *
     * [GET]
     * 시리즈물 조회 API
     * [GET] /genre/series
     * @return BaseResponse<List<GetSeriesRes>>
     */

    @ResponseBody
    @GetMapping("/genre/series")
    public BaseResponse<List<GetSeriesRes>> getSeries(){
        try{
            List <GetSeriesRes> getSeriesRes =videoProvider.getVideoSeriesList();
            return new BaseResponse<>(getSeriesRes);
        }
        catch(BaseException exception){
            return new BaseResponse<>(GET_VIDEO_LIST_FAIL);
        }
    }

    /**
     *
     * [GET]
     * 시리즈물 조회 API
     * [GET] /genre/movies
     * @return BaseResponse<List<GetMoviesRes>>
     */

    @ResponseBody
    @GetMapping("/genre/movies")
    public BaseResponse<List<GetMoviesRes>> getMovies(){
        try{
            List <GetMoviesRes> getMoviesRes =videoProvider.getVideoMoviesList();
            return new BaseResponse<>(getMoviesRes);
        }
        catch(BaseException exception){
            return new BaseResponse<>(GET_VIDEO_LIST_FAIL);
        }
    }

}
