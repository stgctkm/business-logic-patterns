package com.example.domain.type.date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 期間リストBuilder
 */
public class DateRangeListBuilder {

    List<DateRange> list;

    public DateRangeListBuilder() {
        this.list = new ArrayList<>();
    }

    /**
     * 期間リストを返却する
     * 期間に含まれない日付がある場合、IllegalStateExceptionを返却する
     */
    public DateRangeList build() {
        sort();
        if (totalDaysDateRangeList() != totalDaysFromOldestStartDateToLatestEndDate())
            throw new IllegalStateException("全ての日をカバーしていません");
        return new DateRangeList(list);
    }

    /**
     * 期間を追加する
     * 指定した期間がすでに追加した期間と重複している場合、IllegalArgumentExceptionを返却する
     */
    public DateRangeListBuilder add(DateRange dateRange) {
        if (list.stream().anyMatch(range -> range.isOverLappedBy(dateRange))) {
            throw new IllegalArgumentException("期間が重なっています");
        }
        list.add(dateRange);
        return this;
    }

    void sort() {
        this.list = list.stream().sorted(Comparator.comparing(range -> range.start)).toList();
    }

    long totalDaysFromOldestStartDateToLatestEndDate() {
        LocalDate oldestStartDate = list.stream()
                .map(range -> range.start)
                .min(LocalDate::compareTo)
                .orElseThrow(IllegalStateException::new);
        LocalDate latestEndDate = list.stream()
                .map(range -> range.end)
                .max(LocalDate::compareTo)
                .orElseThrow(IllegalStateException::new);
        return DateRange.fromTo(oldestStartDate, latestEndDate).toTotalDays();
    }

    long totalDaysDateRangeList() {
        return list.stream().map(DateRange::toTotalDays).reduce(Math::addExact).orElse(0L);
    }
}
