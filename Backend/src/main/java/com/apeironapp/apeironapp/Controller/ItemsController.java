package com.apeironapp.apeironapp.Controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.apeironapp.apeironapp.DTO.ItemDTO;
import com.apeironapp.apeironapp.DTO.NewAvailableColorsDTO;
import com.apeironapp.apeironapp.DTO.NewAvailableSizesDTO;
import com.apeironapp.apeironapp.DTO.NewItemDTO;
import com.apeironapp.apeironapp.Model.AvailableColors;
import com.apeironapp.apeironapp.Model.AvailableSize;
import com.apeironapp.apeironapp.Model.Item;
import com.apeironapp.apeironapp.Model.Pictures;
import com.apeironapp.apeironapp.Service.Implementations.ItemService;
import com.apeironapp.apeironapp.Service.Implementations.PicturesService;

@RestController
@RequestMapping(value = "/api/items", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ItemsController {

    @Autowired
    private PicturesService picturesService;

    @Autowired
    private ItemService itemService;

    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addItem(@RequestBody NewItemDTO newItemDTO) {

        System.out.println("dzgd" + newItemDTO.getQuantityDTO());
        Item item = itemService.save(newItemDTO);

        return item == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>("Item is successfully added!", HttpStatus.CREATED);
    }

    @PostMapping("/upload")
        // @PreAuthorize("hasRole('PATIENT')")
    ResponseEntity<String> hello(@RequestParam("file") MultipartFile file) throws IOException {

        System.out.println("sgvsrgserg" + file);
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        File destination = new File("src/main/resources/images/" + file.getOriginalFilename());
        ImageIO.write(src, "png", destination);

        return new ResponseEntity<>("Image is successfully added!", HttpStatus.CREATED);
    }

    @GetMapping(value = "/tshirt-women")
    // @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<List<ItemDTO>> tshirtWomen() {
        BufferedImage img = null;
        List<Item> itemList = itemService.findAll();
        List<ItemDTO> womenTShirt = new ArrayList<ItemDTO>();
        for (Item item : itemList) {
            if (item.getGender().equals("Female") && item.getType().equals("T-Shirt")) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setGender(item.getGender());
                itemDTO.setName(item.getName());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setType(item.getType());

                Set<String> list = new HashSet<String>();
                for (Pictures pictures : item.getPictures()) {
                    File destination = new File("src/main/resources/images/" + pictures.getName());
                    try {
                        img = ImageIO.read(destination);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ImageIO.write(img, "PNG", out);
                        byte[] bytes = out.toByteArray();
                        String base64bytes = Base64.getEncoder().encodeToString(bytes);
                        String src = "data:image/png;base64," + base64bytes;
                        list.add(src);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                itemDTO.setFiles(list);

                List<NewAvailableColorsDTO> newAvailableColorsDTOlist = new ArrayList<NewAvailableColorsDTO>();

                for (AvailableColors availableColors : item.getAvailableColors()) {
                    NewAvailableColorsDTO newAvailableColorsDTO = new NewAvailableColorsDTO();
                    newAvailableColorsDTO.setColor(availableColors.getColor());
                    newAvailableColorsDTO.setQuantity(availableColors.getQuantity());

                    List<NewAvailableSizesDTO> newAvailableSizesDTOS = new ArrayList<NewAvailableSizesDTO>();

                    for (AvailableSize availableSize : availableColors.getAvailableSizes()) {
                        NewAvailableSizesDTO newAvailableSizesDTO = new NewAvailableSizesDTO();
                        newAvailableSizesDTO.setSize(availableSize.getSize());
                        newAvailableSizesDTO.setQuantity(availableSize.getQuantity());
                        newAvailableSizesDTOS.add(newAvailableSizesDTO);
                    }

                    newAvailableColorsDTO.setSizes(newAvailableSizesDTOS);
                    newAvailableColorsDTOlist.add(newAvailableColorsDTO);
                }
                itemDTO.setColors(newAvailableColorsDTOlist);

                womenTShirt.add(itemDTO);
            }
        }

        return womenTShirt == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(womenTShirt);
    }



    @GetMapping(value = "/delete/{id}")
    // @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<String> deleteItem(@PathVariable Integer id) {

        itemService.delete(id);

        return ResponseEntity.ok("Success");
    }



    @GetMapping("/tshirt-men")
    // @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<List<ItemDTO>> tshirtMen() {

        BufferedImage img = null;
        List<Item> itemList = itemService.findAll();
        List<ItemDTO> womenTShirt = new ArrayList<ItemDTO>();
        for (Item item : itemList) {
            if (item.getGender().equals("Male") && item.getType().equals("T-Shirt")) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setGender(item.getGender());
                itemDTO.setName(item.getName());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setType(item.getType());

                Set<String> list = new HashSet<String>();
                for (Pictures pictures : item.getPictures()) {
                    File destination = new File("src/main/resources/images/" + pictures.getName());
                    try {
                        img = ImageIO.read(destination);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ImageIO.write(img, "PNG", out);
                        byte[] bytes = out.toByteArray();
                        String base64bytes = Base64.getEncoder().encodeToString(bytes);
                        String src = "data:image/png;base64," + base64bytes;
                        list.add(src);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                itemDTO.setFiles(list);

                List<NewAvailableColorsDTO> newAvailableColorsDTOlist = new ArrayList<NewAvailableColorsDTO>();

                for (AvailableColors availableColors : item.getAvailableColors()) {
                    NewAvailableColorsDTO newAvailableColorsDTO = new NewAvailableColorsDTO();
                    newAvailableColorsDTO.setColor(availableColors.getColor());
                    newAvailableColorsDTO.setQuantity(availableColors.getQuantity());

                    List<NewAvailableSizesDTO> newAvailableSizesDTOS = new ArrayList<NewAvailableSizesDTO>();

                    for (AvailableSize availableSize : availableColors.getAvailableSizes()) {
                        NewAvailableSizesDTO newAvailableSizesDTO = new NewAvailableSizesDTO();
                        newAvailableSizesDTO.setSize(availableSize.getSize());
                        newAvailableSizesDTO.setQuantity(availableSize.getQuantity());
                        newAvailableSizesDTOS.add(newAvailableSizesDTO);
                    }

                    newAvailableColorsDTO.setSizes(newAvailableSizesDTOS);
                    newAvailableColorsDTOlist.add(newAvailableColorsDTO);
                }
                itemDTO.setColors(newAvailableColorsDTOlist);

                womenTShirt.add(itemDTO);
            }
        }

        return womenTShirt == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(womenTShirt);
    }

    @GetMapping("/hoodies-women")
    // @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<List<ItemDTO>> hoodiesWomen() {

        BufferedImage img = null;
        List<Item> itemList = itemService.findAll();
        List<ItemDTO> womenTShirt = new ArrayList<ItemDTO>();
        for (Item item : itemList) {
            if (item.getGender().equals("Female") && item.getType().equals("Hoodie")) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setGender(item.getGender());
                itemDTO.setName(item.getName());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setType(item.getType());

                Set<String> list = new HashSet<String>();
                for (Pictures pictures : item.getPictures()) {
                    File destination = new File("src/main/resources/images/" + pictures.getName());
                    try {
                        img = ImageIO.read(destination);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ImageIO.write(img, "PNG", out);
                        byte[] bytes = out.toByteArray();
                        String base64bytes = Base64.getEncoder().encodeToString(bytes);
                        String src = "data:image/png;base64," + base64bytes;
                        list.add(src);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                itemDTO.setFiles(list);

                List<NewAvailableColorsDTO> newAvailableColorsDTOlist = new ArrayList<NewAvailableColorsDTO>();

                for (AvailableColors availableColors : item.getAvailableColors()) {
                    NewAvailableColorsDTO newAvailableColorsDTO = new NewAvailableColorsDTO();
                    newAvailableColorsDTO.setColor(availableColors.getColor());
                    newAvailableColorsDTO.setQuantity(availableColors.getQuantity());

                    List<NewAvailableSizesDTO> newAvailableSizesDTOS = new ArrayList<NewAvailableSizesDTO>();

                    for (AvailableSize availableSize : availableColors.getAvailableSizes()) {
                        NewAvailableSizesDTO newAvailableSizesDTO = new NewAvailableSizesDTO();
                        newAvailableSizesDTO.setSize(availableSize.getSize());
                        newAvailableSizesDTO.setQuantity(availableSize.getQuantity());
                        newAvailableSizesDTOS.add(newAvailableSizesDTO);
                    }

                    newAvailableColorsDTO.setSizes(newAvailableSizesDTOS);
                    newAvailableColorsDTOlist.add(newAvailableColorsDTO);
                }
                itemDTO.setColors(newAvailableColorsDTOlist);

                womenTShirt.add(itemDTO);
            }
        }

        return womenTShirt == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(womenTShirt);
    }

    @GetMapping("/hoodies-men")
    // @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<List<ItemDTO>> hoodiesMen() {

        BufferedImage img = null;
        List<Item> itemList = itemService.findAll();
        List<ItemDTO> womenTShirt = new ArrayList<ItemDTO>();
        for (Item item : itemList) {
            if (item.getGender().equals("Male") && item.getType().equals("Hoodie")) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setGender(item.getGender());
                itemDTO.setName(item.getName());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setType(item.getType());

                Set<String> list = new HashSet<String>();
                for (Pictures pictures : item.getPictures()) {
                    File destination = new File("src/main/resources/images/" + pictures.getName());
                    try {
                        img = ImageIO.read(destination);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ImageIO.write(img, "PNG", out);
                        byte[] bytes = out.toByteArray();
                        String base64bytes = Base64.getEncoder().encodeToString(bytes);
                        String src = "data:image/png;base64," + base64bytes;
                        list.add(src);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                itemDTO.setFiles(list);

                List<NewAvailableColorsDTO> newAvailableColorsDTOlist = new ArrayList<NewAvailableColorsDTO>();

                for (AvailableColors availableColors : item.getAvailableColors()) {
                    NewAvailableColorsDTO newAvailableColorsDTO = new NewAvailableColorsDTO();
                    newAvailableColorsDTO.setColor(availableColors.getColor());
                    newAvailableColorsDTO.setQuantity(availableColors.getQuantity());

                    List<NewAvailableSizesDTO> newAvailableSizesDTOS = new ArrayList<NewAvailableSizesDTO>();

                    for (AvailableSize availableSize : availableColors.getAvailableSizes()) {
                        NewAvailableSizesDTO newAvailableSizesDTO = new NewAvailableSizesDTO();
                        newAvailableSizesDTO.setSize(availableSize.getSize());
                        newAvailableSizesDTO.setQuantity(availableSize.getQuantity());
                        newAvailableSizesDTOS.add(newAvailableSizesDTO);
                    }

                    newAvailableColorsDTO.setSizes(newAvailableSizesDTOS);
                    newAvailableColorsDTOlist.add(newAvailableColorsDTO);
                }
                itemDTO.setColors(newAvailableColorsDTOlist);

                womenTShirt.add(itemDTO);
            }
        }

        return womenTShirt == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(womenTShirt);
    }

    @GetMapping("/hats")
    // @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<List<ItemDTO>> hats() {

        BufferedImage img = null;
        List<Item> itemList = itemService.findAll();
        List<ItemDTO> womenTShirt = new ArrayList<ItemDTO>();
        for (Item item : itemList) {
            if (item.getGender().equals("Hat") && item.getType().equals("Hat")) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setGender(item.getGender());
                itemDTO.setName(item.getName());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setType(item.getType());

                Set<String> list = new HashSet<String>();
                for (Pictures pictures : item.getPictures()) {
                    File destination = new File("src/main/resources/images/" + pictures.getName());
                    try {
                        img = ImageIO.read(destination);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ImageIO.write(img, "PNG", out);
                        byte[] bytes = out.toByteArray();
                        String base64bytes = Base64.getEncoder().encodeToString(bytes);
                        String src = "data:image/png;base64," + base64bytes;
                        list.add(src);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                itemDTO.setFiles(list);

                List<NewAvailableColorsDTO> newAvailableColorsDTOlist = new ArrayList<NewAvailableColorsDTO>();

                for (AvailableColors availableColors : item.getAvailableColors()) {
                    NewAvailableColorsDTO newAvailableColorsDTO = new NewAvailableColorsDTO();
                    newAvailableColorsDTO.setColor(availableColors.getColor());
                    newAvailableColorsDTO.setQuantity(availableColors.getQuantity());

                    List<NewAvailableSizesDTO> newAvailableSizesDTOS = new ArrayList<NewAvailableSizesDTO>();

                    for (AvailableSize availableSize : availableColors.getAvailableSizes()) {
                        NewAvailableSizesDTO newAvailableSizesDTO = new NewAvailableSizesDTO();
                        newAvailableSizesDTO.setSize(availableSize.getSize());
                        newAvailableSizesDTO.setQuantity(availableSize.getQuantity());
                        newAvailableSizesDTOS.add(newAvailableSizesDTO);
                    }

                    newAvailableColorsDTO.setSizes(newAvailableSizesDTOS);
                    newAvailableColorsDTOlist.add(newAvailableColorsDTO);
                }
                itemDTO.setColors(newAvailableColorsDTOlist);

                womenTShirt.add(itemDTO);
            }
        }

        return womenTShirt == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(womenTShirt);
    }

}
