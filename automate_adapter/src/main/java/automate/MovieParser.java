package automate;

import nk.bigdata.backend.domain.Movie;

import java.io.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class MovieParser {
    static DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    public static Movie parseMovie(String movieId){

        File csv = new File(  movieId + "/info.csv");
        Movie movie = new Movie();
        try{
            UploadUtil.upload(movieId);
            BufferedReader textFile = new BufferedReader(new FileReader(csv));
            String lineDta = textFile.readLine();
            String[] attrs = lineDta.split(",");
            movie.setId(movieId);
            movie.setName(attrs[0]);
            movie.setDirector(attrs[1]);
            movie.setEditor(attrs[2]);
            movie.setActors(attrs[3]);
            movie.setGenres(attrs[4]);
            movie.setReleaseDate(new Date(fmt.parse(attrs[5]).getTime()));
            movie.setImgLink("https://pictures-nicerwang-1256891306.cos.ap-beijing.myqcloud.com/proj/" + movieId + ".png");
        } catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return movie;
    }
}
