package com.example.backendtracker.domain.repositories.mapper;

import com.example.backendtracker.entities.dean.dto.ClassGroupInfoDean;
import com.example.backendtracker.entities.dean.dto.SubgroupWithClassGroups;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubgroupWithClassGroupsExtractor implements ResultSetExtractor<List<SubgroupWithClassGroups>> {

    @Override
    public List<SubgroupWithClassGroups> extractData(ResultSet rs) throws SQLException, DataAccessException {
        // Use LinkedHashMap to maintain insertion order (ordered by id_subgroup)
        Map<Long, SubgroupWithClassGroups> map = new LinkedHashMap<>();

        while (rs.next()) {
            Long idSubgroup = rs.getLong("id_subgroup");

            // Get or create the subgroup
            SubgroupWithClassGroups subgroup = map.get(idSubgroup);
            if (subgroup == null) {
                subgroup = new SubgroupWithClassGroups();
                subgroup.setIdSubgroup(idSubgroup);
                subgroup.setSubgroupNumber(rs.getString("subgroup_number"));
                subgroup.setAdmissionDate(rs.getDate("admission_date").toLocalDate());
                subgroup.setClassGroups(new ArrayList<>());
                map.put(idSubgroup, subgroup);
            }

            // Check if there is a class group (since it's a LEFT JOIN, these fields may be null)
            Long idClassGroup = rs.getLong("id_class_group");
            if (!rs.wasNull()) {
                ClassGroupInfoDean cgInfo = new ClassGroupInfoDean();
                cgInfo.setIdClassGroup(idClassGroup);

                // Since id_class_hold is in ClassGroupsToSubgroups, it may be null
                Long idClassHold = rs.getLong("id_class_hold");
                if (!rs.wasNull()) {
                    cgInfo.setIdClassHold(idClassHold);
                }

                cgInfo.setDescription(rs.getString("description"));
                cgInfo.setSubjectName(rs.getString("subject_name"));
                cgInfo.setFormatName(rs.getString("format_name"));
                cgInfo.setTeacherName(rs.getString("teacher_name"));
                subgroup.getClassGroups().add(cgInfo);
            }
        }

        return new ArrayList<>(map.values());
    }
}