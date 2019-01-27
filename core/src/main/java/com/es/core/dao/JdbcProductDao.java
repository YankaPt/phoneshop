package com.es.core.dao;

import com.es.core.dao.mappers.PhoneRowMapper;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcProductDao implements PhoneDao {
    private static final String SQL_QUERY_FOR_GETTING_ALL_COLORS = "select * from colors";
    private static final String SQL_FOR_GETTING_PHONE_BY_ID = "select * from phones where phones.id=?";
    private static final String SQL_FOR_GETTING_LAST_PHONE_ID = "select MAX(id) as lastId from phones";
    private static final String SQL_FOR_GETTING_AVAILABLE_PHONES_BY_OFFSET_AND_LIMIT = "select * from phones left join phone2color on phones.id = phone2color.phoneId where phones.id in (select phones.id from phones join stocks on phones.id=stocks.phoneId where stocks.stock > 0 and phones.price is not null offset ? limit ?)";
    private static final String SQL_FOR_GETTING_COLORS_BY_PHONE_ID = "select * from phone2color where phone2color.phoneId = ?";
    private static final String SQL_FOR_GETTING_TOTAL_AMOUNT_OF_AVAILABLE = "select count(*) from phones join stocks on phones.id=stocks.phoneId where stocks.stock> 0 and phones.price is not null";
    private static final String SQL_FOR_GETTING_PHONES_BY_KEYWORD = "select * from phones left join phone2color on phones.id = phone2color.phoneId where phones.id in (select phones.id from phones where brand like ? or brand like ? or brand like ? or model like ? or model like ? or model like ?)";
    private static final String SQL_FOR_GETTING_AVAILABLE_PHONES_WITH_ORDER_BY = "select * from phones left join phone2color on phones.id = phone2color.phoneId where phones.id in (select phones.id from phones join stocks on phones.id=stocks.phoneId where stocks.stock > 0 and phones.price is not null order by phones.ORDER_BY_STUB ASCEND_STUB offset ? limit ?) order by phones.ORDER_BY_STUB ASCEND_STUB";

    private JdbcTemplate jdbcTemplate;
    private BeanPropertyRowMapper<Phone> phoneBeanPropertyRowMapper = new BeanPropertyRowMapper<>(Phone.class);

    @Autowired
    public JdbcProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Phone phone) {
        checkPhoneIdAndSetIfNeeded(phone);
        insertPhone(phone);
        if (!phone.getColors().isEmpty()) {
            bindPhoneAndColor(phone);
        }
    }

    private void checkPhoneIdAndSetIfNeeded(Phone phone) {
        jdbcTemplate.query(SQL_FOR_GETTING_LAST_PHONE_ID,
                (ResultSet resultSet) -> phone.setId(resultSet.getLong("lastId") + 1));
    }

    private void insertPhone(Phone phone) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("phones");
        simpleJdbcInsert.execute(getPhoneValues(phone));
    }

    private void bindPhoneAndColor(Phone phone) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("phone2color");
        List<Map<String, ?>> batch = new ArrayList<>();
        phone.getColors().forEach(color -> {
            Map<String, Object> row = new HashMap<>(2);
            row.put("phoneId", phone.getId());
            row.put("colorId", color.getId());
            batch.add(row);
        });
        simpleJdbcInsert.executeBatch((Map<String, ?>[]) batch.toArray());
    }

    @Override
    public Optional<Phone> get(Long key) {
        Map<Long, Color> colors = getColors();
        Optional<Phone> phone = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FOR_GETTING_PHONE_BY_ID,
                (resultSet, i) -> phoneBeanPropertyRowMapper.mapRow(resultSet, 0), key));
        phone.ifPresent((p) -> setColorsForPhone(p, colors));
        return phone;
    }

    private Map<Long, Color> getColors() {
        return jdbcTemplate.query(SQL_QUERY_FOR_GETTING_ALL_COLORS,
                new BeanPropertyRowMapper<>(Color.class)).stream().collect(Collectors.toMap(Color::getId, (c) -> c));
    }

    @Override
    public List<Phone> findAllAvailableWithOrderBy(int offset, int limit, String orderBy, boolean isAscend) {
        String ascending = isAscend ? "asc" : "desc";
        List<Phone> phones = new ArrayList<>();
        jdbcTemplate.query(SQL_FOR_GETTING_AVAILABLE_PHONES_WITH_ORDER_BY.replaceAll("ORDER_BY_STUB", orderBy).replaceAll("ASCEND_STUB", ascending),
                new PhoneRowMapper(phones, getColors()), offset, limit);
        return phones;
    }

    @Override
    public List<Phone> findAllAvailable(int offset, int limit) {
        List<Phone> phones = new ArrayList<>();
        jdbcTemplate.query(SQL_FOR_GETTING_AVAILABLE_PHONES_BY_OFFSET_AND_LIMIT,
                new PhoneRowMapper(phones, getColors()), offset, limit);
        return phones;
    }

    @Override
    public List<Phone> findAllByKeyword(String keyword) {
        keyword = "%" + keyword + "%";
        StringBuilder keywordWithUpperCaseFirstLetter = new StringBuilder(keyword);
        keywordWithUpperCaseFirstLetter.replace(1, 2, keywordWithUpperCaseFirstLetter.substring(1, 2).toUpperCase());
        List<Phone> phones = new ArrayList<>();
        jdbcTemplate.query(SQL_FOR_GETTING_PHONES_BY_KEYWORD,
                new PhoneRowMapper(phones, getColors()), keyword, keyword.toUpperCase(), keywordWithUpperCaseFirstLetter.toString(), keyword, keyword.toUpperCase(), keywordWithUpperCaseFirstLetter.toString());
        return phones;
    }

    private void setColorsForPhone(Phone phone, Map<Long, Color> colors) {
        phone.setColors(new HashSet<>());
        jdbcTemplate.query(SQL_FOR_GETTING_COLORS_BY_PHONE_ID,
                (resultSet, i) -> phone.getColors().add(colors.get(resultSet.getLong("colorId"))), phone.getId());
    }

    @Override
    public Long getTotalAmountOfAvailablePhones() {
        return jdbcTemplate.queryForObject(SQL_FOR_GETTING_TOTAL_AMOUNT_OF_AVAILABLE, Long.class);
    }

    private Map<String, Object> getPhoneValues(Phone phone) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", phone.getId());
        values.put("brand", phone.getBrand());
        values.put("model", phone.getModel());
        values.put("price", phone.getPrice());
        values.put("displaySizeInches", phone.getDisplaySizeInches());
        values.put("weightGr", phone.getWeightGr());
        values.put("lengthMm", phone.getLengthMm());
        values.put("widthMm", phone.getWidthMm());
        values.put("heightMm", phone.getHeightMm());
        values.put("announced", phone.getAnnounced());
        values.put("deviceType", phone.getDeviceType());
        values.put("os", phone.getOs());
        values.put("displayResolution", phone.getDisplayResolution());
        values.put("pixelDensity", phone.getPixelDensity());
        values.put("displayTechnology", phone.getDisplayTechnology());
        values.put("backCameraMegapixels", phone.getBackCameraMegapixels());
        values.put("frontCameraMegapixels", phone.getFrontCameraMegapixels());
        values.put("ramGb", phone.getRamGb());
        values.put("internalStorageGb", phone.getInternalStorageGb());
        values.put("batteryCapacityMah", phone.getBatteryCapacityMah());
        values.put("talkTimeHours", phone.getTalkTimeHours());
        values.put("standByTimeHours", phone.getStandByTimeHours());
        values.put("bluetooth", phone.getBluetooth());
        values.put("positioning", phone.getPositioning());
        values.put("imageUrl", phone.getImageUrl());
        values.put("description", phone.getDescription());
        return values;
    }
}
