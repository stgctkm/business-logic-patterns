package com.example.domain.type.date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;

public class DateRangeTest {

    @Nested
    class ファクトリーメソッド {
        @Test
        @DisplayName("開始終了のファクトリメソッド")
        void fromTo() {
            LocalDate date20180901 = LocalDate.of(2018, 9, 1);
            LocalDate date20180902 = LocalDate.of(2018, 9, 2);
            DateRange actual = DateRange.fromTo(date20180901, date20180902);
            assertEquals(date20180901, actual.start);
            assertEquals(date20180902, actual.end);
        }

        @Test
        @DisplayName("開始終了のファクトリメソッド 同値")
        void fromToBySameDate() {
            LocalDate start20180901 = LocalDate.of(2018, 9, 1);
            LocalDate end20180901 = LocalDate.of(2018, 9, 1);
            DateRange actual = DateRange.fromTo(start20180901, end20180901);
            assertEquals(start20180901, actual.start);
            assertEquals(end20180901, actual.end);
        }

        @Test
        @DisplayName("開始終了のファクトリメソッド 開始＞終了で例外")
        void fromToIllegalArgument() {
            LocalDate date20180902 = LocalDate.of(2018, 9, 2);
            LocalDate date20180901 = LocalDate.of(2018, 9, 1);
            assertThrows(IllegalArgumentException.class, () -> DateRange.fromTo(date20180902, date20180901));
        }

        @Test
        @DisplayName("昨日から今日までのファクトリメソッド")
        void from() {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDate today = LocalDate.now();
            DateRange actual = DateRange.from(yesterday);
            assertEquals(yesterday, actual.start);
            assertEquals(today, actual.end);
        }

        @Test
        @DisplayName("開始（今日）から今日のファクトリメソッド 同値")
        void fromByToday() {
            LocalDate today = LocalDate.now();
            DateRange actual = DateRange.from(today);
            assertEquals(today, actual.start);
            assertEquals(today, actual.end);
        }

        @Test
        @DisplayName("明日から今日のファクトリメソッド 開始＞終了で例外")
        void fromIllegalArgument() {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            assertThrows(IllegalArgumentException.class, () -> DateRange.from(tomorrow));
        }

        @Test
        @DisplayName("今日から明日までのファクトリメソッド")
        void to() {
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            DateRange actual = DateRange.to(tomorrow);
            assertEquals(today, actual.start);
            assertEquals(tomorrow, actual.end);
        }

        @Test
        @DisplayName("今日から終了（今日）のファクトリメソッド 同値")
        void toByToday() {
            LocalDate today = LocalDate.now();
            DateRange actual = DateRange.to(today);
            assertEquals(today, actual.start);
            assertEquals(today, actual.end);
        }

        @Test
        @DisplayName("今日から昨日のファクトリメソッド 開始＞終了で例外")
        void toIllegalArgument() {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            assertThrows(IllegalArgumentException.class, () -> DateRange.to(yesterday));
        }
    }

    @Nested
    class 日付の期間範囲判定 {
        @Test
        @DisplayName("期間内の判定")
        void contains() {
            LocalDate date20180830 = LocalDate.of(2018, 8, 30);
            LocalDate date20180831 = LocalDate.of(2018, 8, 31);
            LocalDate other20180831 = LocalDate.of(2018, 8, 31);
            LocalDate date20180901 = LocalDate.of(2018, 9, 1);
            LocalDate date20180902 = LocalDate.of(2018, 9, 2);
            LocalDate other20180902 = LocalDate.of(2018, 9, 2);
            LocalDate date20180903 = LocalDate.of(2018, 9, 3);
            DateRange range0831to0902 = DateRange.fromTo(date20180831, date20180902);

            assertAll(
                    () -> assertFalse(range0831to0902.contains(date20180830), "期間前"),
                    () -> assertTrue(range0831to0902.contains(other20180831), "期間開始日"),
                    () -> assertTrue(range0831to0902.contains(date20180901), "期間中"),
                    () -> assertTrue(range0831to0902.contains(other20180902), "期間終了日"),
                    () -> assertFalse(range0831to0902.contains(date20180903), "期間後")
            );
        }

