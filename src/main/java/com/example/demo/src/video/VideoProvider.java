package com.example.demo.src.video;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.video.model.GetMoviesRes;
import com.example.demo.src.video.model.GetSeriesRes;
import com.example.demo.src.video.model.GetVideoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequestMapping("/app/videos")
public class VideoProvider {
    private final VideoDao videoDao;

    @Autowired
    public VideoProvider(VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    public List<GetVideoRes>getVideoList() throws BaseException{
        try{
            List<GetVideoRes> acc=videoDao.getVideoList();
            return acc;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetVideoRes>getVideo(int videoId) throws BaseException{
        try{
            System.out.println("videoId = " + videoId);
            GetVideoRes acc=videoDao.getVideo(videoId);
            List<GetVideoRes> accList= new ArrayList<>();
            accList.add(acc);
            return accList;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSeriesRes>getVideoSeriesList() throws BaseException{
        try{
            List<GetSeriesRes> acc=videoDao.getVideoSeriesList();
            return acc;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMoviesRes>getVideoMoviesList() throws BaseException{
        try{
            List<GetMoviesRes> acc=videoDao.getVideoMoviesList();
            return acc;
        } catch(Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
