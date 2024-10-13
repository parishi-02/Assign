package com.parishi.dao;

import com.parishi.dto.AssignmentPlanDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.Trainee;
import com.parishi.utility.mapper.AssignmentPlanMapper;
import com.parishi.utility.mapper.TraineeMapper;
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
import java.util.Map;

@Repository
public class AssignmentDaoImpl implements AssignmentDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    Logger logger;

    @Override
    public AssignmentPlanDTO getAssignmentById(Integer assignmentId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM AssignmentPlan a WHERE a.id = :assignmentId";

            // Create Query
            Query query = session.createQuery(hql);
            query.setParameter("assignmentId", assignmentId);

            // Execute Query
            List<AssignmentPlan> results = query.list();

            System.out.println("result assignment= " + results);

            if (results != null && !results.isEmpty()) {
                AssignmentPlan assignmentPlan = results.get(0);
                AssignmentPlanDTO assignmentPlanDTO = AssignmentPlanMapper.fromEntityToDTO(assignmentPlan);
                System.out.println("att = " + assignmentPlanDTO);
                return assignmentPlanDTO;
            }
            return null;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveAssignmentMarks(AssignmentPlanDTO assignmentPlanDTO) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            AssignmentPlan assignmentPlan = AssignmentPlanMapper.fromDTOToEntity(assignmentPlanDTO);
            System.out.println("Assignment in controller = " + assignmentPlan);
            session.update(assignmentPlan);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            logger.error("Attendance not updated" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAssignmentTotalScore(AssignmentPlanDTO assignmentPlanDTO) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            AssignmentPlan assignmentPlan = AssignmentPlanMapper.fromDTOToEntity(assignmentPlanDTO);
            session.update(assignmentPlan);
            transaction.commit();
            return true;
        } catch (Exception e) {
            logger.error("Assignment total marks not updated", e);
            return false;
        }
    }

    @Override
    public List<AssignmentPlanDTO> getAllAssignment() {
        List<AssignmentPlanDTO> assignmentPlanDTOS = new ArrayList<>();
        try (Session session = sessionFactory.openSession();) {
            List<AssignmentPlan> assignmentPlans = session.createQuery("FROM AssignmentPlan", AssignmentPlan.class).getResultList();

            for (AssignmentPlan assignmentPlan : assignmentPlans) {
                AssignmentPlanDTO assignmentPlanDTO = AssignmentPlanMapper.fromEntityToDTO(assignmentPlan);
                assignmentPlanDTOS.add(assignmentPlanDTO);
            }

            return assignmentPlanDTOS;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
