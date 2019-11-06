package fr.eurecom.parser;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class BgpDao {

    private final JdbcTemplate jdbcTemplate;

    public BgpDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL = "INSERT INTO bgp(dump_timestamp, from_ip, from_asn, prefix, origin, next_hop, as_path, file_name) \n" +
            "        VALUES(?, ?::inet, ?, ?::cidr, ?::origin_type, ?::inet, ?::varchar(10)[], ?)";

    public int[] insertAllToBgp(List<String[]> prefixes, String file) {
        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String[] split = prefixes.get(i);
                Instant epochSeconds = Instant.ofEpochSecond(Integer.parseInt(split[1]));
                ps.setTimestamp(1, Timestamp.from(epochSeconds));
                ps.setString(2, split[3]);
                ps.setString(3, split[4]);
                ps.setString(4, split[5]);
                ps.setString(5, split[7]);
                ps.setString(6, split[8]);
                ps.setObject(7, split[6].split(" "));
                ps.setString(8, file);
            }

            @Override
            public int getBatchSize() {
                return prefixes.size();
            }
        };

        return jdbcTemplate.batchUpdate(SQL, batchPreparedStatementSetter);
    }

    public int[] insert(List<String[]> prefixes) {
        final String insertTimestampSql = "INSERT INTO prefix_timestamp(prefix_cidr, prefix_date) VALUES(?::cidr, ?) ON CONFLICT (prefix_cidr, prefix_date) DO NOTHING";

        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String[] split = prefixes.get(i);
                ps.setString(1, split[5]);
                Instant instant = Instant.ofEpochSecond(Integer.parseInt(split[1]));
                ps.setObject(2, instant.atZone(ZoneId.of("UTC")).toLocalDate());
            }

            @Override
            public int getBatchSize() {
                return prefixes.size();
            }
        };

        return jdbcTemplate.batchUpdate(insertTimestampSql, batchPreparedStatementSetter);
    }

}