package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.model.statistics.CountByYearItem;
import de.milchreis.phobox.core.model.statistics.ItemTagRatio;
import de.milchreis.phobox.core.model.statistics.ItemsInPeriod;
import de.milchreis.phobox.db.StatisticsDAO;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.db.repositories.ItemTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class StatisticService implements IStatisticService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemTagRepository itemTagRepository;
    @Autowired private StatisticsDAO statisticsDAO;

    @Override
    public ItemsInPeriod countItemsByCreationPeriod(LocalDate start, LocalDate end) {
        long numberOfItems = itemRepository.countItemsByCreationPeriod(convertToDate(start), convertToDate(end));
        return new ItemsInPeriod(start, end, numberOfItems);
    }

    @Override
    public ItemsInPeriod findItemsByCreationPeriod(LocalDate start, LocalDate end) {
        List<Item> items = itemRepository.findItemsByCreationPeriod(convertToDate(start), convertToDate(end));
        return new ItemsInPeriod(start, end, items);
    }

    @Override
    public List<Item> findItemsByCamera(String camera) {
        return itemRepository.findItemsByCamera(camera);
    }

    @Override
    public ItemsInPeriod findItemsByCameraAndCreationPeriod(String camera, LocalDate start, LocalDate end) {
        List<Item> items = itemRepository.findItemsByCameraAndCreationPeriod(camera, convertToDate(start), convertToDate(end));
        return new ItemsInPeriod(start, end, items);
    }

    @Override
    public ItemsInPeriod countItemsByCamera(String camera) {
        long numberOfItems = itemRepository.countItemsByCamera(camera);
        return new ItemsInPeriod(LocalDate.of(0, 1, 1), LocalDate.now(), numberOfItems);
    }

    @Override
    public ItemsInPeriod countItemsByCameraAndCreationPeriod(String camera, LocalDate start, LocalDate end) {
        long numberOfItems = itemRepository.countItemsByCameraAndCreationPeriod(camera, convertToDate(start), convertToDate(end));
        return new ItemsInPeriod(start, end, numberOfItems);
    }

    @Override
    public LocalDate convertToLocalDate(String dateStamp) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStamp);
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    @Override
    public List<CountByYearItem> countItemsByYear(Integer year) {
        return statisticsDAO.countByYear(year);
    }

    @Override
    public ItemTagRatio countItems() {
        return new ItemTagRatio(itemRepository.count(), itemTagRepository.countTaggedItems());
    }

    public Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
