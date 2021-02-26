package com.vinsguru.orderservice.service;

import com.vinsguru.orderservice.dto.PurchaseOrderResponseDto;
import com.vinsguru.orderservice.repository.PurchaseOrderRepository;
import com.vinsguru.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderQueryService {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    public Flux<PurchaseOrderResponseDto> getProductsByUserId(int userId){
        return Flux.fromStream(() -> this.orderRepository.findByUserId(userId).stream()) // blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

}
