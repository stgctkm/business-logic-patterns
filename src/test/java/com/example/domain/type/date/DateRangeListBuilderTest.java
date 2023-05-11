package com.example.domain.type.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeListBuilderTest {

    @Test
    void 期間リストを作成する() {
        DateRangeList dateRangeList = new DateRangeListBuilder()
                .add(DateRange.fromTo(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-05")))
                .add(DateRange.fromTo(LocalDate.parse("2023-01-08"), LocalDate.parse("2023-01-08")))
                .add(DateRange.fromTo(LocalDate.parse("2023-01-06"), LocalDate.parse("2023-01-07")))
                .build();

        assertEquals(3, dateRangeList.list.size());
    }

    @Test
    void 重なっている期間を追加できない() {
        DateRangeListBuilder builder = new DateRangeListBuilder()
                .add(DateRange.fromTo(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-05")));
        assertThrows(IllegalArgumentException.class, () ->
            builder.add(DateRange.fromTo(LocalDate.parse("2023-01-05"), LocalDate.parse("2023-01-08")))
        );
    }

    @Test
    void 歯抜けの期間がある場合期間リストを作成できない() {
        DateRangeListBuilder builder = new DateRangeListBuilder()
                .add(DateRange.fromTo(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-05")))
                .add(DateRange.fromTo(LocalDate.parse("2023-01-08"), LocalDate.parse("2023-01-08")));

        assertThrows(IllegalStateException.class, builder::build);
    }
}