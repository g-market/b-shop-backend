package com.gabia.bshop.repository;

import com.gabia.bshop.entity.ItemImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    @Query("select ii "
            + "from ItemImage ii "
            + "join fetch ii.item "
            + "where ii.id in (select min(t.id) "
                            + "from ItemImage t "
                            + "where t.item.id in (:itemIds) "
                            + "group by t.item.id "
                            + "order by t.id)")
    List<ItemImage> findWithItemByItemIds(List<Long> itemIds);

    @Query("select ii.url "
            + "from ItemImage ii "
            + "where ii.id in (select min(t.id) "
                            + "from ItemImage t "
                            + "where t.item.id in (:itemIds) "
                            + "group by t.item.id "
                            + "order by t.id)")
    List<String> findUrlByItemIds(List<Long> itemIds);
}
