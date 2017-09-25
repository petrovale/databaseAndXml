package com.company.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.sun.istack.internal.NotNull;
import lombok.*;

@Data
@EqualsAndHashCode(exclude={"description"})
@RequiredArgsConstructor
@ToString
public class JobDescription {
    @Column("dep_code")
    @NotNull
    private Integer depCode;
    @NotNull
    @Column("dep_job")
    private Integer depJob;
    @NotNull
    private String description;

    public JobDescription(int depCode, int depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }
}
