package de.milchreis.phobox.db;

import de.milchreis.phobox.core.model.statistics.CountByYearItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsDAO {

    @Autowired
    private JdbcTemplate template;


    public List<CountByYearItem> countByYear(Integer year) {

        String query =
                "SELECT camera, MONTH(i.creation) as month, COUNT(i.creation) as count " +
                "FROM Item i " +
                "WHERE YEAR(i.creation) = ? " +
                "GROUP BY MONTH(i.creation), camera";

        return template.query(query, new Object[]{year}, (resultSet, i) -> {
            CountByYearItem item = new CountByYearItem();
            item.setCamera(resultSet.getString("camera"));
            item.setMonth(resultSet.getInt("month"));
            item.setCount(resultSet.getInt("count"));
            return item;
        });

    }

}
