package com.es.phoneshop.web.services;

import com.es.core.model.phone.Phone;
import com.es.core.services.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductListPageService {
    private final PhoneService phoneService;

    @Autowired
    public ProductListPageService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    public Integer resolveParamsAndGetPage(Integer pageNumber, Boolean previousPage, Boolean nextPage) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (previousPage != null) {
            pageNumber = pageNumber > 1 ? pageNumber - 1 : 1;
        } else if (nextPage != null) {
            pageNumber++;
        }
        return pageNumber;
    }

    public List<Phone> findPhonesForCurrentPage(Integer pageNumber, Integer amountOfShowedProducts, String orderBy, boolean isAscend) {
        if (!("brand".equals(orderBy) || "model".equals(orderBy) || "price".equals(orderBy) || "displaySizeInches".equals(orderBy))) {
            orderBy = "brand";
            isAscend = true;
        }
        Long totalAmountOfProductsOnStock = phoneService.getTotalAmountOfPhonesWithPositiveStock();
        if (amountOfShowedProducts * (pageNumber - 1) > totalAmountOfProductsOnStock) {
            pageNumber = ((Long) (totalAmountOfProductsOnStock / amountOfShowedProducts)).intValue();
        }
        return phoneService.getPhonesWithPositiveStockWithOrderBy(amountOfShowedProducts * (pageNumber - 1), amountOfShowedProducts, orderBy, isAscend);
    }

    public List<Phone> findPhonesBySearch(String search) {
        return phoneService.getPhonesByKeyword(search);
    }
}
