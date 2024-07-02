package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.repository.pay.ItemRepositoryCustom;
import com.naver.OnATrip.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/addItem")
    public String addItem(Item item){
        Item saveItem = itemService.addItem(item);
        return "redirect:admin";
    }



    @GetMapping("/subscribe")
    public String subscribe(Model model) {

        List<Item> item = itemService.findAllItems();
        model.addAttribute("item", item);

        return "pay/subscribe";
    }


}
