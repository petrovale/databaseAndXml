package com.company;

import com.company.dao.JobDescriptionDao;
import com.company.model.JobDescription;

import java.util.Arrays;
import java.util.List;

public class JobDescriptionTestData {
    public static JobDescription jobDescription1;
    public static JobDescription jobDescription2;
    public static List<JobDescription> JOBS;

    public static void init() {
        jobDescription1 = new JobDescription(1, 1, "Описание должности разработчика");
        jobDescription2 = new JobDescription(1, 2, "Описание должности босса");
        JOBS = Arrays.asList(jobDescription1, jobDescription2);
    }

    public static void setUp() {
        JobDescriptionDao dao = DBIProvider.getDao(JobDescriptionDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            JOBS.forEach(dao::insert);
        });
    }

    public static JobDescription getUpdated() {
        return new JobDescription(1, 1, "Обновленное описание должности разработчика");
    }
}
