package online.noqiokweb.infrastructure.adapter.repository;

import online.noqiokweb.domain.goods.adapter.repository.IGoodsRepository;
import online.noqiokweb.infrastructure.dao.IOrderDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/25/2025 8:38 下午
 */
@Repository
public class GoodsRepository implements IGoodsRepository {
    @Resource
    private IOrderDao orderDao;
    @Override
    public void changeOrderDealDone(String orderId) {
        orderDao.changeOrderDealDone(orderId);
    }
}
