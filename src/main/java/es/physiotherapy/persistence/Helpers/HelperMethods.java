package es.physiotherapy.persistence.Helpers;

import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.entity.TreatedArea_;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HelperMethods {
    public static TreatedArea treatedAreaBuilder(String[] areas, String observations) {
        TreatedArea treatedArea = new TreatedArea();
        for (String area : areas) {
            try {
                Field field = TreatedArea.class.getDeclaredField(area.trim());
                field.setAccessible(true);
                field.set(treatedArea, true);
            } catch (NoSuchFieldException e) {
                System.err.println(ASCIIColors.RED.getColor() +
                        "The area " + area + " does not exist" +
                        ASCIIColors.RESET.getColor());
            } catch (IllegalAccessException e) {
                System.err.println(ASCIIColors.RED.getColor() +
                        "The area " + area + " is not accessible" +
                        ASCIIColors.RESET.getColor());
            }
        }
        if (observations != null && observations.isBlank()) observations = null;
        treatedArea.setObservations(observations);
        return treatedArea;
    }

    public static Time timeParser(String time) {
        byte timeSplitLength = (byte) time.split(":").length;
        if (timeSplitLength < 2) time += ":00";
        LocalTime localTime = LocalTime.parse(time);
        return Time.valueOf(localTime);
    }

    public static List<String> getListOfValidTreatedAreaStrings(String[] areaStrings){
        List<String> areas = new ArrayList<>();
        for (String area : areaStrings) {
            try {
                area = area.trim().toLowerCase();
                TreatedArea.class.getDeclaredField(area);
                areas.add(area);
            } catch (NoSuchFieldException e) {
                System.err.println(ASCIIColors.RED.getColor() +
                        "The area " + area + " does not exist" +
                        ASCIIColors.RESET.getColor());
            }
        }
        return areas;
    }

    public static LocalDate dateParser(String date) {
        date = date.replaceAll("/", "-")
                .replaceAll("\s+", "");
        return LocalDate.parse(date);
    }
}
