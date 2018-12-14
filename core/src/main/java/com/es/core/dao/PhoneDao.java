package com.es.core.dao;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAllAvailable(int offset, int limit);

    Long getTotalAmountOfAvailablePhones();

    List<Phone> findAllByKeyword(String keyword);
}
