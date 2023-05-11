package com.example.domain.type.date;

import java.time.LocalDate;

/**
 * 期間
 */
public class DateRange {

    LocalDate start;
    LocalDate end;

    private DateRange(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) throw new IllegalArgumentException();
        this.start = start;
        this.end = end;
    }

    public static DateRange fromTo(LocalDate start, LocalDate end) {
        return new DateRange(start, end);
    }

    public static DateRange from(LocalDate start) {
        return new DateRange(start, LocalDate.now());
    }

    public static DateRange to(LocalDate end) {
        return new DateRange(LocalDate.now(), end);
    }

    /**
     * 日付が期間内であるかを返却する
     */
    public boolean contains(LocalDate date) {
        return !isAfter(date) && !isBefore(date);
    }

    /**
     * 期間が指定された日付後であるかを返却する
     *
     * 日付が期間前であるかを返却する
     */
    public boolean isAfter(LocalDate date) {
        return date.isBefore(start);
    }

    /**
     * 期間が指定された日付前であるかを返却する
     *
     * 日付が期間後であるかを返却する
     */
    public boolean isBefore(LocalDate date) {
        return date.isAfter(end);
    }

    /**
     * 引数の期間を含むかを返却する
     */
    public boolean contains(DateRange other) {
        return contains(other.start) && contains(other.end);
    }

    /**
     * 期間が指定された期間より後であるかを返却する
     */
    public boolean isAfter(DateRange other) {
        return start.isAfter(other.end);
    }

    /**
     * 期間が指定された期間より前であるかを返却する
     */
    public boolean isBefore(DateRange other) {
        return end.isBefore(other.start);
    }

    /**
     * 期間が重なっているかを返却する
     */
    public boolean isOverLappedBy(DateRange other) {
        return !start.isAfter(other.end) && !end.isBefore(other.start);
    }

    /**
     * 重なっている期間を返却する
     */
    public DateRange intersectionWith(DateRange other) {
        if (!isOverLappedBy(other))
            throw new IllegalArgumentException("%s is not over rapped %s".formatted(this, other));

        LocalDate from = later(start, other.start);
        LocalDate to = former(end, other.end);
        return DateRange.fromTo(from, to);
    }

    public LocalDate first() {
        return start;
    }

    public LocalDate last() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", start, end);
    }

    public boolean isSame(DateRange other) {
        return start.isEqual(other.start) && end.isEqual(other.end);
    }

    /**
     * より前の日付を返却する
     */
    LocalDate former(LocalDate one, LocalDate other) {
        if (one.isBefore(other)) return one;
        return other;
    }

    /**
     * より後ろの日付返却する
     */
    LocalDate later(LocalDate one, LocalDate other) {
        if (one.isBefore(other)) return other;
        return one;
    }

}
