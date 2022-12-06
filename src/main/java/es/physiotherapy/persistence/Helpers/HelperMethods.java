package es.physiotherapy.persistence.Helpers;

import es.physiotherapy.persistence.entity.TreatedArea;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class HelperMethods {
    public static TreatedArea treatedAreaBuilder(String[] areas, String observations) {
        TreatedArea treatedArea = new TreatedArea();
        for (String area : areas) {
            try {
                Field field = TreatedArea.class.getDeclaredField(area.trim());
                field.setAccessible(true);
                field.set(treatedArea, true);
            } catch (NoSuchFieldException e) {
                System.err.println("The area " + area + " does not exist");
            } catch (IllegalAccessException e) {
                System.err.println("The area " + area + " is not accessible");
            }
        }
        treatedArea.setObservations(observations);
        return treatedArea;
    }

    public static Time timeParser(String time) {
        byte timeSplitLength = (byte) time.split(":").length;
        if (timeSplitLength < 2) time += ":00";
        LocalTime localTime = LocalTime.parse(time);
        return Time.valueOf(localTime);
    }

    public static LocalDate dateParser(String date) {
        date = date.replaceAll("/", "-")
                .replaceAll("\s+", "");
        return LocalDate.parse(date);
    }
}
