package nk.bigdata.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable {
    private static final long serialVersionUID = 1;
    String id;
    String name;
    Date releaseDate;
    String director;
    String editor;
    String genres;
    String imgLink;
    String actors;
}
