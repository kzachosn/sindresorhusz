package us.codecraft.webmagic.model;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.Formatter;
import us.codecraft.webmagic.model.formatter.DateFormatter;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafter@gmail.com
 *         Date: 2017/6/3
 *         Time: 下午9:06
 */
public class PageModelExtractorTest {

    private PageMocker pageMocker = new PageMocker();

    public static class ModelDateStr {

        @ExtractBy(value = "//div[@class='date']/text()", notNull = true)
        private String dateStr;

    }

    public static class ModelDate {

        @Formatter(value = "yyyyMMdd", formatter = DateFormatter.class)
        @ExtractBy(value = "//div[@class='date']/text()", notNull = true)
        private Date date;

    }

    public static class ModelInt {

        @ExtractBy(value = "//div[@class='number']/text()", notNull = true)
        private int number;

    }

    public static class ModelStringList {

        @ExtractBy("//a/@href")
        private List<String> links;

    }

    public static class ModelIntList {

        @Formatter(subClazz = Integer.class)
        @ExtractBy("//li[@class='numbers']/text()")
        private List<Integer> numbers;

    }

    public static class ModelDateList {

        @Formatter(subClazz = Date.class, value = "yyyyMMdd")
        @ExtractBy("//li[@class='dates']/text()")
        private List<Date> dates;

    }

    @Test
    public void testXpath() throws Exception {
        ModelDateStr modelDate = (ModelDateStr) PageModelExtractor.create(ModelDateStr.class).process(pageMocker.getMockPage());
        assertThat(modelDate.dateStr).isEqualTo("20170603");
    }

    @Test
    public void testExtractDate() throws Exception {
        ModelDate modelDate = (ModelDate) PageModelExtractor.create(ModelDate.class).process(pageMocker.getMockPage());
        assertThat(DateFormatUtils.format(modelDate.date,"yyyyMMdd")).isEqualTo("20170603");
    }

    @Test
    public void testExtractInt() throws Exception {
        ModelInt modelDate = (ModelInt) PageModelExtractor.create(ModelInt.class).process(pageMocker.getMockPage());
        assertThat(modelDate.number).isEqualTo(12);
    }

    @Test
    public void testExtractList() throws Exception {
        ModelStringList modelDate = (ModelStringList) PageModelExtractor.create(ModelStringList.class).process(pageMocker.getMockPage());
        assertThat(modelDate.links).hasSize(8);
    }

    @Test
    public void testExtractIntList() throws Exception {
        ModelIntList modelDate = (ModelIntList) PageModelExtractor.create(ModelIntList.class).process(pageMocker.getMockPage());
        assertThat(modelDate.numbers).hasSize(4);
    }

    @Test
    public void testExtractDateList() throws Exception {
        ModelDateList modelDate = (ModelDateList) PageModelExtractor.create(ModelDateList.class).process(pageMocker.getMockPage());
        assertThat(modelDate.dates).hasSize(4);
    }
}