        @Test
        @DisplayName("期間前の判定")
        void beforeStart() {
            LocalDate date20180830 = LocalDate.of(2018, 8, 30);
            LocalDate date20180831 = LocalDate.of(2018, 8, 31);
            LocalDate other20180831 = LocalDate.of(2018, 8, 31);
            LocalDate date20180901 = LocalDate.of(2018, 9, 1);
            LocalDate date20180902 = LocalDate.of(2018, 9, 2);
            LocalDate other20180902 = LocalDate.of(2018, 9, 2);
            LocalDate date20180903 = LocalDate.of(2018, 9, 3);
            DateRange range0831to0902 = DateRange.fromTo(date20180831, date20180902);

            assertAll(
                    () -> assertTrue(range0831to0902.isAfter(date20180830), "期間前"),
                    () -> assertFalse(range0831to0902.isAfter(other20180831), "期間開始日"),
                    () -> assertFalse(range0831to0902.isAfter(date20180901), "期間中"),
                    () -> assertFalse(range0831to0902.isAfter(other20180902), "期間終了日"),
                    () -> assertFalse(range0831to0902.isAfter(date20180903), "期間後")
            );
        }

        @Test
        @DisplayName("期間後の判定")
        void afterEnd() {
            LocalDate date20180830 = LocalDate.of(2018, 8, 30);
            LocalDate date20180831 = LocalDate.of(2018, 8, 31);
            LocalDate other20180831 = LocalDate.of(2018, 8, 31);
            LocalDate date20180901 = LocalDate.of(2018, 9, 1);
            LocalDate date20180902 = LocalDate.of(2018, 9, 2);
            LocalDate other20180902 = LocalDate.of(2018, 9, 2);
            LocalDate date20180903 = LocalDate.of(2018, 9, 3);
            DateRange range0831to0902 = DateRange.fromTo(date20180831, date20180902);

            assertAll(
                    () -> assertFalse(range0831to0902.isBefore(date20180830), "期間前"),
                    () -> assertFalse(range0831to0902.isBefore(other20180831), "期間開始日"),
                    () -> assertFalse(range0831to0902.isBefore(date20180901), "期間中"),
                    () -> assertFalse(range0831to0902.isBefore(other20180902), "期間終了日"),
                    () -> assertTrue(range0831to0902.isBefore(date20180903), "期間後")
            );
        }
    }

    @Nested
    class 期間の期間範囲判定 {

        DateRange sut = DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-12"));
        @ParameterizedTest
        @MethodSource("rangeForContainsTest")
        void 与えられた期間が期間内にあるか(String message, DateRange input, boolean expected) {
            assertEquals(expected, sut.contains(input), message);
        }

