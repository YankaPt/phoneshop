package com.es.core.services.phone;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneDao phoneDao;

    @Autowired
    public PhoneServiceImpl(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public List<Phone> getPhonesWithPositiveStock(int offset, int limit) {
        return phoneDao.findAllAvailable(offset, limit);
    }

    @Override
    public List<Phone> getPhonesWithPositiveStockWithOrderBy(int offset, int limit, String orderBy, boolean isAscend) {
        return phoneDao.findAllAvailableWithOrderBy(offset, limit, orderBy, isAscend);
    }

    @Override
    public List<Phone> getPhonesByKeyword(String keyword) {
        return phoneDao.findAllByKeyword(keyword);
    }

    @Override
    public Long getTotalAmountOfPhonesWithPositiveStock() {
        return phoneDao.getTotalAmountOfAvailablePhones();
    }

    @Override
    public Optional<Phone> get(Long key) {
        return phoneDao.get(key);
    }
}
