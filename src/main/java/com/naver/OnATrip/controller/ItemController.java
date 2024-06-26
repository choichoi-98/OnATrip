package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.repository.pay.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/payPage")
    public String getItemById(@RequestParam("item_id") int itemId, Model model) {

        Item item = new Item();
        item.setId(1);
        item.setName("1개월");
        item.setItemPrice(100);
        item.setPeriod(30);

        //Item item = (Item) itemRepository.findAllById(itemId);
        model.addAttribute("item", item);

        return "pay/payPage";
    }



}
