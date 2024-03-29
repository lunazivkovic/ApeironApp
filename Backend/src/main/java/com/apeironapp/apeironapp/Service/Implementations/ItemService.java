package com.apeironapp.apeironapp.Service.Implementations;

import com.apeironapp.apeironapp.DTO.*;
import com.apeironapp.apeironapp.Model.AvailableColors;
import com.apeironapp.apeironapp.Model.AvailableSize;
import com.apeironapp.apeironapp.Model.Item;
import com.apeironapp.apeironapp.Model.Pictures;
import com.apeironapp.apeironapp.Repository.ItemRepository;
import com.apeironapp.apeironapp.Service.IServices.IAuthorityService;
import com.apeironapp.apeironapp.Service.IServices.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ItemService implements IItemService {


    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AvailableColorsService availableColorsService;


    @Autowired
    private AvailableSizesService availableSizesService;


    @Autowired
    private PicturesService picturesService;

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item save(NewItemDTO newItemDTO) {
        Item item = new Item();
        item.setName(newItemDTO.getName());
        item.setPrice(newItemDTO.getPrice());
        item.setType(newItemDTO.getType());
        item.setGender(newItemDTO.getGender());

        Set<Pictures> pictures = new HashSet<Pictures>();
        item = itemRepository.save(item);
        for(String s: newItemDTO.getPictures()) {

            NewPictureDTO newPictureDTO = new NewPictureDTO();
            System.out.println("faesf" + item.getId());
            newPictureDTO.setItemId(item.getId());
            newPictureDTO.setName(s);
            Pictures picture = picturesService.save(newPictureDTO);
            pictures.add(picture);

        }
        item.setPictures(pictures);

        Item item1 = itemRepository.save(item);

        AvailableColors availableColors1 = new AvailableColors();

        List<String> colors = new ArrayList<String>();

        for(QuantityDTO color: newItemDTO.getQuantityDTO()) {

            if (!colors.contains(color.getColor())) {
                colors.add(color.getColor());
            }
        }

        for(String color: colors) {

            String quantity = "";
            String col = "";

         /*   String niz[] = color.split(",");
            col = niz[0];
            quantity = niz[1];*/

            NewAvailableColorsDTO availableColors = new NewAvailableColorsDTO();
            availableColors.setColor(color);
            availableColors.setQuantity(0);
            availableColors.setItemId(item.getId());
            availableColors1 = availableColorsService.save(availableColors);


        }

        List<AvailableColors> availableColors = availableColorsService.findAll();
        for(QuantityDTO size: newItemDTO.getQuantityDTO()) {

           AvailableColors availableColors2 = new AvailableColors();
            String quantity = "";
            String col = "";
            String si = "";

           /* String niz[] = size.split(",");
            col = niz[0];
            si = niz[1];
            quantity = niz[2];*/

            col = size.getColor();
            si = size.getSize();
            quantity = size.getQuantity();


            for(AvailableColors a : availableColors){

                if(a.getColor().equals(col)){
                    availableColors2 = a;
                }
            }



            NewAvailableSizesDTO availableSizesDTO = new NewAvailableSizesDTO();
            availableSizesDTO.setSize(si);
            availableSizesDTO.setQuantity(Integer.parseInt(quantity));
            availableSizesDTO.setAvailableColorsId(availableColors2.getId());
            AvailableSize availableSize = availableSizesService.save(availableSizesDTO);



        }



        return item1;
    }

    @Override
    public Item findById(Integer id) {
        return itemRepository.findById(id).get();
    }

    @Override
    public void delete(Integer id) {
        Item item = findById(id);
        itemRepository.delete(item);
    }

    @Override
    public Item update(Item item) {


        return itemRepository.save(item);
    }


}
