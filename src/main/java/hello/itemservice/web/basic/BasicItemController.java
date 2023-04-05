package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final이 붙은 애의 생성자를 만들어줌 !
public class BasicItemController {

    private final ItemRepository itemRepository;
    // BasicItemController가 스프링빈에 등록되면서 생성자주입 itemRepository

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add") // 조회
    public String addForm() {
        return "basic/addForm";
    }

    // 위아래 같은 url인데 HTTPMethod로 url 구분!

//    @PostMapping("/add")
//    public String addItemV1(@RequestParam String itemName,
//                            @RequestParam int price,
//                            @RequestParam Integer quantity,
//                            Model model) {
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);
//
//        itemRepository.save(item);
//        model.addAttribute("item", item);
//
//        return "basic/item";
//    }
//
////    @PostMapping("/add") 위랑 중복 매핑 일어나서 오류날까 봐 주석처리!
//    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
//        itemRepository.save(item);
////        model.addAttribute("item", item); // 자동 추가, 생략 가능
//        return "basic/item";
//    }

    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // @ModelAttribute 생략 가능
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct // 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다 !
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

}
