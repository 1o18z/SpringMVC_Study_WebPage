package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository // 컴포넌트 스캔에 들어감!
public class ItemRepository {

    // 여러 개가 동시에 접근하면 HashMap 쓰면 안됨! -> 얘가 지금 싱글톤으로 생성 + static -> ConcurrentHashMap 써야 함!
    private static final Map<Long, Item> store = new HashMap<>(); // static
    private static long sequence = 0L; // static
    // 스프링 컨테이너 안에서 쓰면 어차피 싱글톤이기 때문에 static을 안 써줘도 됨 !

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
