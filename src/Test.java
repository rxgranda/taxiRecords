import javafx.scene.input.DataFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;


public class Test {

    public static void main(String[]args) {
        ZoneId zoneId = ZoneId.of("Europe/Paris");
        //TimeZone tz=TimeZone.getTimeZone("Europe/Paris");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime a= LocalDateTime.parse("2019-03-31 01:59:00",formatter);
        LocalDateTime b= LocalDateTime.parse("2019-03-31 03:00:00",formatter);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(a, zoneId);
        ZonedDateTime zonedDateTimeb = ZonedDateTime.of(b, zoneId);
        //String str="2010-02-28 23:46:08";
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //sdf.setTimeZone(tz);
        //Date fecha=sdf.parse(str);
        //System.out.println(fecha);
        //System.out.println(a);
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTimeb);
        long dur = Duration.between(zonedDateTime, zonedDateTimeb).getSeconds();
        System.out.println(dur);


    }
}
