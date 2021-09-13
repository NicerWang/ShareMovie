package nk.bigdata.backend.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRateData {
    String id;
    Date date;
    int dayCommentsNum;
    double dayAverRating;
    double accumulateAverRating;
}
