package com.study.kafka.shopvalidator.events;

import com.study.kafka.shopvalidator.dto.ShopDto;
import com.study.kafka.shopvalidator.dto.ShopItemDto;
import com.study.kafka.shopvalidator.model.Product;
import com.study.kafka.shopvalidator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {
    private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";
    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ShopDto> kafkaTemplate;
    
    @KafkaListener(topics = SHOP_TOPIC_NAME, groupId = "group")
    public void listenerShopTopic(ShopDto shopDto){
        try{
            log.info("Compra recebida no topico: {}", shopDto.getIdentifier());
            boolean success = true;
            for (ShopItemDto itemDto: shopDto.getItems()){
                Product product = productRepository.findByIdentifier(itemDto.getProductIdentifier());
                if (!isValidShop(itemDto, product)){
                    shopError(shopDto);
                    success = false;
                    break;
                }
            }

            if (success){
                shopSuccess(shopDto);
            }
        } catch (Exception e){
            log.error("Erro no processamento da compra {}", shopDto.getIdentifier());
        }
    }

    private void shopSuccess(ShopDto shopDto) {
        log.info("Compra {} efetuada com sucesso", shopDto.getIdentifier());
        shopDto.setStatus("SUCCESS");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDto);
    }

    private void shopError(ShopDto shopDto) {
        log.info("Erro no processamento da compra {}", shopDto.getIdentifier());
        shopDto.setStatus("ERROR");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDto);
    }

    private boolean isValidShop(ShopItemDto itemDto, Product product) {
        return product != null || product.getAmount() >= itemDto.getAmount();
    }

}
