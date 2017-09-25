package com.company.dao;

import com.company.DBIProvider;
import com.company.DBITestProvider;
import com.company.JobDescriptionTestData;
import com.company.model.JobDescription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import java.util.Arrays;
import java.util.List;

import static com.company.JobDescriptionTestData.*;

public class JobDescriptionDaoTest {

    static {
        DBITestProvider.initDBI();
    }

    private JobDescriptionDao dao;

    public JobDescriptionDaoTest() {
        this.dao = DBIProvider.getDao(JobDescriptionDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        JobDescriptionTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        JobDescriptionTestData.setUp();
    }

    @Test
    public void testGetAll() throws Exception {
        List<JobDescription> jobs = dao.getAll();
        Assert.assertEquals(JOBS, jobs);
    }

    @Test
    public void testUpdate() throws Exception {
        JobDescription updated = getUpdated();
        dao.update(updated);
        Assert.assertEquals(Arrays.asList(jobDescription2, updated), dao.getAll());
    }

    @Test
    public void testSave() throws Exception {
        JobDescription jobDescription3 = new JobDescription(2, 3, "Описание должности тестировщика");
        JobDescription jobDescription4 = new JobDescription(1, 4, "Описание должности системного администратор");
        dao.save(Arrays.asList(jobDescription3, jobDescription4));
        Assert.assertEquals(Arrays.asList(jobDescription1, jobDescription2, jobDescription3, jobDescription4), dao.getAll());
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testSaveException() throws Exception {
        dao.save(Arrays.asList(jobDescription1, jobDescription2));
    }

    @Test
    public void delete() throws Exception {
        dao.delete(Arrays.asList(jobDescription1));
        Assert.assertEquals(Arrays.asList(jobDescription2), dao.getAll());
    }

}