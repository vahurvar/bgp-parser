package fr.eurecom.parser;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Component
public class BgpDao {

    private final JdbcTemplate jdbcTemplate;

    public BgpDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String insertTimestampSql = "INSERT INTO prefix_temp(prefix_cidr, prefix_timestamp, as_path) " +
            "VALUES(?::cidr, ?, ?) ON CONFLICT DO NOTHING";

    public int[] insert(List<String[]> prefixes) {
        BatchPreparedStatementSetter batchPreparedStatementSetter = getBatchPreparedStatement(prefixes);
        return jdbcTemplate.batchUpdate(insertTimestampSql, batchPreparedStatementSetter);
    }

    private BatchPreparedStatementSetter getBatchPreparedStatement(List<String[]> prefixes) {
        return new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String[] split = prefixes.get(i);

                    ps.setString(1, split[5]);
                    Instant instant = Instant.ofEpochSecond(Integer.parseInt(split[1]));
                    ps.setTimestamp(2, Timestamp.from(instant));
                    ps.setObject(3, split[6]);
                }

                @Override
                public int getBatchSize() {
                    return prefixes.size();
                }
            };
    }

    private String[] convertASNPath(String s) {
        String[] asPath = s.split(" ");
        String asn = getAsnNumber(asPath, asPath[asPath.length - 1]);

        if (asn.startsWith("{")) {
            String[] last = asn.replaceFirst("\\{", "")
                    .replaceFirst("}", "")
                    .split(",");

            asPath[asPath.length - 1] = Arrays.toString(last);
        }
        return asPath;
    }

    private String getAsnNumber(String[] asPath, String asn) {
        if (asn.startsWith("{")) {
            String[] last = asn.replaceFirst("\\{", "")
                    .replaceFirst("}", "")
                    .split(",");

            asPath[asPath.length - 1] = Arrays.toString(last);
            asn = asPath[asPath.length - 2];
        }
        return asn;
    }

}
