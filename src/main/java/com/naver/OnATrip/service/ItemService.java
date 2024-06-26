package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.repository.pay.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

//    public Item getItemById(int itemId){
//        return itemRepository.findById(itemId)
//                .orElseThrow(()-> new IllegalArgumentException("Invalid item ID: " + itemId));
//    }


}
