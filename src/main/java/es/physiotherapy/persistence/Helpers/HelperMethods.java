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

    public static List<String> getListOfValidTreatedAreaStrings(String[] areaStrings){
        List<String> areas = new ArrayList<>();
        for (String area : areaStrings) {
            switch (area.toLowerCase()) {
                case "cervical" -> areas.add(TreatedArea_.CERVICAL);
                case "dorsal" -> areas.add(TreatedArea_.DORSAL);
                case "lumbar" -> areas.add(TreatedArea_.LUMBAR);
                case "sacroiliac" -> areas.add(TreatedArea_.SACROILIAC);
                case "shoulder" -> areas.add(TreatedArea_.SHOULDER);
                case "elbow" -> areas.add(TreatedArea_.ELBOW);
                case "wrist" -> areas.add(TreatedArea_.WRIST);
                case "hand" -> areas.add(TreatedArea_.HAND);
                case "hip" -> areas.add(TreatedArea_.HIP);
                case "knee" -> areas.add(TreatedArea_.KNEE);
                case "ankle" -> areas.add(TreatedArea_.ANKLE);
                case "foot" -> areas.add(TreatedArea_.FOOT);
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
