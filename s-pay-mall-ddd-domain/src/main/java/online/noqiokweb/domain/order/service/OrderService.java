package online.noqiokweb.domain.order.service;

import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.order.adapter.port.IProductPort;
import online.noqiokweb.domain.order.adapter.repository.IOrderRepository;
import online.noqiokweb.domain.order.model.aggregate.CreateOrderAggregate;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 7:51 PM
 */
@Slf4j
@Service
public class OrderService extends AbstractOrderService{
    public OrderService(IOrderRepository  repository, IProductPort port){
        super(repository, port);
    }
    @Override
    protected void doSaveOrder(CreateOrderAggregate orderAggregate) {
        repository.doSaveOrder(orderAggregate);
    }
}
