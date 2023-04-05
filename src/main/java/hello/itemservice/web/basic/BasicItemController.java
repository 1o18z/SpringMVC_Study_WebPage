package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

////    @PostMapping("/add") 위랑 중복 매핑 일어나서 오류날까 봐 주석처리!
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        model.addAttribute("item", item); // 자동 추가, 생략 가능
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // @ModelAttribute 생략 가능
        itemRepository.save(item);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items" + item.getId();
        // 수정 후 새로고침하면 계속 Post 실행됨 (마지막 수행된 게 계속 다시 실행돼서) -> 상품 상세 페이지로 리다이렉트 보내서 이걸 방지
        // 새로고침해서 다시 요청이 오면 "redirect:/basic/items" + item.getId() 여기로 가라고 다시 요청을 해버림!
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) { // 저장되었다는 메시지를 같이 보내자 (파라미터를 붙여서 보냄)
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}"; // 여기 못 들어간 애(status)는 쿼리 파라미터 형식(?status=true)로 들어감
    } // addAttribute 덕분에 itemId를 치환을 해준다({itemId} 이렇게!)

    // 상품 수정 폼 뷰
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId); // 아이디로 찾고
        model.addAttribute("item", item); // 모델에 넣고
        return "basic/editForm";
    }

    // 상품 수정 처리
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item); // 수정 처리
        return "redirect:/basic/items/{itemId}";
        // 마지막에 뷰 템플릿을 호출하는 대신 상품 상세 화면으로 이동하도록 리다이렉트 호출
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