        static List<Arguments> rangeForContainsTest() {
            return List.of(
                    arguments("開始日、終了日ともに期間内", DateRange.fromTo(LocalDate.parse("2023-04-22"), LocalDate.parse("2023-05-11")), true),
                    arguments("開始日、終了日とも同一の期間", DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-12")), true),
                    arguments("開始日が期間外、終了日が期間内", DateRange.fromTo(LocalDate.parse("2023-04-20"), LocalDate.parse("2023-05-12")), false),
                    arguments("開始日が期間内、終了日が期間外", DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-13")), false),
                    arguments( "対象期間を含む期間", DateRange.fromTo(LocalDate.parse("2000-04-29"), LocalDate.parse("2100-05-01")), false),
                    arguments( "重なりのない期間", DateRange.fromTo(LocalDate.parse("2022-04-29"), LocalDate.parse("2022-05-01")), false)
            );
        }
    }

    @Nested
    class 重なっている期間の返却 {
        DateRange sut = DateRange.fromTo(LocalDate.parse("2023-04-29"), LocalDate.parse("2023-05-07"));

        @ParameterizedTest
        @MethodSource("テスト対象期間")
        void 重なっている期間を返却する(String message, DateRange other, DateRange expected) {
            assertTrue(expected.isSame(sut.intersectionWith(other)), message);
        }

        static List<Arguments> テスト対象期間() {
            return List.of(
                    arguments("期間内", DateRange.fromTo(LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-02")), DateRange.fromTo(LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-02"))),
                    arguments("開始日が期間内", DateRange.fromTo(LocalDate.parse("2023-05-03"), LocalDate.parse("2023-05-12")), DateRange.fromTo(LocalDate.parse("2023-05-03"), LocalDate.parse("2023-05-07"))),
                    arguments("終了日が期間内", DateRange.fromTo(LocalDate.parse("2023-04-20"), LocalDate.parse("2023-05-01")), DateRange.fromTo(LocalDate.parse("2023-04-29"), LocalDate.parse("2023-05-01")))
            );
        }

        @Test
        void 重なっている期間がない場合にはIllegalArgumentExceptionを返却する() {
            DateRange other = DateRange.fromTo(LocalDate.parse("2024-05-01"), LocalDate.parse("2024-05-02"));
            assertThrows(IllegalArgumentException.class, () -> sut.intersectionWith(other));
        }

        @Test
        void 同じ期間を指定された場合には期間を返却する() {
            DateRange other = DateRange.fromTo(LocalDate.parse("2023-04-29"), LocalDate.parse("2023-05-07"));
            assertTrue(sut.isSame(sut.intersectionWith(other)));
        }

    }

    @Nested
    class 期間が重なっているか {
        DateRange sut = DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-12"));
        @ParameterizedTest
        @MethodSource("重なっている期間のテスト期間")
        void 期間が重なっている(String message, DateRange other, boolean expected) {
            assertEquals(expected, sut.isOverLappedBy(other), message);
        }

        static List<Arguments> 重なっている期間のテスト期間() {
            return List.of(
                    arguments("開始日、終了日ともに期間内", DateRange.fromTo(LocalDate.parse("2023-04-22"), LocalDate.parse("2023-05-11")), true),
                    arguments("開始日、終了日とも同一の期間", DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-12")), true),
                    arguments("開始日が期間外、終了日が期間内", DateRange.fromTo(LocalDate.parse("2023-04-20"), LocalDate.parse("2023-05-12")), true),
                    arguments("開始日が期間内、終了日が期間外", DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-13")), true),
                    arguments( "対象期間を含む期間", DateRange.fromTo(LocalDate.parse("2000-04-29"), LocalDate.parse("2100-05-01")), true),
                    arguments( "重なりのない期間", DateRange.fromTo(LocalDate.parse("2022-04-29"), LocalDate.parse("2022-05-01")), false)
            );
        }
    }

    @Nested
    class 期間の前後関係 {

        @Nested
        class 期間前の判定 {
            DateRange sut = DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-12"));

            @Test
            void 指定された期間より前の期間であればtrueを返却する() {
                DateRange laterRange = DateRange.fromTo(LocalDate.parse("2023-05-21"), LocalDate.parse("2023-06-12"));
                assertTrue(sut.isBefore(laterRange));
            }

            @Test
            void 一部でも重なっている期間の場合はfalseを返却する() {
                DateRange laterRange = DateRange.fromTo(LocalDate.parse("2023-05-12"), LocalDate.parse("2023-06-12"));
                assertFalse(sut.isBefore(laterRange));
            }
        }

        @Nested
        class 期間後の判定 {
            DateRange sut = DateRange.fromTo(LocalDate.parse("2023-04-21"), LocalDate.parse("2023-05-12"));

            @Test
            void 指定された期間より後の期間であればtrueを返却する() {
                DateRange laterRange = DateRange.fromTo(LocalDate.parse("2023-01-21"), LocalDate.parse("2023-02-12"));
                assertTrue(sut.isAfter(laterRange));
            }

            @Test
            void 一部でも重なっている期間の場合はfalseを返却する() {
                DateRange laterRange = DateRange.fromTo(LocalDate.parse("2023-03-29"), LocalDate.parse("2023-04-21"));
                assertFalse(sut.isAfter(laterRange));
            }
        }

    }

}
