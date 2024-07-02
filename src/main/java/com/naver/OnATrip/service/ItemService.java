package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.repository.pay.ItemRepository;
import com.naver.OnATrip.repository.pay.ItemRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

//    public Item getItemById(int itemId){
//        return itemRepository.findById(itemId)
//                .orElseThrow(()-> new IllegalArgumentException("Invalid item ID: " + itemId));
//    }

    @Transactional
    public Item addItem(Item item){
        return itemRepository.save(item);
    }

    @Transactional
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public List<Item> findAllById(int id){
        return itemRepository.findAllById(id);
    }

}
