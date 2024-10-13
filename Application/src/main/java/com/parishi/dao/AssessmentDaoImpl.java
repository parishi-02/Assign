package com.parishi.dao;

import com.parishi.dto.AssessmentDTO;
import com.parishi.dto.AssignmentPlanDTO;
import com.parishi.dto.BatchDTO;
import com.parishi.entity.Assessment;
import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.Batch;
import com.parishi.utility.mapper.AssessmentMapper;
import com.parishi.utility.mapper.AssignmentPlanMapper;
import com.parishi.utility.mapper.BatchMapper;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AssessmentDaoImpl implements AssessmentDao
{
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    Logger logger;

    @Override
    public AssessmentDTO getAssessmentById(Integer assessmentId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Assessment a WHERE a.assessmentId = :assessmentId";

            // Create Query
            Query query = session.createQuery(hql);
            query.setParameter("assessmentId", assessmentId);

            // Execute Query
            List<Assessment> results = query.list();

            System.out.println("result assignment= " + results);

            if(results!=null && !results.isEmpty())
            {
                Assessment assessment = results.get(0);
                AssessmentDTO assessmentDTO = AssessmentMapper.fromEntityToDTO(assessment);
                System.out.println("att = " +assessmentDTO);
                return assessmentDTO;
            }
            return null;
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveAssessmentMarks(AssessmentDTO assessmentDTO) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Assessment assessment = AssessmentMapper.fromDTOToEntity(assessmentDTO);
            System.out.println("Assessment in controller = " +assessment);
            session.update(assessment);
            transaction.commit();
            return true;
        }
        catch (HibernateException e) {
            logger.error("Assessment not updated" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAssessment(AssessmentDTO assessmentDTO) {
        try
        {
            Session session = sessionFactory.openSession();
            Transaction transaction=session.beginTransaction();
            Assessment assessment = AssessmentMapper.fromDTOToEntity(assessmentDTO);
            session.update(assessment);
            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("Assessment not updated", e);
            return false;
        }
    }


    @Override
    public List<AssessmentDTO> getAllAssessment()
    {
        List<AssessmentDTO> assessmentDTOS = new ArrayList<>();
        try (Session session = sessionFactory.openSession();) {
            List<Assessment> assessments = session.createQuery("FROM Assessment", Assessment.class).getResultList();

            for (Assessment assessment : assessments) {
                AssessmentDTO assessmentDTO = AssessmentMapper.fromEntityToDTO(assessment);
                assessmentDTOS.add(assessmentDTO);
            }

            return assessmentDTOS;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;

    }
}
