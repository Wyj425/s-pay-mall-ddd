package online.noqiokweb.api;

import online.noqiokweb.api.dto.CreatePayRequestDTO;
import online.noqiokweb.api.response.Response;

public interface IPayService {
    Response<String> createPayOrder(CreatePayRequestDTO requestDTO);


}
