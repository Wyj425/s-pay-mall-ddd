package online.noqiokweb.domain.goods.service;

import online.noqiokweb.domain.goods.adapter.repository.IGoodsRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/25/2025 8:36 下午
 */
@Service
public class GoodsService implements IGoodsService{
    @Resource
    private IGoodsRepository repository;

    @Override
    public void changeOrderDealDone(String tradeNo) {
        repository.changeOrderDealDone(tradeNo);
    }
}
