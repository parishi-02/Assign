package com.parishi.dao;

import com.parishi.dto.BatchDTO;
import com.parishi.dto.DayWisePlanDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.*;
import com.parishi.utility.mapper.BatchMapper;
import com.parishi.utility.mapper.TraineeMapper;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class BatchDaoImpl implements BatchDao
{
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Logger logger;

    @Override
    public boolean createBatch(BatchDTO batchDTO) {
        try
        {
            Session session = sessionFactory.openSession();
            Transaction transaction=session.beginTransaction();

            Batch batch = BatchMapper.fromDTOToEntity(batchDTO);
            session.save(batch);
            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("Create Batch", e);
            return false;
        }

    }

    @Override
    public List<BatchDTO> getAllBatches() {
        List<BatchDTO> batchDTOS = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            List<String> batchIds = session.createQuery("SELECT batchId FROM Batch", String.class).getResultList();

            for (String batchId : batchIds) {
                Batch batch = session.load(Batch.class, batchId); // Load the entity by primary key
                BatchDTO batchDTO = BatchMapper.fromEntityToDTO(batch);
                batchDTOS.add(batchDTO);
            }

            for (BatchDTO batch : batchDTOS) {
                List<DayWisePlanDTO> dayWisePlans = batch.getCoursePlanDto().getDayWisePlanDTOS();

                Collections.sort(dayWisePlans, new Comparator<DayWisePlanDTO>() {
                    @Override
                    public int compare(DayWisePlanDTO o1, DayWisePlanDTO o2) {
                        return o1.getPlanId().compareTo(o2.getPlanId());
                    }
                });
            }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return batchDTOS;
    }

    @Override
    public BatchDTO getBatchById(String batchId) {
        BatchDTO batchDTO = null;
        try(Session session = sessionFactory.openSession()) {
            Batch batch = session.load(Batch.class, batchId);
            batchDTO = BatchMapper.fromEntityToDTO(batch);

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();

        }
        return batchDTO;
    }

    @Override
    public boolean updateBatch(BatchDTO batchDTO) {
        boolean flag = true;
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Batch batch = BatchMapper.fromDTOToEntity(batchDTO);
            session.merge(batch);
            transaction.commit();
        }
        catch (HibernateException e) {
            logger.error("Batch not updated" + e.getMessage());
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
    @Override
    public List<BatchDTO> getBatchByTrainerId(Integer trainerId) {
        List<BatchDTO> batchDTOS = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            String jpql = "SELECT b.batchId FROM Batch b WHERE b.trainer.trainerId = :trainerId";
            TypedQuery<String> query = session.createQuery(jpql, String.class);
            query.setParameter("trainerId", trainerId);
            List<String> batchIds = query.getResultList();

            System.out.println("Batch id in trainer dao = " + batchIds);
            for (String batchId : batchIds) {
                Batch batch = session.load(Batch.class, batchId); // Load the entity by primary key
                BatchDTO batchDTO = BatchMapper.fromEntityToDTO(batch);
                batchDTOS.add(batchDTO);
            }

            for (BatchDTO batch : batchDTOS) {
                List<DayWisePlanDTO> dayWisePlans = batch.getCoursePlanDto().getDayWisePlanDTOS();
                Collections.sort(dayWisePlans, new Comparator<DayWisePlanDTO>() {
                    @Override
                    public int compare(DayWisePlanDTO o1, DayWisePlanDTO o2) {
                        return o1.getPlanId().compareTo(o2.getPlanId());
                    }
                });
            }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return batchDTOS;
    }

    @Override
    public boolean updateCoursePlanDates(String batchId, String topicName, LocalDate newDate) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            List<Batch> batchList = session.createNativeQuery(" select * from track_batch where  BATCH_ID= ?1", Batch.class)
                    .setParameter(1,batchId)
                    .list();
            // Fetch the course plan by batchId
            List<DayWisePlan> dayWisePlan = batchList.get(0).getCoursePlan().getDayWisePlans();
            Collections.sort(dayWisePlan, new Comparator<DayWisePlan>() {
                @Override
                public int compare(DayWisePlan p1, DayWisePlan p2) {
                    return p1.getPlanId().compareTo(p2.getPlanId());
                }
            });
            // Print each item in a new line
            System.out.println("list = " +dayWisePlan);
            // Find the topic and calculate the date difference
            LocalDate currentTopicDate = null;
            int topicIndex = -1;
            for (int i = 0; i < dayWisePlan.size(); i++) {
                if (dayWisePlan.get(i).getTopics()!=null && dayWisePlan.get(i).getTopics().getTopicName().equalsIgnoreCase(topicName)) {
                    currentTopicDate = dayWisePlan.get(i).getPlannedDate();
                    topicIndex = i;
                    break;
                }
            }
            if (currentTopicDate == null || topicIndex == -1) {
                throw new IllegalArgumentException("Topic not found in course plan.");
            }

            long daysBetween = ChronoUnit.DAYS.between(currentTopicDate, newDate);

            System.out.println("Days = " + daysBetween);
            int i=0;
            int count=1;
            // Update the planned dates for this and subsequent topics
            for (i = topicIndex; i <dayWisePlan.size()-1; i++) {

                System.out.println("i = " + i);
                if(count<=daysBetween)
                {
                    DayWisePlan dayWisePlan1 = dayWisePlan.get(i);
                    System.out.println("Day wise plan 1 after looping = " + dayWisePlan1);

                    int j=i+1;

//                    System.out.println("holiday = " +dayWisePlan.get(j).getTopics().getTopicName().contains("Holiday"));

                    if(dayWisePlan.get(j).getTopics()!=null && dayWisePlan.get(j).getTopics().getTopicName().contains("Holiday"))
                    {
                        System.out.println("holiday 45 = " +dayWisePlan.get(j).getTopics().getTopicName().contains("Holiday"));
                        int countHoliday = 1;
                        while(countHoliday<= daysBetween && (dayWisePlan.get(j).getTopics()!=null && dayWisePlan.get(j).getTopics().getTopicName().contains("Holiday")))
                        {
                            countHoliday++;
                            count++;
                            j++;
                        }
                        if(countHoliday>daysBetween)
                            break;

                        DayWisePlan dayWisePlan2 = dayWisePlan.get(j);
                        System.out.println("Day wise plan 2 in holiday condition = " + dayWisePlan2);

                        Topic topic = dayWisePlan2.getTopics();
                        AssignmentPlan assignmentPlan = dayWisePlan2.getAssignmentPlan();
                        dayWisePlan1.setTopics(topic);
                        dayWisePlan1.setAssignmentPlan(assignmentPlan);

                        // Save the updated object back to the database
                        session.update(dayWisePlan1);
                        i=j-1;

                    }
                    else if(dayWisePlan.get(j).getTopics()==null) // saturday sunday case
                    {
                        count = count+2;
                        j=j+2;
                        DayWisePlan dayWisePlan2 = dayWisePlan.get(j);

                        System.out.println("Day wise plan in saturady sunday  = " + dayWisePlan2 + " i = " + i);
                        Topic topic = dayWisePlan2.getTopics();
                        AssignmentPlan assignmentPlan = dayWisePlan2.getAssignmentPlan();
                        dayWisePlan1.setTopics(topic);
                        dayWisePlan1.setAssignmentPlan(assignmentPlan);

                        // Save the updated object back to the database
                        session.update(dayWisePlan1);

                        i=j-1;
                    }
                    else
                    {

                        DayWisePlan dayWisePlan2 = dayWisePlan.get(j);
                        System.out.println("day wise plan in normal = " + dayWisePlan2 + "i = " + i);
                        Topic topic = dayWisePlan2.getTopics();
                        AssignmentPlan assignmentPlan = dayWisePlan2.getAssignmentPlan();
                        dayWisePlan1.setTopics(topic);
                        dayWisePlan1.setAssignmentPlan(assignmentPlan);

                        // Save the updated object back to the database
                        session.update(dayWisePlan1);
                    }
                    count++;
                }
                else
                    break;
            }
            DayWisePlan dayWisePlanHR = dayWisePlan.get(i);
            System.out.println("day wise in out = " + dayWisePlanHR + " i= " + i);
            Topic topicHR = new Topic();
            topicHR.setTopicName("HR INDUCTION");
            dayWisePlanHR.setTopics(topicHR);
            dayWisePlanHR.setAssignmentPlan(null);
            session.update(dayWisePlanHR);
            transaction.commit();
            return true;
        } catch (TransientObjectException e) {
            System.out.println("TransientObjectException: " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
           logger.error(e.getMessage());
           e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }


    @Override
    public boolean updateTopicPlannedDate(String batchId, Integer planId, LocalDate newDate,String remarks) {

        Session session = sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        try
        {
            Batch batch = session.load(Batch.class,batchId);
            List<DayWisePlan> dayWisePlans = batch.getCoursePlan().getDayWisePlans();
            Optional<DayWisePlan> result =  dayWisePlans.stream()
                    .filter(plan -> plan.getPlanId().equals(planId))
                    .findFirst();

            DayWisePlan dayWisePlan = result.get();
            dayWisePlan.setActualDate(newDate);
            dayWisePlan.setRemarks(remarks);
            session.update(dayWisePlan);
            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("Topic planned date not updated", e);
            return false;
        }
    }
    @Override
    public boolean addAssessment(BatchDTO batchDTO) {
        try
        {
            Session session = sessionFactory.openSession();
            Transaction transaction=session.beginTransaction();
            Batch batch = BatchMapper.fromDTOToEntity(batchDTO);
            session.update(batch);
            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("Assessment not added", e);
            return false;
        }
    }

    @Override
    public BatchDTO getBatchByTraineeId(Integer traineeId) {
        try (Session session = sessionFactory.openSession()) {
           List<BatchDTO> batchList = getAllBatches();

            System.out.println("tarinee id = " + traineeId);
            System.out.println("batchlist = " + batchList);

           for(BatchDTO batchDTO : batchList)
           {
               System.out.println("batch = " + batchDTO);
               for(TraineeDTO traineeDTO : batchDTO.getTraineeList())
               {
                   System.out.println("tarinee = " + batchDTO.getTraineeList());
                   if((traineeDTO.getEmployeeId()).equals(traineeId))
                       return batchDTO;
               }
           }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
