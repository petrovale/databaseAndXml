package com.company.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import com.company.model.JobDescription;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class JobDescriptionDao {

    @SqlQuery("SELECT * FROM JOB_DESCRIPTION")
    public abstract List<JobDescription> getAll();

    @SqlUpdate("UPDATE job_description SET description = :description WHERE dep_code = :depCode and dep_job = :depJob")
    public abstract void update(@BindBean JobDescription job);

    @SqlBatch("INSERT INTO job_description (dep_code, dep_job, description) VALUES (:depCode, :depJob, :description)")
    public abstract void save(@BindBean List<JobDescription> jobs);

    @SqlBatch("delete from job_description where dep_code = :depCode and dep_job = :depJob")
    public abstract void delete(@BindBean List<JobDescription> jobs);

    @SqlUpdate("TRUNCATE TABLE job_description AND COMMIT")
    public abstract void clean();

    @SqlUpdate("INSERT INTO job_description (dep_code, dep_job, description) VALUES (:depCode, :depJob, :description)")
    public abstract void insert(@BindBean JobDescription jobDescription);
}
