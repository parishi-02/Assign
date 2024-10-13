package com.parishi.dao;

import com.parishi.dto.TraineeDTO;
import com.parishi.entity.Trainee;
import com.parishi.utility.mapper.TraineeMapper;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TraineeDaoImpl implements TraineeDao
{
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Logger logger;
    @Override
    public List<TraineeDTO> getAllTrainees() {
        List<TraineeDTO> traineeDTOList = new ArrayList<>();
        try(Session session = sessionFactory.openSession();){
            List<Trainee> traineeList = session.createQuery("FROM Trainee", Trainee.class).getResultList();
            for(Trainee trainee : traineeList){
                TraineeDTO traineeDTO = TraineeMapper.fromEntityToDTO(trainee);
                traineeDTOList.add(traineeDTO);
            }
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return traineeDTOList;
    }

    @Override
    public boolean registerTrainee(TraineeDTO traineeDTO) {
        try(Session session = sessionFactory.openSession())
        {
            Transaction transaction=session.beginTransaction();

            Trainee trainee = TraineeMapper.fromDTOToEntity(traineeDTO);
            session.save(trainee);

            transaction.commit();
            return true;
        }
        catch(Exception e)
        {
            logger.error("register trainee", e);
            return false;
        }

    }

    @Override
    public boolean insertBulkTrainee(List<TraineeDTO> traineeDTOList) {
        int rowsAffected = 0;

        if (traineeDTOList == null || traineeDTOList.isEmpty()) {
            logger.error("traineeDTOList is empty");
            return false;
        }

        try( Session session = sessionFactory.openSession()){
            Transaction transaction=session.beginTransaction();

            for (TraineeDTO traineeDTO : traineeDTOList) {

                Trainee trainee = TraineeMapper.fromDTOToEntity(traineeDTO);

                session.save(trainee);
                rowsAffected++;
            }
            transaction.commit();
        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected > 0;
    }

    @Override
    public boolean updateTrainee(TraineeDTO traineeDTO) {
        if(traineeDTO == null){
            logger.error("traineeDTO is null, cannot update Trainee");
            return false;
        }

        try( Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            System.out.println("Trainee DTO = "+traineeDTO);
            Trainee trainee = TraineeMapper.fromDTOToEntity(traineeDTO);
            System.out.println("Trainee = " + trainee);
            session.update(trainee);
            transaction.commit();

        }catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public TraineeDTO getTrainee(int empId) {
        try(Session session = sessionFactory.openSession()) {
            Trainee trainee = session.get(Trainee.class, empId);
            if(trainee!=null){
                TraineeDTO traineeDTO = TraineeMapper.fromEntityToDTO(trainee);
                return traineeDTO;
            }else{
                logger.error("Trainee not fetched");
                return null;
            }
        }catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TraineeDTO> getTraineeListByBatchId(String batchId) {

        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT t.* FROM track_trainees t " +
                    "JOIN track_batch_track_trainees btm ON t.employee_id = btm.TRAINEELIST_EMPLOYEE_ID " +
                    "WHERE btm.BATCH_BATCH_ID = :batchId";
            NativeQuery<Trainee> query = session.createNativeQuery(sql, Trainee.class);
            query.setParameter("batchId", batchId);
            List<Trainee> traineeList = query.list();

            System.out.println("Dao = " +traineeList);

            List<TraineeDTO> traineeDTOList = new ArrayList<>();
            for(Trainee trainee : traineeList)
            {
                TraineeDTO traineeDTO = TraineeMapper.fromEntityToDTO(trainee);
                traineeDTOList.add(traineeDTO);
            }

            return traineeDTOList;

        }
        catch (HibernateException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
