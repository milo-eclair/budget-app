package com.example.budgetapp.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汎用的なカレンダー処理のクラス
 */
public class Calendar {
	
	/**
	 * 日クラス
	 */
	 public static class Day {
        public int date;
        public int spendingTotal = 0;
        public List<Schedule> schedules = new ArrayList<>();
        public boolean isWeekend = false;
        public boolean isHoliday = false;
    }

	 /**
	  * スケジュールクラス
	  */
    public static class Schedule {
        public String scheduleTitle;
        public int scheduleAmount;
    }
    /**
     * 平日休日を含めたカレンダーを作成する
     * @param year
     * @param month
     * @return
     */
    public static List<List<Day>> generateCalendar(int year, int month) {
        List<List<Day>> weeks = new ArrayList<>();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);
        int daysInMonth = ym.lengthOfMonth();

        Map<LocalDate, Boolean> holidays = new HashMap<>();
        holidays.put(LocalDate.of(2025, 1, 1), true);
        holidays.put(LocalDate.of(2025, 12, 23), true);
        // 必要に応じて祝日を追加

        List<Day> week = new ArrayList<>();
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // 日曜=0
        for (int i = 0; i < firstDayOfWeek; i++) {
            week.add(null);
        }

        for (int dayNum = 1; dayNum <= daysInMonth; dayNum++) {
            LocalDate date = LocalDate.of(year, month, dayNum);
            Day day = new Day();
            day.date = dayNum;

            DayOfWeek dow = date.getDayOfWeek();
            day.isWeekend = (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY);

            day.isHoliday = holidays.containsKey(date);

            week.add(day);

            if (week.size() == 7) {
                weeks.add(week);
                week = new ArrayList<>();
            }
        }

        if (!week.isEmpty()) {
            while (week.size() < 7) {
                week.add(null);
            }
            weeks.add(week);
        }

        return weeks;
    }
}
