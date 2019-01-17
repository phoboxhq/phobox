package de.milchreis.phobox.server.api;

import de.milchreis.phobox.core.model.statistics.CountByYearItem;
import de.milchreis.phobox.core.model.statistics.ItemTagRatio;
import de.milchreis.phobox.core.model.statistics.ItemsInPeriod;
import de.milchreis.phobox.server.services.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/statistics/")
public class StatisticsController {

    @Autowired private IStatisticService statisticService;

    @RequestMapping(value = "year/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CountByYearItem> countItemsByCreationPeriod(@PathVariable("year") Integer year) {
        return statisticService.countItemsByYear(year);
    }

    @RequestMapping(value = "count/{start}/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ItemsInPeriod countItemsByCreationPeriod(@PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        return statisticService.countItemsByCreationPeriod(
                statisticService.convertToLocalDate(start),
                statisticService.convertToLocalDate(end));
    }

    @RequestMapping(value = "items/{start}/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ItemsInPeriod findItemsByCreationPeriod(@PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        return statisticService.findItemsByCreationPeriod(
                statisticService.convertToLocalDate(start),
                statisticService.convertToLocalDate(end));
    }

    @RequestMapping(value = "count/{start}/{end}/{camera}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ItemsInPeriod countItemsByCreationPeriod(@PathVariable("camera") String camera, @PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        return statisticService.countItemsByCameraAndCreationPeriod(
                camera,
                statisticService.convertToLocalDate(start),
                statisticService.convertToLocalDate(end));
    }

    @RequestMapping(value = "items/{start}/{end}/{camera}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ItemsInPeriod findItemsByCreationPeriod(@PathVariable("camera") String camera, @PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        return statisticService.findItemsByCameraAndCreationPeriod(
                camera,
                statisticService.convertToLocalDate(start),
                statisticService.convertToLocalDate(end));
    }

    @RequestMapping(value = "tagging", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ItemTagRatio getTaggingState() {
        return statisticService.countItems();
    }

}
