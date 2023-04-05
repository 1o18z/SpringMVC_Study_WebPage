package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }


    @Test
    void save(){
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void findAll(){
        Item item1 = new Item("item1", 20000, 5);
        Item item2 = new Item("item2", 30000, 9);

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> result = itemRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1, item2);
    }


    @Test
    void updateItem(){
        Item item = new Item("item", 1000, 3);

        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        Item newItem = new Item("newItem", 10000, 2);
        itemRepository.update(itemId, newItem);


        Item findItem = itemRepository.findById(itemId);

        assertThat(findItem.getItemName()).isEqualTo(newItem.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(newItem.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(newItem.getQuantity());


    }

}
