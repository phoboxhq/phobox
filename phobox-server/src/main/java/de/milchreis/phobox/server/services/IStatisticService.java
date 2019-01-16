package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.model.statistics.CountByYearItem;
import de.milchreis.phobox.core.model.statistics.ItemsInPeriod;
import de.milchreis.phobox.db.entities.Item;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface IStatisticService {

    ItemsInPeriod countItemsByCreationPeriod(LocalDate start, LocalDate end);

    ItemsInPeriod findItemsByCreationPeriod(LocalDate start, LocalDate end);

    List<Item> findItemsByCamera(String camera);

    ItemsInPeriod findItemsByCameraAndCreationPeriod(String camera, LocalDate start, LocalDate end);

    ItemsInPeriod countItemsByCamera(String camera);

    ItemsInPeriod countItemsByCameraAndCreationPeriod(String camera, LocalDate start, LocalDate end);

    LocalDate convertToLocalDate(String dateStamp) throws ParseException;

    List<CountByYearItem> countItemsByYear(Integer year);
}
