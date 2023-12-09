package toy.ojm.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import toy.ojm.entity.RestaurantEntity;

import javax.sql.DataSource;
import java.lang.reflect.Member;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RestaurantEntity save(RestaurantEntity RestaurantEntity) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("RestaurantEntity").usingGeneratedKeyColumns("bpLcNm");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bpLcNm", RestaurantEntity.getBpLcNm());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
//        RestaurantEntity.setBpLcNm(key.longValue());
        return RestaurantEntity;
    }

//    @Override
//    public Optional<RestaurantEntity> findByDtlStateNm(String dtlStateNm) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where dtlStateNm = ? ", RestaurantRowMapper(), dtlStateNm);
//        return result.stream().findAny();
//    }
//
//    @Override
//    public Optional<RestaurantEntity> findBySiteWhLaDdr(String siteWhLaDdr) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where siteWhLaDdr = ? ", RestaurantRowMapper(), siteWhLaDdr);
//        return result.stream().findAny();
//    }
//
//    @Override
//    public Optional<RestaurantEntity> findByRdNWhLaDdr(String rdNWhLaDdr) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where rdNWhLaDdr = ? ", RestaurantRowMapper(), rdNWhLaDdr);
//        return result.stream().findAny();
//    }
//    @Override
//    public Optional<RestaurantEntity> findByBpLcNm(String bpLcNm) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where bpLcNm = ? ", RestaurantRowMapper(), bpLcNm);
//        return result.stream().findAny();
//    }
//    @Override
//    public Optional<RestaurantEntity> findByUpTadNm(String upTadNm) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where upTadNm = ? ", RestaurantRowMapper(), upTadNm);
//        return result.stream().findAny();
//    }
//    @Override
//    public Optional<RestaurantEntity> findByX(String x) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where x = ? ", RestaurantRowMapper(), x);
//        return result.stream().findAny();
//    }
//    @Override
//    public Optional<RestaurantEntity> findByY(String y) {
//        List<RestaurantEntity> result = jdbcTemplate.query("select * from RestaurantEntity where y = ? ", RestaurantRowMapper(), y);
//        return result.stream().findAny();
//    }
//
//    @Override
//    public List<RestaurantEntity> findAll()){
//        return jdbcTemplate.query("select * from RestaurantEntity", RestaurantRowMapper());;
//    }

//    public List<String> findAllNames() {
//        return jdbcTemplate.query("SELECT name FROM RestaurantEntity",
//            (rs, rowNum) -> rs.getString("name"));
//    }
//
//
//    private RowMapper<RestaurantEntity> RestaurantRowMapper() {
//        return (rs, rowNum) -> {
//            RestaurantEntity restaurantEntity = new RestaurantEntity();
//            restaurantEntity.setDtlStateNm(rs.getString("dtlStateNm"));
//            restaurantEntity.setSiteWhLaDdr(rs.getString("siteWhLaDdr"));
//            restaurantEntity.setSiteWhLaDdr(rs.getString("siteWhLaDdr"));
//            restaurantEntity.setSiteWhLaDdr(rs.getString("siteWhLaDdr"));
//            restaurantEntity.setSiteWhLaDdr(rs.getString("siteWhLaDdr"));
//            restaurantEntity.setSiteWhLaDdr(rs.getString("siteWhLaDdr"));
//
//            return restaurantEntity;
//        }
//    }
//
//    /* 일괄 삽입 */
    public int[] batchInsert(List<RestaurantEntity> restaurants) {
        return jdbcTemplate.batchUpdate(
            "INSERT INTO restaurant(dtlStateNm, siteWhLaDdr) VALUES(?, ?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    RestaurantEntity restaurantEntity = restaurants.get(i);
                    ps.setString(1, restaurantEntity.getDtlStateNm());
                    ps.setString(2, restaurantEntity.getSiteWhLaDdr());
                    ps.setString(3, restaurantEntity.getRdNWhLaDdr());
                    ps.setString(4, restaurantEntity.getBpLcNm());
                    ps.setString(5, restaurantEntity.getUpTadNm());
                    ps.setString(6, restaurantEntity.getX());
                    ps.setString(7, restaurantEntity.getY());


                }

                @Override
                public int getBatchSize() {
                    return restaurants.size();
                }
            });
    }
}
