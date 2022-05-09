package exam.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeService {

    public static final long MILLISECONDS_24_HOURS = 2*24*60*60*1000;
    public static final String TIME_ZONE_EUROPE_VILNIUS = "Europe/Vilnius";
    
    public DateTimeService(){

    }

    /**Compare dates and times */
    public int compare(String dateTime1, String dateTime2, long differenceMilliseconds) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long time1 = sdf.parse(dateTime1).getTime();
        long time2 = sdf.parse(dateTime2).getTime();
        long time = (time1-time2)-differenceMilliseconds;
        
        if(time == 0) return 0;
        if(time > 0) return 1;
        return -1;
    }


    /**Get current date */
    public String now(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(DateTimeService.TIME_ZONE_EUROPE_VILNIUS));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
