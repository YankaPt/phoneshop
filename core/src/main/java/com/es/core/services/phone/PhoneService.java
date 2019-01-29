package com.es.core.services.phone;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    List<Phone> getPhonesWithPositiveStock(int offset, int limit);

    List<Phone> getPhonesByKeyword(String keyword);

    List<Phone> getPhonesWithPositiveStockWithOrderBy(int offset, int limit, String orderBy, boolean isAscend);

    Long getTotalAmountOfPhonesWithPositiveStock();

    Optional<Phone> get(Long key);
}
