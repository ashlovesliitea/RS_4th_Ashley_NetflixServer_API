package com.example.demo.src.video;

import com.example.demo.src.video.model.entity.Episode;
import com.example.demo.src.video.model.entity.Video;
import com.example.demo.src.video.model.response.GetMoviesRes;
import com.example.demo.src.video.model.response.GetSeriesRes;
import com.example.demo.src.video.model.response.GetVideoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VideoDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetVideoRes getVideo(int videoId){
        System.out.println(videoId);
        Video video=findVideo(videoId);

        int isSeries=video.getVideo_series_status();
        System.out.println("isSeries = " + isSeries);
        if(isSeries==0){
            return new GetVideoRes(video, null);
        }
        else{
            List<Episode> episode=getEpisode(videoId);
            return new GetVideoRes(video,episode);
        }

    }

   public List<GetVideoRes> getVideoList() {
        String idListQuery="Select video_id from Video";
        List<Integer>VideoIdList= this.jdbcTemplate.query(idListQuery,
                (rs,rowNum)->{
                return rs.getInt("video_id");
                });

        List<GetVideoRes>VideoList=new ArrayList<>();
        for(int id:VideoIdList){
            Video video=findVideo(id);
            VideoList.add(new GetVideoRes(video,null));
        }
       System.out.println("VideoList = " + VideoList);
        return VideoList;
   }

    public List<GetSeriesRes> getVideoSeriesList() {
        String idListQuery="Select video_id from Video where video_series_status=1";
        List<Integer>VideoIdList= this.jdbcTemplate.query(idListQuery,
                (rs,rowNum)->{
                    return rs.getInt("video_id");
                });

        List<GetSeriesRes>VideoList=new ArrayList<>();
        for(int id:VideoIdList){
            Video video=findVideo(id);
            VideoList.add(new GetSeriesRes(video));
        }
        return VideoList;
    }


    public List<GetMoviesRes> getVideoMoviesList() {
        String idListQuery="Select video_id from Video where video_series_status=0";
        List<Integer>VideoIdList= this.jdbcTemplate.query(idListQuery,
                (rs,rowNum)->{
                    return rs.getInt("video_id");
                });

        List<GetMoviesRes>VideoList=new ArrayList<>();
        for(int id:VideoIdList){
            Video video=findVideo(id);
            VideoList.add(new GetMoviesRes(video));
        }
        return VideoList;
    }

    public Video findVideo(int videoId){
        String getVideoQuery="SELECT V.video_title,V.video_date,V.video_pg,V.video_plot,ifnull(V.video_series_num,0) as series_num, " +
                "V.video_series_status,ifnull(V.video_running_time,0) as running_time,V.video_trailer_url,V.video_thumbnail_url " +
                "FROM Video V " +
                "WHERE V.video_id=?";

        String getVideoActorsQuery="SELECT actor_name from Video V " +
                "INNER JOIN (SELECT video_id,actor_name FROM Actor " +
                "INNER JOIN Casting ON Actor.actor_id=Casting.actor_id) " +
                "A ON V.video_id=A.video_id "+
                "Where V.video_id=?";

        String getVideoCharQuery = "Select char_name from Video V "+
                "INNER JOIN (SELECT video_id,char_name FROM Characteristic " +
                "INNER JOIN Video_Characteristic ON Characteristic.char_id=Video_Characteristic.char_id " +
                ") C ON V.video_id=C.video_id Where V.video_id=?";

        String getVideoGenreQuery="Select genre_name from Video V "+
                "INNER JOIN (SELECT video_id,genre_name FROM Genre " +
                "INNER JOIN Video_Genre ON Genre.genre_id=Video_Genre.genre_id " +
                ") G ON V.video_id=G.video_id where V.video_id=?";


        List<String> actorList=this.jdbcTemplate.query(getVideoActorsQuery,
                (rs,rowNum)-> {
            String actor_name=rs.getString("actor_name");
            return actor_name;},videoId);

        List<String> charList=this.jdbcTemplate.query(getVideoCharQuery,
                (rs,rowNum)->{
                    String char_name=rs.getString("char_name");
                    return char_name;},videoId);

        List<String> genreList=this.jdbcTemplate.query(getVideoGenreQuery,
                (rs,rowNum)->{
                    return rs.getString("genre_name");
                },videoId);


        return this.jdbcTemplate.queryForObject(getVideoQuery,
                (rs,rowNum)->{
                        String title= rs.getString("video_title");
                        String date=rs.getString("video_date");
                        int pg=rs.getInt("video_pg");
                        String plot=rs.getString("video_plot");
                        int series_num=rs.getInt("series_num");
                        int series_status=rs.getInt("video_series_status");
                        Time time;
                        if(series_status==1){
                             time= Time.valueOf("00:00:00");
                        }
                        else{ time=rs.getTime("running_time");}
                        String url1=rs.getString("video_trailer_url");
                        String url2=rs.getString("video_thumbnail_url");
                        System.out.println(title+" "+date+" "+pg+" "+series_num+" "+series_status+" "+time+" "+url1+" "+url2);
                        return new Video(title,date,pg,plot,series_num,series_status,time,url1,url2,actorList,charList,genreList);
                       },videoId);


    }



    public List<Episode> getEpisode(int videoId){
       String findEpisodeQuery="SELECT season_num,episode_num,episode_title,running_time,episode_url,episode_plot\n" +
               "FROM Episode E\n" +
               "INNER JOIN Video V ON E.video_id=V.video_id\n" +
               "WHERE E.video_id=?";

       return this.jdbcTemplate.query(findEpisodeQuery,
               (rs,rowNum)->new Episode(
                       rs.getInt("season_num"),
                       rs.getInt("episode_num"),
                       rs.getString("episode_title"),
                       rs.getTime("running_time"),
                       rs.getString("episode_url"),
                       rs.getString("episode_plot")
               ),videoId);
    }
}
